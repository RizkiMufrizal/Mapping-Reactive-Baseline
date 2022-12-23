package org.rizki.mufrizal.baseline.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Mono<?>> generalException(Exception ex) {
        ExceptionHelper.logging(ex);

        Map<String, Object> stringObjectsMap = new HashMap<>();
        stringObjectsMap.put("Code", 500);
        stringObjectsMap.put("Description", "Error");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Mono.just(stringObjectsMap));
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<Mono<?>> webClientRequestException(WebClientRequestException ex) {
        ExceptionHelper.logging(ex);

        Map<String, Object> stringObjectsMap = new HashMap<>();
        stringObjectsMap.put("Code", 500);
        stringObjectsMap.put("Description", "Error connection");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Mono.just(stringObjectsMap));
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Mono<?>> webClientResponseException(WebClientResponseException ex) {
        ExceptionHelper.logging(ex);

        Map<String, Object> stringObjectsMap = new HashMap<>();
        stringObjectsMap.put("Code", 500);
        stringObjectsMap.put("Description", "Error connection");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Mono.just(stringObjectsMap));
    }

}