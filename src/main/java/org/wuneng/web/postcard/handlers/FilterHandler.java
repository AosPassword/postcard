package org.wuneng.web.postcard.handlers;

import com.google.protobuf.ByteString;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wuneng.web.postcard.beans.Constant;
import org.wuneng.web.postcard.beans.PostCardMessage;
import org.wuneng.web.postcard.services.BloomFilterService;
import org.wuneng.web.postcard.services.impl.BloomFilterServiceImpl;
import org.wuneng.web.postcard.utils.Channel2UserMap;
import org.wuneng.web.postcard.utils.DateUtil;
import org.wuneng.web.postcard.utils.MessageFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.sql.Timestamp;

@Component
@ChannelHandler.Sharable
public class FilterHandler extends ChannelInboundHandlerAdapter {
    @Resource
    BloomFilterServiceImpl bloomFilterService;

    private static BloomFilterService filterService;

    @PostConstruct
    public void init() {
        filterService = bloomFilterService;
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PostCardMessage.Message message = (PostCardMessage.Message) msg;
        Integer send_user_id = Channel2UserMap.getChannel2UserMap().get(ctx.channel());
        if (send_user_id != 0) {
            int accept_user_id = message.getAcceptUserId();
            //查看map中是否包含这个channel
            if (accept_user_id != 0) {
                logger.debug("accept user id:" + accept_user_id);
                //查看此accept user id是否合法
                if (!filterService.id_might_constain(accept_user_id)) {
                    PostCardMessage.Message result = MessageFactory.getMessage(Constant.ERROR,
                            ByteString.copyFromUtf8(Constant.ACCEPT_USER_IS_NOT_EXIST));
                    ctx.channel().writeAndFlush(result);
                    ReferenceCountUtil.release(msg);
                }
            }
            PostCardMessage.Message decoration = MessageFactory.getMessage(message, send_user_id, DateUtil.get_stamp());
            super.channelRead(ctx, decoration);
            ReferenceCountUtil.release(msg);
        } else {
            //不包含说明没法登陆信息
            PostCardMessage.Message result = MessageFactory.getMessage(Constant.ERROR,
                    ByteString.copyFromUtf8(Constant.PLZ_LOG_IN_FIRST));
            ctx.channel().writeAndFlush(result);
            ReferenceCountUtil.release(msg);
        }
    }
}
