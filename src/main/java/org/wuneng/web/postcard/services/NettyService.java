package org.wuneng.web.postcard.services;

import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;

public interface NettyService {
    ChannelFuture run (InetSocketAddress address);

    void destroy();
}
