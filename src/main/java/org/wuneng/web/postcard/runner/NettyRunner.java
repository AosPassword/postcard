package org.wuneng.web.postcard.runner;

import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.wuneng.web.postcard.services.NettyService;

import java.net.InetSocketAddress;

@Component
public class NettyRunner implements CommandLineRunner {

    @Value("${netty.config.port}")
    private int port;

    @Value("${netty.config.url}")
    private String url;

    @Autowired
    NettyService nettyService;

    @Override
    public void run(String... strings) {
        InetSocketAddress address = new InetSocketAddress(url, port);
        ChannelFuture future = nettyService.run(address);
    }
}
