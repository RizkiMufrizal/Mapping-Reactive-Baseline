package org.rizki.mufrizal.baseline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rizki.mufrizal.baseline.mapper.object.client.response.HelloClientResponse;
import org.rizki.mufrizal.baseline.mapper.object.server.request.HelloServerRequest;
import org.rizki.mufrizal.baseline.mapper.object.server.response.HelloServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MappingReactiveBaselineApplicationTests {

    @Autowired
    ApplicationContext context;

    private WebTestClient webTestClient;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(8089));
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    public static void shutdown() {
        wireMockServer.stop();
    }

    @BeforeEach
    public void setupMock() throws JsonProcessingException {
        webTestClient = WebTestClient
                .bindToApplicationContext(context)
                .configureClient()
                .build();

        HelloClientResponse helloClientResponse = HelloClientResponse.builder()
                .message("Hello Test")
                .referceNumber(UUID.randomUUID().toString())
                .code("200")
                .description("Success")
                .hash(UUID.randomUUID().toString())
                .build();

        WireMock.stubFor(WireMock.post(WireMock.urlEqualTo("/api/test"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(helloClientResponse))));
    }

    @Test
    void helloController() {
        HelloServerRequest helloServerRequest = new HelloServerRequest();
        helloServerRequest.setMessage("Hello Test");
        helloServerRequest.setReferceNumber(UUID.randomUUID().toString());

        webTestClient
                .post().uri("/api/hello")
                .body(BodyInserters.fromValue(helloServerRequest))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(HelloServerResponse.class).value(hello -> assertThat(hello.getMessage()).isEqualTo("Hello Test"));
    }

}
