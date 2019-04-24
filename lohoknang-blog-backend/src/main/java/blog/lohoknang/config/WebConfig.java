package blog.lohoknang.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import blog.lohoknang.controller.BlogControlHandler;
import blog.lohoknang.controller.CommentCorsControlHandler;
import blog.lohoknang.controller.RobotControlHandler;
import blog.lohoknang.exc.InvalidParameterException;
import blog.lohoknang.filter.RobotFilter;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Configuration
public class WebConfig implements WebFluxConfigurer {
    @Resource
    private BlogControlHandler blogControlHandler;
    @Resource
    private CommentCorsControlHandler commentCorsControlHandler;
    @Resource
    private RobotControlHandler robotControlHandler;
    @Resource
    private RobotFilter robotFilter;

    @SuppressWarnings("SpellCheckingInspection")
    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                .GET("/blogs/{id}", blogControlHandler::getBlog)
                .GET("/blogs", blogControlHandler::getBlogIntro)
                .GET("/categories", blogControlHandler::getTopCategories)
                .GET("/dates", blogControlHandler::getTopDates)
                .GET("/updateds", blogControlHandler::getTopUpdateds)
                .POST("/comment-cors", commentCorsControlHandler::commentCors)
                .GET("/rb", robotControlHandler::getIndex)
                .GET("/rb/blogs/{id}", robotControlHandler::getBlog)
                .GET("/sitemap.xml", robotControlHandler::getSiteMap)
                .GET("/gsitemap.xml", robotControlHandler::getGSiteMap)
                .filter(robotFilter)
                .onError(e -> (e instanceof InvalidParameterException),
                        (e, req) -> ServerResponse.badRequest().syncBody(e.getMessage()))
                .build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
