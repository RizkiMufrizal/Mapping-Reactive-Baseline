package org.rizki.mufrizal.baseline.mapper;

import org.rizki.mufrizal.baseline.mapper.object.client.request.HelloClientRequest;
import org.rizki.mufrizal.baseline.mapper.object.client.response.HelloClientResponse;
import org.rizki.mufrizal.baseline.mapper.object.server.request.HelloServerRequest;
import org.rizki.mufrizal.baseline.mapper.object.server.response.HelloServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class HelloMapper {

    @Autowired
    private Environment environment;

    public HelloClientRequest toHelloClientRequest(HelloServerRequest helloServerRequest) {
        return HelloClientRequest.builder()
                .hash(environment.getRequiredProperty("backend.secret.key"))
                .message(helloServerRequest.getMessage())
                .referceNumber(helloServerRequest.getReferceNumber())
                .build();
    }

    public Mono<HelloServerResponse> toHelloServerResponse(Mono<HelloClientResponse> helloClientResponseMono) {
        return helloClientResponseMono
                .map(hello -> HelloServerResponse.builder()
                        .referceNumber(hello.getReferceNumber())
                        .message(hello.getMessage())
                        .code(hello.getCode())
                        .description(hello.getDescription())
                        .build());
    }
}
