package org.rizki.mufrizal.baseline.exception;

import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class RestClientExceptionHandler<T> {
    public Mono<T> onErrorResume(Throwable ex, Class<T> tClass) {
        if (ex instanceof WebClientResponseException exception) {
            return Mono.just(Objects.requireNonNull(exception.getResponseBodyAs(tClass)));
        }
        ExceptionHelper.logging(ex);
        return Mono.error(ex);
    }

}
