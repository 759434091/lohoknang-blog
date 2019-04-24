package blog.lohoknang.controller;

import javax.annotation.Resource;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import blog.lohoknang.service.BlogService;
import reactor.core.publisher.Mono;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Component
public class RobotControlHandler {
    @Resource
    private BlogService blogService;

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

    public Mono<ServerResponse> getBlog(ServerRequest serverRequest) {
        return Mono
                .just("id")
                .map(serverRequest::pathVariable)
                .flatMap(blogService::getBlog)
                .map(blog -> blogService
                        .getRobotBlog()
                        .map(b -> String.format(BLOG_FORMAT,
                                b.getId(),
                                b.getTitle(),
                                StringEscapeUtils.escapeHtml4(b.getIntro())))
                        .reduce((l, r) -> l + r)
                        .map(str -> String.format(BLOG_PAGE_FORMAT,
                                blog.getTitle(),
                                blog.getTitle(),
                                blog.getCategory(),
                                blog.getAuthor(),
                                blog.getCreatedAt(),
                                blog.getUpdatedAt(),
                                blog.getVideoNum(),
                                blog.getContent(),
                                str)))
                .flatMap(bodyMono -> ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(bodyMono, String.class));
    }

    @SuppressWarnings("unused")
    public Mono<ServerResponse> getSiteMap(ServerRequest serverRequest) {
        return blogService
                .getRobotBlog()
                .map(b -> String.format(SITE_CONTENT,
                        b.getId(),
                        b.getUpdatedAt(),
                        b.getTitle(),
                        StringEscapeUtils.escapeHtml4(b.getIntro()),
                        b.getCategory(),
                        b.getCreatedAt(),
                        b.getCategory()))
                .reduce((l, r) -> l + r)
                .map(str -> String.format(SITE_MAP, str))
                .flatMap(str -> ServerResponse.ok().contentType(MediaType.TEXT_XML).syncBody(str));
    }

    @SuppressWarnings("unused")
    public Mono<ServerResponse> getGSiteMap(ServerRequest serverRequest) {
        return blogService
                .getRobotBlog()
                .map(b -> String.format(G_SITE_CONTENT,
                        b.getId(),
                        b.getUpdatedAt()))
                .reduce((l, r) -> l + r)
                .map(str -> String.format(G_SITE_MAP, str))
                .flatMap(str -> ServerResponse.ok().contentType(MediaType.TEXT_XML).syncBody(str));
    }

    /* --------------------constant-------------------- */

    private static final String G_SITE_CONTENT = ""
            + "<url>"
            + "<loc>https://lohoknang.blog/blogs/%s</loc>"
            + "<lastmod>%s</lastmod>"
            + "<changefreq>always</changefreq>"
            + "</url>";

    private static final String G_SITE_MAP = ""
            + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">%s</urlset>"
            + "";

    private static final String SITE_MAP = ""
            + "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<urlset>%s</urlset>";

    private static final String SITE_CONTENT = "<url>"
            + "<loc>https://lohoknang.blog/blogs/%s</loc>"
            + "<lastmod>%s</lastmod>"
            + "<changefreq>always</changefreq>"
            + "<priority>0.5</priority>"
            + "<data>"
            + "<display>"
            + "<title>%s</title>"
            + "<content>%s</content>"
            + "<tag>%s</tag>"
            + "<pubTime>%s</pubTime>"
            + "<breadCrumb title=\"LO'S BLOG\" url=\"https://lohoknang.blog\"/>"
            + "<breadCrumb title=\"博客\" url=\"https://lohoknang.blog/blogs\"/>"
            + "<author nickname=\"A9043\" url=\"https://lohoknang.blog\""
            + "thumbnail=\"https://lohoknang.blog/static/myLogo.png\"/>"
            + "<property>%s</property>"
            + "</display>"
            + "</data>"
            + "</url>";

    private static final String BLOG_PAGE_FORMAT = ""
            + "<!DOCTYPE html>"
            + "<html>"
            + "<head>"
            + "<meta charset=\"utf-8\">"
            + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,"
            + "user-scalable=no\">"
            + "<link rel=\"shortcut icon\" type=\"image/x-icon\" href=\"static/myLogo.png\">"
            + "<title>a9043-blog-%s</title>"
            + "</head>"
            + "<body>"
            + "<h1>LO'S BLOG</h1>"
            + "<div>"
            + "<h2>%s</h2>"
            + "<h3>分类 %s |"
            + "作者 %s |"
            + "发布于 %s |"
            + "最后修改于 %s |"
            + "%s 阅读"
            + "</h3>"
            + "<p>%s</p>"
            + "</div>"
            + "<ul>%s</ul>"
            + "</body>"
            + "</html>";

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
