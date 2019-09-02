package blog.lohoknang.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@ConfigurationProperties(prefix = "blog.lohoknang")
public class SecureFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    private Base64.Decoder decoder = Base64.getDecoder();

    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        val authList = request
                .headers()
                .header("Authorization");
        if (authList.isEmpty()) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        val authStr = authList.get(0);
        if (!authStr.startsWith("Basic ")) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        String decodeAuth;
        try {
            val encodeAuth = authStr.substring(6);
            decodeAuth = new String(decoder.decode(encodeAuth), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        val usernamePassword = decodeAuth.split(":");
        if (usernamePassword.length != 2) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!usernamePassword[0].equals(username)) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!usernamePassword[1].equals(password)) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        return next.handle(request);
    }
}
