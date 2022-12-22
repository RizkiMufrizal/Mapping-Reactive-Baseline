package org.rizki.mufrizal.baseline.controller;

import org.rizki.mufrizal.baseline.mapper.object.client.request.HelloClientRequest;
import org.rizki.mufrizal.baseline.mapper.object.client.response.HelloClientResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class Sample {

    @PostMapping(value = "/api/test")
    public ResponseEntity<HelloClientResponse> test(@RequestBody HelloClientRequest helloClientRequest) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(HelloClientResponse.builder()
                        .message(helloClientRequest.getMessage())
                        .referceNumber(helloClientRequest.getReferceNumber())
                        .code("200")
                        .description("Success")
                        .hash(UUID.randomUUID().toString())
                        .build());
    }

}