package blog.lohoknang.controller;

import java.util.function.Function;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author <a href="luxueneng@baidu.com">luxueneng</a>
 * @since 2019-04-23
 */
@Slf4j
@Component
@ConfigurationProperties("comment-cors")
public class CommentCorsControlHandler {
    private WebClient webClient = WebClient.builder()
            .baseUrl("https://github.com")
            .build();

    @Getter
    @Setter
    private String clientId;

    @Getter
    @Setter
    private String clientSecret;

    private String uriFormat = "/login/oauth/access_token?client_id=%s&client_secret=%s&code=%s";

    public Mono<ServerResponse> commentCors(ServerRequest serverRequest) {
        return serverRequest
                .bodyToMono(String.class)
                .flatMap(code -> webClient
                        .post()
                        .uri(String.format(uriFormat, clientId, clientSecret, code))
                        .header("Accept", "application/json")
                        .header("User-Agent", "gh-oauth-server")
                        .exchange()
                        .onErrorContinue((e, val) -> log.error("err" + val, e))
                        .flatMap(clientResponse -> ServerResponse
                                .status(clientResponse.statusCode())
                                .body(clientResponse.bodyToMono(String.class), String.class))
                        .log())
                .onErrorContinue((e, val) -> log.error("err" + val, e))
                .log();

    }
}
