package org.rizki.mufrizal.baseline.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import reactor.core.publisher.Mono;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Mono<?>> generalException(Exception ex) {
        logging(ex);

        Map<String, Object> stringObjectsMap = new HashMap<>();
        stringObjectsMap.put("Code", 500);
        stringObjectsMap.put("Description", "Error");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Mono.just(stringObjectsMap));
    }

    @ExceptionHandler(WebClientRequestException.class)
    public ResponseEntity<Mono<?>> webClientRequestException(WebClientRequestException ex) {
        logging(ex);

        Map<String, Object> stringObjectsMap = new HashMap<>();
        stringObjectsMap.put("Code", 500);
        stringObjectsMap.put("Description", "Error connection");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Mono.just(stringObjectsMap));
    }

    private void logging(Exception ex) {
        StringWriter stringWriter = new StringWriter();
        log.error("Exception {}", ex.getMessage());
        ex.printStackTrace(new PrintWriter(stringWriter));
        log.error("Exception {}", stringWriter);
    }

}