package blog.lohoknang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * RpcServer
 *
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-08
 */
@EnableMongoAuditing
@SpringBootApplication
@EnableConfigurationProperties
@EnableReactiveMongoRepositories
public class BlogBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogBackendApplication.class, args);
    }

}
