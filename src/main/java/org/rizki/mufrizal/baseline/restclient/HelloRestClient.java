package org.rizki.mufrizal.baseline.restclient;

import lombok.extern.log4j.Log4j2;
import org.eclipse.jetty.client.HttpRequest;
import org.rizki.mufrizal.baseline.exception.RestClientExceptionHandler;
import org.rizki.mufrizal.baseline.mapper.HelloMapper;
import org.rizki.mufrizal.baseline.mapper.object.client.request.HelloClientRequest;
import org.rizki.mufrizal.baseline.mapper.object.client.response.HelloClientResponse;
import org.rizki.mufrizal.baseline.mapper.object.server.request.HelloServerRequest;
import org.rizki.mufrizal.baseline.mapper.object.server.response.GeneralServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class HelloRestClient {

    @Autowired
    private WebClient webClient;

    @Autowired
    private HelloMapper helloMapper;

    @Autowired
    private Environment environment;

    public Mono<GeneralServerResponse> sayHello(HelloServerRequest helloServerRequest) {
        HelloClientRequest helloClientRequest = helloMapper.toHelloClientRequest(helloServerRequest);
        Mono<HelloClientResponse> helloClientResponseMono = webClient.post()
                .uri(environment.getRequiredProperty("backend.url"))
                .httpRequest(clientHttpRequest -> {
                    HttpRequest httpRequest = clientHttpRequest.getNativeRequest();
                    httpRequest.timeout(Long.parseLong(environment.getRequiredProperty("backend.timeout")), TimeUnit.MILLISECONDS);
                })
                .body(BodyInserters.fromValue(helloClientRequest))
                .headers(httpHeaders -> httpHeaders.setBasicAuth(environment.getRequiredProperty("backend.username"), environment.getRequiredProperty("backend.password")))
                .retrieve()
                .bodyToMono(HelloClientResponse.class)
                .onErrorResume(ex -> new RestClientExceptionHandler<HelloClientResponse>().onErrorResume(ex, HelloClientResponse.class));

        return helloMapper.toHelloServerResponse(helloClientResponseMono);
    }

}