package org.rizki.mufrizal.baseline.mapper;

import org.rizki.mufrizal.baseline.mapper.harmonized.HarmonizedMapper;
import org.rizki.mufrizal.baseline.mapper.object.server.response.GeneralServerResponse;
import org.rizki.mufrizal.baseline.mapper.harmonized.Harmonized;
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

    @Autowired
    private HarmonizedMapper harmonizedMapper;

    public HelloClientRequest toHelloClientRequest(HelloServerRequest helloServerRequest) {
        return HelloClientRequest.builder()
                .hash(environment.getRequiredProperty("backend.secret.key"))
                .message(helloServerRequest.getMessage())
                .referceNumber(helloServerRequest.getReferceNumber())
                .build();
    }

    public Mono<GeneralServerResponse> toHelloServerResponse(Mono<HelloClientResponse> helloClientResponseMono) {
        return helloClientResponseMono.zipWhen(hello -> harmonizedMapper.getHarmonized("BE", hello.getCode()))
                .flatMap(data -> {
                    HelloClientResponse helloClientResponse = data.getT1();
                    if (data.getT2() instanceof Harmonized harmonized) {
                        return harmonizedMapper.successHarmonized(
                                HelloServerResponse.builder()
                                        .referceNumber(helloClientResponse.getReferceNumber())
                                        .message(helloClientResponse.getMessage())
                                        .code(harmonized.getCode())
                                        .description(harmonized.getMessage())
                                        .build(),
                                harmonized.getHttpStatus());
                    }
                    return Mono.just((GeneralServerResponse) data.getT2());
                });
    }
}
