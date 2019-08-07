package blog.lohoknang;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import blog.lohoknang.entity.Blog;
import blog.lohoknang.repository.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class BlogBackendApplicationTests {
    @Resource
    private BlogRepository blogRepository;

    @Test
    public void contextLoads() throws IOException, InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Path dataPath = Paths.get("/Users/luxueneng/Documents/run");
        Flux.fromStream(Files
                .lines(dataPath)
                .map(it -> it.substring(1, it.length() - 1))
                .map(it -> it.split("(?:'), (?:')"))
                .filter(it -> {
                    if (it.length != 9) {
                        log.error("err id={}", it[0]);
                        return false;
                    }

                    return true;
                })
                .map(it -> Blog
                        .builder()
                        .category(it[1])
                        .author(it[2])
                        .title(it[3])
                        .intro(it[5].substring(0, Math.min(50, it[5].length())))
                        .content(it[5])
                        .videoNum(Integer.parseInt(it[8]))
                        .createdAt(LocalDateTime.from(formatter.parse(it[6])))
                        .updatedAt(LocalDateTime.from(formatter.parse(it[7])))
                        .build()))
                .publish(it -> blogRepository.saveAll(it))
                .doFinally(it -> {
                    log.info("res type={}", it);
                    countDownLatch.countDown();
                })
                .subscribe(it -> log.info("res={}", it));

        countDownLatch.await();
    }

}
