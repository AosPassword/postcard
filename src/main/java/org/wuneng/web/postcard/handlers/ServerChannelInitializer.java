package org.wuneng.web.postcard.handlers;

import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;


public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final MessageLite lite;

    public ServerChannelInitializer(MessageLite message) {
        this.lite = message;
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new ProtobufVarint32FrameDecoder())
                .addLast(new ProtobufVarint32LengthFieldPrepender())
                .addLast(new ProtobufEncoder())
                .addLast(new ProtobufDecoder(lite))
                .addLast(new IdleStateHandler(0,0,10))
                .addLast("is_active",new ChannelActiveHandler())
                .addLast("log",new LogHandler())
                .addLast("filter",new FilterHandler())
                .addLast("add",new AddFriendHandler())
                .addLast("str",new StringMessageHandler())
                .addLast(new ErrorHandler());
    }
}
