package blog.lohoknang.filter;

import java.util.stream.Stream;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import blog.lohoknang.controller.RobotControlHandler;
import reactor.core.publisher.Mono;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Component
public class RobotFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    @Resource
    private RobotControlHandler robotControlHandler;

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        String userAgent = String.join("", request
                .headers()
                .header("User-Agent"));
        String robot = "robot";
        String spider = "spider";
        if (!userAgent.contains(robot) && !userAgent.contains(spider)) {
            return next.handle(request);
        }

        String rb = "/rb";
        String siteMap = "/sitemap.xml";
        String gSiteMap = "/gsitemap.xml";

        String uri = request.path();
        if (Stream.of(rb, siteMap, gSiteMap).anyMatch(uri::contains)) {
            return next.handle(request);
        }

        return robotControlHandler.getIndex(request);
    }
}
