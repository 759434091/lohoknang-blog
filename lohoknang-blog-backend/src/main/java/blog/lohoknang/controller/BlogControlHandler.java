package blog.lohoknang.controller;

import blog.lohoknang.constant.FindType;
import blog.lohoknang.entity.Blog;
import blog.lohoknang.exc.InvalidParameterException;
import blog.lohoknang.service.BlogService;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Component
public class BlogControlHandler {
    @Resource
    private BlogService blogService;

    public Mono<ServerResponse> getBlog(ServerRequest serverRequest) {
        return Mono
                .just("id")
                .map(serverRequest::pathVariable)
                .map(blogService::getBlog)
                .flatMap(blogMono -> ServerResponse.ok().body(blogMono, Blog.class));
    }

    public Mono<ServerResponse> getBlogIntro(ServerRequest serverRequest) {
        String typeKey = "type";
        String valueKey = "value";
        String pageKey = "page";

        MultiValueMap<String, String> queryMap = serverRequest.queryParams();
        if (Stream
                .of(typeKey, valueKey, pageKey)
                .anyMatch(s -> !queryMap.containsKey(s))) {
            return Mono.error(new InvalidParameterException("Invalid query param"));
        }

        Integer page = Optional
                .of(pageKey)
                .map(queryMap::getFirst)
                .map(s -> {
                    try {
                        return Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        return -1;
                    }
                })
                .filter(p -> p >= 0 && p <= 500)
                .orElse(-1);
        if (page == -1) {
            return Mono.error(new InvalidParameterException("Invalid page"));
        }

        return Mono.just(typeKey)
                .map(queryMap::getFirst)
                .map(FindType::getType)
                .doOnNext(value -> {
                    if (value == null) {
                        throw new InvalidParameterException("invalid type=" + queryMap.getFirst(typeKey));
                    }
                })
                .map(value -> {
                    switch (value) {
                        case RAW:
                            return blogService.getBlogIntroByRaw(page);
                        case CATEGORY:
                            return blogService.getBlogIntroByCategory(queryMap.getFirst(valueKey), page);
                        case DATE:
                            LocalDateTime[] validateTime = Optional.of(valueKey)
                                    .map(queryMap::getFirst)
                                    .map(this::getValidDateTime)
                                    .orElseThrow(() -> new InvalidParameterException(
                                            "invalid datetime=" + queryMap.getFirst(valueKey)));

                            return blogService.getBlogIntroByDate(validateTime[0], validateTime[1], page);
                        default:
                            throw new InvalidParameterException("invalid type=" + value);
                    }
                })
                .flatMap(flux -> ServerResponse.ok().body(flux, Blog.class));
    }

    public Mono<ServerResponse> getTopCategories(ServerRequest serverRequest) {
        return getTopParam(serverRequest)
                .flatMapMany(blogService::getTopCategories)
                .collectList()
                .flatMap(it -> ServerResponse.ok().syncBody(it));
    }

    public Mono<ServerResponse> getTopDates(ServerRequest serverRequest) {
        return getTopParam(serverRequest)
                .flatMapMany(blogService::getTopDates)
                .collectList()
                .flatMap(it -> ServerResponse.ok().syncBody(it));
    }

    @SuppressWarnings("SpellCheckingInspection")
    public Mono<ServerResponse> getTopUpdateds(ServerRequest serverRequest) {
        return getTopParam(serverRequest)
                .flatMapMany(blogService::getTopUpdateds)
                .collectList()
                .flatMap(it -> ServerResponse.ok().syncBody(it));
    }

    private LocalDateTime[] getValidDateTime(String dateStr) {
        if (dateStr == null
                || dateStr.length() != 7
                || !dateStr.matches("\\d{4}-\\d{2}")) {
            return null;
        }
        int year = Integer.parseInt(dateStr.substring(0, 4));
        int month = Integer.parseInt(dateStr.substring(5, 7));
        if (month < Month.JANUARY.getValue() || month > Month.DECEMBER.getValue()) {
            return null;
        }

        LocalDateTime startTime = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endTime = startTime.plus(1, ChronoUnit.MONTHS);

        return new LocalDateTime[]{startTime, endTime};
    }

    private Mono<Integer> getTopParam(ServerRequest serverRequest) {
        return Mono
                .justOrEmpty(serverRequest.queryParam("top"))
                .map(Integer::valueOf)
                .onErrorResume(throwable -> Mono.empty())
                .filter(it -> it > 0 && it < 6)
                .switchIfEmpty(Mono.error(new InvalidParameterException("Invalid top")))
                .onErrorMap(throwable -> throwable instanceof NumberFormatException, throwable -> new InvalidParameterException("Invalid top"));
    }
}
