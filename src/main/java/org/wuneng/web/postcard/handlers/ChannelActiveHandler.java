package org.wuneng.web.postcard.handlers;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateHandler;
import org.wuneng.web.postcard.beans.PostCardMessage;
import org.wuneng.web.postcard.utils.MessageFactory;


public class ChannelActiveHandler extends ChannelInboundHandlerAdapter {
    private static final PostCardMessage.Message heart = MessageFactory.getHeart();
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateHandler){
            ctx.writeAndFlush(heart).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }
}
