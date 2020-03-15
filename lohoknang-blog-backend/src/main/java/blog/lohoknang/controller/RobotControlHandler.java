package blog.lohoknang.controller;

import blog.lohoknang.service.BlogService;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.format.DateTimeFormatter;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Component
public class RobotControlHandler {
    @Resource
    private BlogService blogService;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_DATE;

    @SuppressWarnings("unused")
    public Mono<ServerResponse> getIndex(ServerRequest serverRequest) {
        return blogService
                .getRobotBlog()
                .map(blog -> String.format(BLOG_FORMAT,
                        blog.getId(),
                        blog.getTitle(),
                        StringEscapeUtils.escapeHtml4(blog.getIntro())))
                .reduce((l, r) -> l + r)
                .map(str -> String.format(INDEX_FORMAT, str))
                .flatMap(html -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).syncBody(html));
    }

    private static final String SITE_CONTENT = ""
            + "<url>"
            + "    <loc>https://www.lohoknang.com/#/blogs/%s</loc>"
            + "    <lastmod>%s</lastmod>"
            + "    <changefreq>monthly</changefreq>"
            + "</url>";

    /* --------------------constant-------------------- */
    private static final String SITE_MAP = ""
            + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">"
            + "    %s"
            + "</urlset>";

    @SuppressWarnings("unused")
    public Mono<ServerResponse> getSiteMap(ServerRequest serverRequest) {
        return blogService
                .getRobotBlog()
                .map(b -> String.format(SITE_CONTENT,
                        b.getId(),
                        dateTimeFormatter.format(b.getUpdatedAt())))
                .reduce((l, r) -> l + r)
                .map(str -> String.format(SITE_MAP, str))
                .flatMap(str -> ServerResponse.ok().contentType(MediaType.TEXT_XML).syncBody(str));
    }

    private static final String BLOG_FORMAT = ""
            + "<div>"
            + "<a href=\"https://lohoknang.blog/blogs/%s\">%s</a>"
            + "<p>%s...</p>"
            + "/div>";

    private static final String INDEX_FORMAT = ""
            + "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "<meta charset=\"utf-8\">"
            + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,"
            + "user-scalable=no\">"
            + "<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"static/myLogo.png\">"
            + "<title>a9043-blog</title>"
            + "</head>"
            + "<body>"
            + "<h1>LO'S BLOG</h1>"
            + "<h2>A9043-BLOG</h2>"
            + "<h3>lohoknang</h3>"
            + "%s"
            + "</body>"
            + "</html>";
}
