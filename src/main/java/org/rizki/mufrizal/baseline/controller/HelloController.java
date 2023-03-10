package org.rizki.mufrizal.baseline.controller;

import org.rizki.mufrizal.baseline.mapper.object.server.request.HelloServerRequest;
import org.rizki.mufrizal.baseline.restclient.HelloRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {

    @Autowired
    private HelloRestClient helloRestClient;

    @PostMapping(value = "/api/hello")
    public Mono<ResponseEntity<?>> helloWorld(@RequestBody HelloServerRequest helloServerRequest) {
        return helloRestClient.sayHello(helloServerRequest)
                .map(response -> ResponseEntity.status(HttpStatus.valueOf(response.getHttpStatus())).body(response.getBody()));

    }

}