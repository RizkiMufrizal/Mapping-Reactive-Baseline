package org.rizki.mufrizal.baseline.configuration;

import lombok.extern.log4j.Log4j2;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.dynamic.HttpClientTransportDynamic;
import org.eclipse.jetty.io.ClientConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
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
    public WebClient jettyWebClient() {
        SslContextFactory.Client sslContextFactory = new SslContextFactory.Client();
        sslContextFactory.setTrustAll(true);
        sslContextFactory.setEndpointIdentificationAlgorithm("HTTPS");
        ClientConnector clientConnector = new ClientConnector();
        clientConnector.setSslContextFactory(sslContextFactory);
        QueuedThreadPool queuedThreadPool = new QueuedThreadPool();
        queuedThreadPool.setMaxThreads(100);
        queuedThreadPool.setMinThreads(5);

        HttpClient httpClient = new HttpClient(new HttpClientTransportDynamic(clientConnector)) {
            @Override
            public Request newRequest(URI uri) {
                Request request = super.newRequest(uri);
                return logging(request);
            }
        };
        httpClient.setExecutor(queuedThreadPool);
        httpClient.setMaxConnectionsPerDestination(50);
        httpClient.setMaxRequestsQueuedPerDestination(50);
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString())
                .clientConnector(new JettyClientHttpConnector(httpClient))
                .build();
    }

    private Request logging(Request inboundRequest) {
        StringBuilder logging = new StringBuilder();
        // Request Logging
        inboundRequest.onRequestBegin(request ->
                logging.append("Request Url ").append(request.getURI())
                        .append("\n")
                        .append("Request Method ").append(request.getMethod())
                        .append("\n"));
        inboundRequest.onRequestHeaders(request -> {
            logging.append("Request Headers:\n");
            request.getHeaders().stream().parallel().forEach(header -> logging.append("\t").append(header.getName()).append(" : ").append(header.getValue()).append("\n"));
        });
        inboundRequest.onRequestContent((request, content) -> {
            var bufferAsString = StandardCharsets.UTF_8.decode(content).toString();
            logging.append("Request Body ").append(bufferAsString)
                    .append("\n");
        });

        // Response Logging
        inboundRequest.onResponseBegin(response ->
                logging.append("Response Http Status ").append(response.getStatus())
                        .append("\n")
                        .append("Response Http Message ").append(response.getReason())
                        .append("\n")
                        .append("Response Http Version ").append(response.getVersion())
                        .append("\n")
        );
        inboundRequest.onResponseHeaders(response -> {
            logging.append("Response Headers:\n");
            response.getHeaders().stream().parallel().forEach(header -> logging.append("\t").append(header.getName()).append(" : ").append(header.getValue()).append("\n"));
        });
        inboundRequest.onResponseContent(((response, content) -> {
            var bufferAsString = StandardCharsets.UTF_8.decode(content).toString();
            logging.append("Response Body ").append(bufferAsString);
        }));

        inboundRequest.onRequestSuccess(request -> {
            log.info("====================Rest Client Begin====================");
            log.info(logging.toString());
        });
        inboundRequest.onResponseSuccess(response -> {
            log.info(logging.toString());
            log.info("====================Rest Client End====================");
        });

        return inboundRequest;
    }

}