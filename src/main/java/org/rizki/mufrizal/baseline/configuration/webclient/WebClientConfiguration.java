package org.rizki.mufrizal.baseline.configuration.webclient;

import lombok.extern.log4j.Log4j2;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.dynamic.HttpClientTransportDynamic;
import org.eclipse.jetty.http.HttpField;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.JettyClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Configuration
@Log4j2
public class WebClientConfiguration {

    @Bean
    public WebClient createWebClient() {
        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
        sslContextFactory.setTrustAll(true);
        sslContextFactory.setEndpointIdentificationAlgorithm("HTTPS");
        ClientConnector clientConnector = new ClientConnector();
        clientConnector.setSslContextFactory(sslContextFactory);
        HttpClient httpClient = new HttpClient(new HttpClientTransportDynamic(clientConnector)) {
            @Override
            public Request newRequest(URI uri) {
                Request request = super.newRequest(uri);
                return logging(request);
            }
        };

        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString())
                .clientConnector(new JettyClientHttpConnector(httpClient)).build();
    }

    private Request logging(Request inboundRequest) {
        inboundRequest.onRequestBegin(request -> {
            log.info("Request Url {}", request.getURI());
            log.info("Request Method {}", request.getMethod());
        });
        inboundRequest.onRequestHeaders(request -> {
            for (HttpField header : request.getHeaders()) {
                log.info("Header {} : {}", header.getName(), header.getValue());
            }
        });
        inboundRequest.onRequestContent((request, content) -> {
            var bufferAsString = StandardCharsets.UTF_8.decode(content).toString();
            log.info("Request Body {}", bufferAsString);
        });
        inboundRequest.onResponseBegin(response -> {
            log.info("Response Http Status {}", response.getStatus());
            log.info("Response Http Message {}", response.getReason());
            log.info("Response Http Version {}", response.getVersion());
        });
        inboundRequest.onResponseHeaders(response -> {
            for (HttpField header : response.getHeaders()) {
                log.info("Header {} : {}", header.getName(), header.getValue());
            }
        });
        inboundRequest.onResponseContent(((response, content) -> {
            var bufferAsString = StandardCharsets.UTF_8.decode(content).toString();
            log.info("Response Body {}", bufferAsString);
        }));

        return inboundRequest;
    }

}