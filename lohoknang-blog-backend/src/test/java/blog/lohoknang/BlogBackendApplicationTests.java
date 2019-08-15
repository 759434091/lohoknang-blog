package blog.lohoknang;

import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import blog.lohoknang.repository.BlogRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogBackendApplicationTests {
    @Resource
    private BlogRepository blogRepository;

    public static final String content = "感觉是时候开始给博客写一个后端了. \n"
            + "\n"
            + "一开始在考虑使用多页应用做后端还是单页应用做后端呢. 多页应用的话需要另开一个url做, 可以申请多一个域名或者用现用域名的新的资源路径. 但是我想这个博客也不大, 目前优化过后打包的大小才几十kb, "
            + "就干脆写在一个单页应用上好了. \n"
            + "\n"
            + "然后我在博客的某一个私密的地方偷偷加了一个登录入口, 如果你找到了欢迎在contact页面联系我hhhhhh. 然后就是在路由上在加一个路由页面了, 这个路由url被看到了也无妨, "
            + "没有获得登录令牌的话是没有用处的. \n"
            + "\n"
            + "### 那么以下是教程\n"
            + "\n"
            + "#### 1.  新建路由 修改router\n"
            + "如图, 新建新路由  \n"
            + "该路由页面为管理主页面 同时新增子路由用于嵌套路由 \n"
            + "(详见 vue-router : [nested-routes](https://router.vuejs.org/zh-cn/essentials/nested-routes.html) )  \n"
            + "![avatar](https://resource.lohoknang.blog/images/5d4a74a98bd8238fbd8b59d5/router.png)  \n"
            + "   \n"
            + "#### 2.  先编写主页面设计  \n"
            + "还是使用element-ui的组件 作为布局容器, 做路由\n"
            + "aside 为导航栏 \n"
            + "main 为子路由\n"
            + "\n"
            + "```html\n"
            + "  <div class=\"f\">\n"
            + "    <el-container class=\"container\">\n"
            + "      <el-aside width=\"70px\" class=\"aside\">\n"
            + "        <el-menu\n"
            + "          default-active=\"write\"\n"
            + "          class=\"el-menu-vertical-demo\"\n"
            + "          :collapse=\"true\"\n"
            + "          :router=\"true\">\n"
            + "          <el-menu-item index=\"write\">\n"
            + "            <i class=\"el-icon-document\"></i>\n"
            + "            <span slot=\"title\">写博客</span>\n"
            + "          </el-menu-item>\n"
            + "          <el-menu-item index=\"edit\">\n"
            + "            <i class=\"el-icon-setting\"></i>\n"
            + "            <span slot=\"title\">编辑博客</span>\n"
            + "          </el-menu-item>\n"
            + "        </el-menu>\n"
            + "      </el-aside>\n"
            + "      <el-main>\n"
            + "        <router-view></router-view>\n"
            + "      </el-main>\n"
            + "    </el-container>\n"
            + "  </div>\n"
            + "```\n"
            + "\n"
            + "效果如图\n"
            + "\n"
            + "![avatar](https://resource.lohoknang.blog/images/5d4a74a98bd8238fbd8b59d5/base.png)  \n"
            + "\n"
            + "#### 3.  新建博客页面\n"
            + "\n"
            + "想的是需要一个实时预览的markdown编辑器. 现在先自己做一个简陋的. \n"
            + "\n"
            + "布局一分为二  一边做编辑器 , 一边做预览器 两边各使用&lt;el-card&gt;做容器\n"
            + "```html\n"
            + "  <el-row :gutter=\"10\">\n"
            + "    <el-col :span=\"12\"></el-col>\n"
            + "    <el-col :span=\"12\"></el-col>\n"
            + "  </el-row>\n"
            + "```\n"
            + "\n"
            + "左边编辑器暂时使用表单作为编辑器\n"
            + "\n"
            + "```html\n"
            + "        <el-form class=\"edit-form\">\n"
            + "          <el-row :gutter=\"20\">\n"
            + "            <el-col :span=\"12\">\n"
            + "              <el-form-item label=\"标题\">\n"
            + "                <el-input v-model=\"form.title\"\n"
            + "                          placeholder=\"标题\">>\n"
            + "                </el-input>\n"
            + "              </el-form-item>\n"
            + "            </el-col>\n"
            + "            <el-col :span=\"12\">\n"
            + "              <el-form-item label=\"分类\">\n"
            + "                <el-input v-model=\"form.type\"\n"
            + "                          placeholder=\"分类\"></el-input>\n"
            + "              </el-form-item>\n"
            + "            </el-col>\n"
            + "          </el-row>\n"
            + "          <el-form-item label=\"正文\">\n"
            + "            <el-input type=\"textarea\"\n"
            + "                      :autosize=\"{ minRows: 5, maxRows: 10}\"\n"
            + "                      placeholder=\"请输入内容\"\n"
            + "                      v-model=\"form.content\"></el-input>\n"
            + "          </el-form-item>\n"
            + "        </el-form>\n"
            + "```\n"
            + "\n"
            + "然后添加监听器侦听form.content的变化\n"
            + "利用原来展示博客的markdown解析器解析内容并实时编译并显示\n"
            + "\n"
            + "```javascript\n"
            + "import HyperDown from 'hyperdown';\n"
            + "\n"
            + "const parser = new HyperDown();\n"
            + "\n"
            + "export default {\n"
            + "  name: 'Write',\n"
            + "  data() {\n"
            + "    return {\n"
            + "      htmlContent: '',\n"
            + "      form: {\n"
            + "        title: '',\n"
            + "        type: '',\n"
            + "        content: '',\n"
            + "      },\n"
            + "    };\n"
            + "  },\n"
            + "  watch: {\n"
            + "    'form.content': function updateView(content) {\n"
            + "      this.htmlContent = parser.makeHtml(content);\n"
            + "    },\n"
            + "  },\n"
            + "  }\n"
            + "```\n"
            + "\n"
            + "最后是预览的代码\n"
            + "```html\n"
            + "      <el-card class=\"card view-card\" shadow=\"hover\">\n"
            + "        <el-scrollbar class=\"scrollbar\" ref=\"scrollBar\">\n"
            + "          <div class=\"view-content\">\n"
            + "            <h1 v-text=\"form.title\"></h1>\n"
            + "            <p v-text=\"form.type === '' ? '' : '分类: '+ form.type\"></p>\n"
            + "            <div class=\"html-content\" v-html=\"htmlContent\" v-highlight></div>\n"
            + "          </div>\n"
            + "        </el-scrollbar>\n"
            + "      </el-card>\n"
            + "```\n"
            + "\n"
            + "使用了hightlight.js 作为高亮插件. 与vue 有冲突,  要用的话自行google解决\n"
            + "\n"
            + "#### 4. 最终效果图\n"
            + "![final](https://resource.lohoknang.blog/images/5d4a74a98bd8238fbd8b59d5/final.png)  \n"
            + "\n"
            + "-------------------------\n"
            + "**************************\n"
            + "\n"
            + "##### 现在一个比较大的问题是编辑器和预览器的滚动条不会自动匹配滚动, 但是滚动条用的是element-ui 的隐藏组件. 这个组件没有文档, 就有点难受了. 日后有机会再修改吧\n"
            + "\n"
            + "##### 图片放大我会琢磨一下怎么做的\n"
            + "\n"
            + "##### 后端因为不是本文重点, 就不做介绍了";

    @Test
    public void contextLoads() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        blogRepository
                .findById(new ObjectId("5d4a74a98bd8238fbd8b59d4"))
                .doOnNext(it -> it.setContent(it
                        .getContent()
                        .replace("\\\'", "\'")
                        .replace("\\\"", "\"")
                        .replace("\\n", "\n")
                        .replace("\\r", "\r")
                ))
                /*.doOnNext(it -> it.setContent(content))*/
                .flatMap(blogRepository::save)
                .doOnError(e -> log.error("catch err", e))
                .doFinally(it -> {
                    log.info("type={}", it);
                    countDownLatch.countDown();
                })
                .subscribe(it -> log.info("res={}", it));

        countDownLatch.await();
    }
}
