package blog.lohoknang.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.annotation.Resource;

import lombok.val;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.DateOperators;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import blog.lohoknang.entity.Blog;
import blog.lohoknang.exc.InvalidParameterException;
import blog.lohoknang.repository.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Slf4j
@Service
public class BlogService {
    private Integer pageSize = 10;
    @Resource
    private BlogRepository blogRepository;
    @Resource
    private ReactiveMongoTemplate reactiveMongoTemplate;

    private Scheduler statisticScheduler = Schedulers.newSingle("statistic");

    private ConcurrentSkipListSet<ObjectId> idSet = new ConcurrentSkipListSet<>();

    public Mono<Blog> getBlog(@NonNull String idStr) {
        return Mono
                .just(idStr)
                .map(ObjectId::new)
                .onErrorMap(
                        e -> (e instanceof IllegalArgumentException),
                        e -> new InvalidParameterException(e.getMessage())
                )
                .flatMap(blogRepository::findById)
                .doOnNext(this::addViewNum);
    }

    private void addViewNum(Blog blog){
        statisticScheduler.schedule(() -> {
            val id = blog.getId();
            blog.setViewNum(blog.getViewNum() + 1);
            blogRepository
                    .save(blog)
                    .doOnSubscribe(subscription -> {
                        val mills = System.currentTimeMillis();
                        while (!idSet.add(id)) {
                            if (System.currentTimeMillis() - mills > 3 * 1000) {
                                subscription.cancel();
                                break;
                            }
                        }
                    })
                    .doFinally(signalType -> {
                        if (signalType != SignalType.CANCEL) {
                            idSet.remove(id);
                        }
                    });
        });
    }

    public Flux<Blog> getBlogIntroByRaw(@NonNull Integer page) {
        return Mono.just(page)
                .map(p -> PageRequest.of(p, pageSize))
                .flatMapMany(blogRepository::findByOrderByCreatedAtDesc);
    }

    public Flux<Blog> getBlogIntroByCategory(
            @NonNull String category,
            @NonNull Integer page) {
        return Mono.just(page)
                .map(p -> PageRequest.of(p, pageSize))
                .flatMapMany(pageRequest -> blogRepository.findByCategoryEquals(category,
                        pageRequest));
    }

    public Flux<Blog> getBlogIntroByDate(@NonNull LocalDateTime startTime,
                                         @NonNull LocalDateTime endTime,
                                         @NonNull Integer page) {
        return Mono.just(page)
                .map(p -> PageRequest.of(p, pageSize))
                .flatMapMany(pageRequest ->
                        blogRepository.findByCreatedAtBetween(startTime, endTime,
                                pageRequest));
    }

    public Mono<List<String>> getTopCategories(Integer top) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("category").count().as("count"),
                Aggregation.sort(Sort.Direction.DESC, "count"),
                Aggregation.limit(top)
        );

        return reactiveMongoTemplate
                .aggregate(aggregation, "blog", Document.class)
                .map(document -> document.getString("_id"))
                .collectList();
    }

    public Mono<List<String>> getTopDates(Integer top) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation
                        .project("createdAt")
                        .and(DateOperators.dateOf("createdAt").toString("%Y-%m").onNullReturn("00-00"))
                        .as("year_month"),
                Aggregation.group("year_month"),
                Aggregation.sort(Sort.Direction.DESC, "_id"),
                Aggregation.limit(top)
        );

        return reactiveMongoTemplate
                .aggregate(aggregation, "blog", Document.class)
                .map(document -> document.getString("_id"))
                .collectList();
    }

    @SuppressWarnings("SpellCheckingInspection")
    public Flux<Blog> getTopUpdateds(Integer top) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.project("id", "title", "createdAt"),
                Aggregation.sort(Sort.Direction.DESC, "createdAt"),
                Aggregation.limit(top)
        );

        return reactiveMongoTemplate
                .aggregate(aggregation, "blog", Blog.class);
    }

    public Flux<Blog> getRobotBlog() {
        return blogRepository.findByOrderByCreatedAt();
    }
}
