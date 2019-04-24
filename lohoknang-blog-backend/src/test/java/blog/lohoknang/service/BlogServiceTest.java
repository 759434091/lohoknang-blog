package blog.lohoknang.service;

import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;

import blog.lohoknang.BlogBackendApplicationTests;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Slf4j
public class BlogServiceTest extends BlogBackendApplicationTests {
    @Resource
    private BlogService blogService;

    @Test
    public void getBlog() {
    }

    @Test
    public void getBlogIntroByRaw() {
    }

    @Test
    public void getBlogIntroByCategory() {
    }

    @Test
    public void getBlogIntroByDate() {
    }

    @Test
    public void getTopCategories() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        blogService.getTopCategories(5)
                .doOnComplete(countDownLatch::countDown)
                .subscribe(log::info);
        countDownLatch.await();
    }

    @Test
    public void getTopDates() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        blogService.getTopDates(5)
                .doOnComplete(countDownLatch::countDown)
                .subscribe(log::info);
        countDownLatch.await();
    }

    @Test
    @SuppressWarnings("SpellCheckingInspection")
    public void getTopUpdateds() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        blogService.getTopUpdateds(5)
                .doOnComplete(countDownLatch::countDown)
                .map(JSON::toJSONString)
                .subscribe(log::info);
        countDownLatch.await();
    }

    @Test
    public void getRobotBlog() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        blogService.getRobotBlog()
                .doOnComplete(countDownLatch::countDown)
                .map(JSON::toJSONString)
                .subscribe(log::info);
        countDownLatch.await();
    }
}