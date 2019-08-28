package blog.lohoknang.repository;

import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;

import com.alibaba.fastjson.JSON;

import blog.lohoknang.BlogBackendApplicationTests;
import blog.lohoknang.entity.Blog;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Slf4j
public class BlogRepositoryTest extends BlogBackendApplicationTests {
    @Resource
    private BlogRepository blogRepository;

    @Test
    public void save() throws InterruptedException {
        Blog blog = Blog
                .builder()
                .title("asdfsadf")
                .author("lohoknang")
                .category("node")
                .content("conasdfasdfdftent")
                .intro("insadftro")
                .viewNum(1)
                .build();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        blogRepository.save(blog)
                .doOnTerminate(countDownLatch::countDown)
                .subscribe(blog1 -> log.info(JSON.toJSONString(blog1)));
        countDownLatch.await();
    }

    @Test
    public void findBy() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        blogRepository
                .findByOrderByCreatedAtDesc(PageRequest.of(0, 10))
                .doOnComplete(countDownLatch::countDown)
                .subscribe(blog -> log.info(JSON.toJSONString(blog)));

        countDownLatch.await();
    }
}