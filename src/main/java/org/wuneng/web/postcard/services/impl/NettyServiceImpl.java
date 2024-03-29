package org.wuneng.web.postcard.services.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.beans.PostCardMessage;
import org.wuneng.web.postcard.handlers.ServerChannelInitializer;
import org.wuneng.web.postcard.services.NettyService;

import java.net.InetSocketAddress;

@Service
public class NettyServiceImpl implements NettyService {
    private  Logger log = LoggerFactory.getLogger(getClass());

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Channel channel;

    /**
     * 启动服务
     */
    @Override
    public ChannelFuture run(InetSocketAddress address) {
        ChannelFuture f = null;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).handler(new LoggingHandler())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ServerChannelInitializer(PostCardMessage.Message.getDefaultInstance()))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            f = b.bind(address ).sync();
            channel = f.channel();
        } catch (Exception e) {
            log.error("Netty start error:", e);
        } finally {
            if (f != null && f.isSuccess()) {
                log.info("Netty server listening " + address.getHostName() + " on port " + address.getPort() + " and ready for connections...");
            } else {
                log.error("Netty server start up Error!");
            }
        }
        return f;
    }

    @Override
    public void destroy() {
        log.info("Shutdown Netty Server...");
        if(channel != null) { channel.close();}
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("Shutdown Netty Server Success!");
    }
}
