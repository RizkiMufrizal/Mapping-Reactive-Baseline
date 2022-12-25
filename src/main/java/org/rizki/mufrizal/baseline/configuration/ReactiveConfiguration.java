package org.rizki.mufrizal.baseline.configuration;

import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class ReactiveConfiguration {

    @Bean
    public NioEventLoopGroup nioEventLoopGroup() {
        return new NioEventLoopGroup(20);
    }

    @Bean
    public NettyReactiveWebServerFactory factory(NioEventLoopGroup nioEventLoopGroup) {
        NettyReactiveWebServerFactory nettyReactiveWebServerFactory = new NettyReactiveWebServerFactory();
        nettyReactiveWebServerFactory.setServerCustomizers(Collections.singletonList(httpServer -> httpServer.runOn(nioEventLoopGroup)));
        return nettyReactiveWebServerFactory;
    }

}