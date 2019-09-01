package org.wuneng.web.postcard.handlers;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuneng.web.postcard.beans.PostCardMessage;
import org.wuneng.web.postcard.utils.MessageFactory;


public class ChannelActiveHandler extends ChannelInboundHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateHandler){
            logger.debug("userEventTriggered active!");
            ctx.writeAndFlush(MessageFactory.getHeart()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }
}
