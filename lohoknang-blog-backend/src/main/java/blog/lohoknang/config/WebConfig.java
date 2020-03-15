package blog.lohoknang.config;

import blog.lohoknang.controller.BlogControlHandler;
import blog.lohoknang.controller.CommentCorsControlHandler;
import blog.lohoknang.controller.RobotControlHandler;
import blog.lohoknang.exc.InvalidParameterException;
import blog.lohoknang.exc.NotFoundException;
import blog.lohoknang.filter.SecureFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import javax.annotation.Resource;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Slf4j
@Configuration
public class WebConfig {
    @Resource
    private BlogControlHandler blogControlHandler;
    @Resource
    private CommentCorsControlHandler commentCorsControlHandler;
    @Resource
    private RobotControlHandler robotControlHandler;
    @Resource
    private SecureFilter secureFilter;

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions
                .route()
                .nest(RequestPredicates.path("/blogs"), builder -> builder
                        .PUT("/{id}", blogControlHandler::updateBlog)
                        .GET("/{id}", blogControlHandler::getBlog)
                        .POST("", blogControlHandler::insertBlog)
                        .GET("", blogControlHandler::getBlogIntro))
                .GET("/categories", blogControlHandler::getTopCategories)
                .GET("/dates", blogControlHandler::getTopDates)
                .GET("/updateds", blogControlHandler::getTopUpdateds)
                .POST("/authenticate", (severRequest) -> ServerResponse.accepted().build())
                .POST("/comment-cors", commentCorsControlHandler::commentCors)
                .GET("/sitemap.xml", robotControlHandler::getSiteMap)
                .filter(secureFilter)
                .onError(e -> (e instanceof InvalidParameterException),
                        (e, req) -> ServerResponse.badRequest().body(BodyInserters.fromObject(e.getMessage())))
                .onError(e -> (e instanceof NotFoundException),
                        (e, req) -> ServerResponse.notFound().build())
                .build();
    }
}
