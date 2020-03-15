package blog.lohoknang.repository;

import blog.lohoknang.entity.Blog;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Repository
public interface BlogRepository extends ReactiveMongoRepository<Blog, ObjectId> {

    /**
     * findOrderByCreatedAtAsc
     *
     * @param pageable pageable
     *
     * @return flux
     */
    @Query(fields = "{ content : 0 }")
    Flux<Blog> findByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * findByOrderByCreatedAt
     *
     * @return flux
     */
    @Query(fields = "{ id : 1, title : 1, intro: 1, updatedAt : 1}")
    Flux<Blog> findByOrderByCreatedAt();

    /**
     * findByAndCategoryEqualsOrderByCreatedAtAsc
     *
     * @param category category
     * @param pageable pageable
     *
     * @return flux
     */
    @Query(fields = "{ content : 0 }")
    Flux<Blog> findByCategoryEquals(String category, Pageable pageable);

    /**
     * findByAndCreatedAtBetweenOrderByCreatedAtAsc
     *
     * @param startTime s
     * @param endTime   e
     * @param pageable  pageable
     *
     * @return flux
     */
    @Query(fields = "{ content : 0 }")
    Flux<Blog> findByCreatedAtBetween(LocalDateTime startTime,
                                      LocalDateTime endTime,
                                      Pageable pageable);
}
