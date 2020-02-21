package blog.lohoknang;

import blog.lohoknang.repository.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogBackendApplicationTests {
    @Resource
    private BlogRepository blogRepository;

    public static final String content = "## 前言\n" +
            "\n" +
            "阅读本文需要\n" +
            "\n" +
            "- 函数式编程\n" +
            "- 响应式编程\n" +
            "- Spring Boot、Project Reactor\n" +
            "\n" +
            "---\n" +
            "\n" +
            "Workflow，即为工作流。其需要对输入数据源（pull数据、push数据）通过串行和并行的工作链进行处理，最后输出结果（打印、储存……）\n" +
            "\n" +
            "`S(ource) -> workflow -> R(esult)`\n" +
            "\n" +
            "Workflow的基本工作单位`WorkUnit`代表一个单独的处理单元，为工作流程中的最小工作单元。\n" +
            "\n" +
            "![image-20200220213238550](https://resource.lohoknang.blog/images/5e4e8cf8a66e88368016eb81/image-20200220213238550.png)\n" +
            "\n" +
            "## 特征\n" +
            "\n" +
            "一条完整Workflow有以下要求。\n" +
            "\n" +
            "- 并行处理正交WorkUnit\n" +
            "- 串行处理前后依赖的WorkUnit\n" +
            "- 处理数据储存在一个单独的上下文中\n" +
            "\n" +
            "## 抽象\n" +
            "\n" +
            "以抖音发布短视频为例子。当用户拍摄并编辑完毕短视频之后，点击发布按钮触发视频上传。但是，在视频可以被检索和推荐之前，需要有很多工作需要完成。例如，视频转码，视频内容识别，视频打标签，视频内容审核(政治、色情、)，视频入库……\n" +
            "\n" +
            "使用Workflow抽象该案例，上传视频后，即往视频处理链投入视频Source，然后创建工作流程上下文Context正式开始工作流处理。\n" +
            "\n" +
            "定义基本工作单元\n" +
            "\n" +
            "- `WorkUnit1`第一步为视频转码，需要在所有工作之前进行。\n" +
            "- `WorkUnit2`第二步为视频内容识别，这一步为后续提供一些必要基本信息。\n" +
            "- `WorkUnit3`第三步为视频打上标签，例如美女、舞蹈、搞笑、段子……\n" +
            "- `ParallelWorkUnit4A`第四步为视频审核，这一步有很多但是互不相关的审核策略，完全可以并行化处理\n" +
            "- `ParallelWorkUnit4B`并行策略B\n" +
            "- `ParallelWorkUnit4C`并行策略C\n" +
            "\n" +
            "`FinalContextConsumer` 最后一步为视频入库，入库后的视频既可以被检索然后交由Feed流推荐系统投喂给其他用户\n" +
            "\n" +
            "![image-20200220213354361](https://resource.lohoknang.blog/images/5e4e8cf8a66e88368016eb81/image-20200220213354361.png)\n" +
            "\n" +
            "每日会有成千上万个视频流入该数据处理流，即Workflow是对数据流中的每一个数据进行处理。我们换一个视觉表述Workflow\n" +
            "\n" +
            "```\n" +
            "----o---o---o----->\n" +
            "    |   |   |\n" +
            "    ----------\n" +
            "---| workflow |----\n" +
            "    ----------\n" +
            "    |   |   |\n" +
            "----x---x---x----->\n" +
            "```\n" +
            "\n" +
            "很直观的，o为每一条上传的数据源，x为经过数据流处理完毕的结果，从左到右为时间线的发展。\n" +
            "最终细化如下。\n" +
            "\n" +
            "```\n" +
            "----o-----o-----o------->\n" +
            "    |     |     |\n" +
            "----c-----c-----c-------> map to Context\n" +
            "    |     |     |\n" +
            "----c1----c1----c1------> WorkUnit1\n" +
            "    |     |     |\n" +
            "----c2----c2----c2------> WorkUnit2\n" +
            "    |     |     |\n" +
            "----c3----c3----c3------> WorkUnit3\n" +
            "    |     |     |\n" +
            "----ccc---ccc---ccc-----> WorkUnit4A + WorkUnit4B + WorkUnit4C\n" +
            "    |     |     |\n" +
            "----x-----x-----x------->\n" +
            "```\n" +
            "\n" +
            "## 设计\n" +
            "\n" +
            "于是，完整的工作流程抽象完毕，可以开始设计代码。\n" +
            "\n" +
            "首先定义视频源的类型，例如定义为`VideoRequest`。于是数据源流的类型为`Publisher<VideoRequest>`，对于`[n:m]`的视频源流来讲，会是`Flux<VideoRequest>`\n" +
            "\n" +
            "工作流的结果应该是一个包含源`Request`，并且有所有相关的处理完毕信息的单一`Context`，于是工作流的返回值是`[0:1]`的`Publisher<Context>`，具体为`Mono<Context>`\n" +
            "\n" +
            "伪代码如下\n" +
            "\n" +
            "```java\n" +
            "Mono<Context> contextMono = ((Flux<VideoRequest>)source)\n" +
            "    .map(request -> Context.of(\"request\", request))\n" +
            "    .map(videoWorkflow::apply);\n" +
            "```\n" +
            "\n" +
            "### 工作单元定义\n" +
            "\n" +
            "每一个工作单元都是根据传入的上下文进行工作，并处理成新的上下文返回，于是它为一个`UnaryOperator`，具体类型为`UnaryOperator<Context>`，为其定义别名`WorkUnit`\n" +
            "\n" +
            "```java\n" +
            "@FunctionalInterface\n" +
            "public interface WorkUnit extends UnaryOperator<Context> {\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "\n" +
            "\n" +
            "### 工作流定义\n" +
            "\n" +
            "以串行工作流中进行并行工作流的方式设计，于是工作流程类型为`Flux<ParallelFlux<WorkUnit>>`\n" +
            "\n" +
            "于串行工作流中，每一个工作单元都依赖上一个工作单元的结果，最终返回最后一个工作单元处理的上下文。为典型的`reduceOperator`于是代码可以如下实现\n" +
            "\n" +
            "```java\n" +
            "Flux<ParallelFlux<WorkUnit>> workflow;\n" +
            "\n" +
            "Mono<Context> contextMono = ((Flux<VideoRequest>)source)\n" +
            "    .map(request -> Context.of(\"request\", request))\n" +
            "    .flatmap(context -> (Mono<Context>) workflow\n" +
            "             .reduce(\n" +
            "                 context,\n" +
            "                 parallelWorkflow::apply\n" +
            "             ));\n" +
            "```\n" +
            "\n" +
            "而并行工作流中，所有工作单元可以根据输入的`Context`并行处理，在最终聚合为一个`Context`结果即可。\n" +
            "\n" +
            "由于`reduce`中的聚合函数为异步函数，所以reduce的State必须为一个`Publisher`。不然需要在该异步函数的最后加上`block()`语句。在响应式编程中，一般是不允许阻塞的。\n" +
            "\n" +
            "于是代码可以做如下细化，其中`toProcessor`为将数据流转换为hot stream，在响应式数据流中cold/hot stream的性质不同，由于这里`reduce`操作的State是一个`Publisher`，如果使用cold stream会使得上游数据流重复被订阅触发（见`marked`注释处）。这一步使用`cache()`也可，原理和结果应该是一样的（不保证底层）。\n" +
            "\n" +
            "```java\n" +
            "Flux<ParallelFlux<WorkUnit>> workflow;\n" +
            "\n" +
            "Mono<Context> contextMono = ((Flux<VideoRequest>)source)\n" +
            "    .map(request -> Context.of(\"request\", request))\n" +
            "    .flatmap(context -> (Mono<Context>) workflow\n" +
            "             .reduce(\n" +
            "                 Mono.just(context),\n" +
            "                 (Mono<Context> contextMono, ParallelFlux<WorkUnit> parallelFlux) ->\n" +
            "                     (Mono<Context>) parallelWorkflow\n" +
            "                         .flatMap(contextMono::map) // marked\n" +
            "                         .reduce(Context::putAll)\n" +
            "                         .toProcessor() // .cache() can also\n" +
            "             )\n" +
            "             .flatMap(Function.identity())\n" +
            "    );\n" +
            "```\n" +
            "\n" +
            "reduce的过程如下面运算，在`reduce`操作过程中，如果使用cold stream会导致重复计算之前的函数，这在Workflow中是不允许（没有必要，且性能浪费），尽管在纯函数中它是合法且不会出错的。于是，turn it hot，或者cache掉运算结果，能够保证程序往预想的工作流抽象运行。\n" +
            "\n" +
            "```\n" +
            "state0 = F(x)\n" +
            "state1 = H(x) = h1(F(x)) + h2(F(x)) + ... + h?(F(x)) = h1(state0) + h2(state0) + ... + h?(state0)\n" +
            "state2 = I(x) = i1(H(x)) + i2(H(x)) + ... + i?(H(x)) = i1(state1) + i2(state1) + ... + i?(state1)\n" +
            "...\n" +
            "stateN = N(x) = ...\n" +
            "\n" +
            "return stateN\n" +
            "```\n" +
            "\n" +
            "到此，工作流程就基本实现完毕了。\n" +
            "\n" +
            "## Spring Boot Starter支持\n" +
            "\n" +
            "设计预想的配置方式为：业务开发者开发`WorkUnit`并注册成Bean，然后通过properties配置具体的一条或多条工作流。\n" +
            "\n" +
            "下面为配置两条不同的工作流样例。\n" +
            "\n" +
            "```yaml\n" +
            "blog:\n" +
            "  lohoknang:\n" +
            "    workflow:\n" +
            "      reactive:\n" +
            "        enabled: true\n" +
            "        definitions:\n" +
            "          workflow1:\n" +
            "            - workUnit1\n" +
            "            - workUnit3A\n" +
            "            - workUnit3B\n" +
            "          workflow2:\n" +
            "            - workUnit2\n" +
            "            - workUnit3A\n" +
            "            - workUnit3C\n" +
            "```\n" +
            "\n" +
            "### AutoConfiguration\n" +
            "\n" +
            "根据配置的设计，开发自动配置类`WorkflowDefinitionAutoConfiguration`。其中，需要获取`ApplicationContext`以注册Bean实例，需要将配置读取到`definitions`。\n" +
            "\n" +
            "```java\n" +
            "@Configuration\n" +
            "@ConditionalOnProperty(value = \"blog.lohoknang.workflow.reactive.enabled\", havingValue = \"true\")\n" +
            "@ConfigurationProperties(\"blog.lohoknang.workflow.reactive\")\n" +
            "public class WorkflowDefinitionAutoConfiguration implements ApplicationContextAware {\n" +
            "    private GenericApplicationContext applicationContext;\n" +
            "    \n" +
            "    @Getter\n" +
            "    @Setter\n" +
            "    private Map<String, List<List<String>>> definitions;\n" +
            "    \n" +
            "    @PostConstruct\n" +
            "    public void postConstruct() {\n" +
            "        \n" +
            "    }\n" +
            "    \n" +
            "    @Override\n" +
            "    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {\n" +
            "        this.applicationContext = (GenericApplicationContext) applicationContext;\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "在`postConstruct`注册工作流。注意创建Workflow Flux的时候，将stream collect掉后再创建Flux。因为如果使用`Flux.fromStream()`操作符，将会导致该stream flux第一次使用完毕后即被关闭。这里不一定使用list flux，一般只要符合can be re-used就可。\n" +
            "\n" +
            "```java\n" +
            "public void postConstruct() {\n" +
            "    Objects.requireNonNull(definitions, \"Invalid workflow definition\");\n" +
            "    Map<String, WorkUnit> workUnitMap = applicationContext.getBeansOfType(WorkUnit.class);\n" +
            "\n" +
            "    definitions\n" +
            "            .entrySet()\n" +
            "            .stream()\n" +
            "            .map(entry -> new WorkflowDefinition(\n" +
            "                    entry.getKey(),\n" +
            "                    Flux\n" +
            "                            .fromIterable(entry\n" +
            "                                    .getValue()\n" +
            "                                    .stream()\n" +
            "                                    .map(list -> Flux\n" +
            "                                            .fromIterable(list\n" +
            "                                                    .stream()\n" +
            "                                                    .map(workUnitMap::get)\n" +
            "                                                    .collect(Collectors.toList())\n" +
            "                                            )\n" +
            "                                            .parallel())\n" +
            "                                        .collect(Collectors.toList()))\n" +
            "            ))\n" +
            "            .forEach(workflowDefinition -> applicationContext.registerBean(\n" +
            "                    workflowDefinition.getName(),\n" +
            "                    WorkflowDefinition.class,\n" +
            "                    () -> workflowDefinition\n" +
            "            ));\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "### WorkflowDefinition\n" +
            "\n" +
            "是一个包装类，对基本信息包装一下而已。\n" +
            "\n" +
            "```java\n" +
            "@Data\n" +
            "@AllArgsConstructor\n" +
            "public class WorkflowDefinition {\n" +
            "    private String name;\n" +
            "    private Flux<ParallelFlux<WorkUnit>> workflowDefinitionFlux;\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "### WorkUnit\n" +
            "\n" +
            "如上文定义\n" +
            "\n" +
            "```java\n" +
            "@FunctionalInterface\n" +
            "public interface WorkUnit extends UnaryOperator<Context> {\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "### WorkflowFactory\n" +
            "\n" +
            "为具体的完整的Workflow的实现，代码如上文。用于绑定数据源和Workflow。\n" +
            "\n" +
            "```java\n" +
            "public class WorkflowFactory {\n" +
            "    public static final String REQUEST_KEY = \"blog.lohoknang.workflow.reacitve.request\";\n" +
            "\n" +
            "    public static <T> Flux<Context> create(Flux<T> source, WorkflowDefinition workFlowDefinition) {\n" +
            "        return source\n" +
            "                .map(request -> Context.of(REQUEST_KEY, request))\n" +
            "                .flatMap(context -> workFlowDefinition\n" +
            "                        .getWorkflowDefinitionFlux()\n" +
            "                        .reduce(\n" +
            "                                MonoProcessor.just(context),\n" +
            "                                (contextMono, parallelFlux) -> parallelFlux\n" +
            "                                        .flatMap(contextMono::map)\n" +
            "                                        .reduce(Context::putAll)\n" +
            "                                        .toProcessor()\n" +
            "                        )\n" +
            "                        .flatMap(Function.identity())\n" +
            "                );\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## 使用案例\n" +
            "\n" +
            "在Spring Boot应用中如何使用该框架。\n" +
            "\n" +
            "### 配置\n" +
            "\n" +
            "先是完成，由开发者实现并注册的工作单元`WorkUnit`\n" +
            "\n" +
            "```java\n" +
            "@Configuration\n" +
            "static class TestConfig {\n" +
            "    @Bean\n" +
            "    public WorkUnit workUnit1() {\n" +
            "        return context -> context.put(\"workUnit1\", \"workUnit1\");\n" +
            "    }\n" +
            "\n" +
            "    @Bean\n" +
            "    public WorkUnit workUnit2() {\n" +
            "        return context -> context.put(\"workUnit2\", \"workUnit2\");\n" +
            "    }\n" +
            "\n" +
            "    @Bean\n" +
            "    public WorkUnit workUnit3A() {\n" +
            "        return context -> context.put(\"workUnit3A\", \"workUnit3A\");\n" +
            "    }\n" +
            "\n" +
            "    @Bean\n" +
            "    public WorkUnit workUnit3B() {\n" +
            "        return context -> context.put(\"workUnit3B\", \"workUnit3B\");\n" +
            "    }\n" +
            "\n" +
            "    @Bean\n" +
            "    public WorkUnit workUnit3C() {\n" +
            "        return context -> context.put(\"workUnit3C\", \"workUnit3C\");\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "接着配置Workflow Definition\n" +
            "\n" +
            "```yaml\n" +
            "blog:\n" +
            "  lohoknang:\n" +
            "    workflow:\n" +
            "      reactive:\n" +
            "        enabled: true\n" +
            "        definitions:\n" +
            "          workflow1:\n" +
            "            - workUnit1\n" +
            "            - workUnit3A\n" +
            "            - workUnit3B\n" +
            "          workflow2:\n" +
            "            - workUnit2\n" +
            "            - workUnit3A\n" +
            "            - workUnit3C\n" +
            "```\n" +
            "\n" +
            "### 使用\n" +
            "\n" +
            "定义服务`TestService`测试Workflow\n" +
            "\n" +
            "```java\n" +
            "@Service\n" +
            "class TestService {\n" +
            "    @Resource\n" +
            "    private WorkflowDefinition workflow1;\n" +
            "    \n" +
            "    @Resource\n" +
            "    private WorkflowDefinition workflow2;\n" +
            "    \n" +
            "    public void test() {\n" +
            "        WorkflowFactory.create(Flux.just(\"video1\", \"video2\"), workflow1).log().subscribe();\n" +
            "        WorkflowFactory.create(Flux.just(\"video1\", \"video2\"), workflow2).log().subscribe();\n" +
            "    }\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "### 测试\n" +
            "\n" +
            "调用`test`方法，测试结果如下\n" +
            "\n" +
            "```\n" +
            "2020-02-20 19:29:58.435  INFO 8644 --- [           main] reactor.Flux.FlatMap.1                   : onSubscribe(FluxFlatMap.FlatMapMain)\n" +
            "2020-02-20 19:29:58.437  INFO 8644 --- [           main] reactor.Flux.FlatMap.1                   : request(unbounded)\n" +
            "2020-02-20 19:29:58.487  INFO 8644 --- [           main] reactor.Flux.FlatMap.1                   : onNext(ContextN{blog.lohoknang.workflow.reacitve.request=video1, workUnit1=workUnit1, workUnit3A=workUnit3A, workUnit3B=workUnit3B})\n" +
            "2020-02-20 19:29:58.489  INFO 8644 --- [           main] reactor.Flux.FlatMap.1                   : onNext(ContextN{blog.lohoknang.workflow.reacitve.request=video2, workUnit1=workUnit1, workUnit3A=workUnit3A, workUnit3B=workUnit3B})\n" +
            "2020-02-20 19:29:58.489  INFO 8644 --- [           main] reactor.Flux.FlatMap.1                   : onComplete()\n" +
            "2020-02-20 19:29:58.489  INFO 8644 --- [           main] reactor.Flux.FlatMap.2                   : onSubscribe(FluxFlatMap.FlatMapMain)\n" +
            "2020-02-20 19:29:58.489  INFO 8644 --- [           main] reactor.Flux.FlatMap.2                   : request(unbounded)\n" +
            "2020-02-20 19:29:58.490  INFO 8644 --- [           main] reactor.Flux.FlatMap.2                   : onNext(ContextN{blog.lohoknang.workflow.reacitve.request=video1, workUnit2=workUnit2, workUnit3A=workUnit3A, workUnit3C=workUnit3C})\n" +
            "2020-02-20 19:29:58.491  INFO 8644 --- [           main] reactor.Flux.FlatMap.2                   : onNext(ContextN{blog.lohoknang.workflow.reacitve.request=video2, workUnit2=workUnit2, workUnit3A=workUnit3A, workUnit3C=workUnit3C})\n" +
            "2020-02-20 19:29:58.491  INFO 8644 --- [           main] reactor.Flux.FlatMap.2                   : onComplete()\n" +
            "\n" +
            "```\n" +
            "\n" +
            "\n" +
            "\n" +
            "## 代码\n" +
            "\n" +
            "案例代码仓库位于Github [reactive-workflow](https://github.com/759434091/reactive-workflow)\n";

    @Test
    public void contextLoads() throws InterruptedException {
/*        Blog blog = Blog
                .builder()
                .id(new ObjectId("5e4e8cf8a66e88368016eb81"))
                .title("构建响应式Workflow应用")
                .author("Hoknang Lo")
                .category("响应式编程")
                .content(content)
                .intro(content.substring(0, 120))
                .viewNum(12)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        blogRepository
                .save(blog)
                .doOnError(e -> log.error("catch err", e))
                .doFinally(it -> {
                    log.info("type={}", it);
                    countDownLatch.countDown();
                })
                .subscribe(it -> log.info("res={}", it));*/

        CountDownLatch countDownLatch = new CountDownLatch(1);
        blogRepository
                .findById(new ObjectId("5e4e8cf8a66e88368016eb81"))
                .doOnNext(blog -> blog.setContent(content))
                .flatMap(blogRepository::save)
                .doOnError(e -> log.error("catch err", e))
                .doFinally(it -> {
                    log.info("type={}", it);
                    countDownLatch.countDown();
                })
                .subscribe(it -> log.info("res={}", it));

        countDownLatch.await();
    }

    @Test
    public void objectIdGet() {
        log.info("{}", ObjectId.get());
    }
}
