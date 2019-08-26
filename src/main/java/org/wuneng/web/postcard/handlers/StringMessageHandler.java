package org.wuneng.web.postcard.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wuneng.web.postcard.beans.Constant;
import org.wuneng.web.postcard.beans.MessageVessel;
import org.wuneng.web.postcard.beans.PostCardMessage;
import org.wuneng.web.postcard.services.MessageService;
import org.wuneng.web.postcard.services.impl.MessageServiceImpl;
import org.wuneng.web.postcard.utils.MessageFactory;
import org.wuneng.web.postcard.utils.User2ChannelMap;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@ChannelHandler.Sharable
public class StringMessageHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    MessageServiceImpl messageServiceImpl;

    private static MessageService messageService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void init() {
        messageService = messageServiceImpl;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PostCardMessage.Message message = (PostCardMessage.Message) msg;
        PostCardMessage.Message result;
        if (message.getSubject().equals(Constant.STRING_MESSAGE)){
            Integer accept = message.getAcceptUserId();
            String payload = message.getPayload().toStringUtf8();
            logger.debug("accept user:"+accept+"payload:"+payload);
            //判断收件人和负载是否为空,负载是否超出长度
            if (accept!=0&&(payload.length()<=255)&&!payload.isEmpty()){
                logger.debug("msg accept success");
                result = MessageFactory.getMessage(Constant.SUCCESS, message.getId());
                logger.debug(result.getId());
                ctx.channel().writeAndFlush(result);
                MessageVessel messageVessel = new MessageVessel(message);
                logger.debug(messageVessel.toString());
                messageService.insert_message(messageVessel);
                logger.debug("message id->"+messageVessel.getId());
                PostCardMessage.Message m = MessageFactory.getMessage(message,messageVessel.getId());
                logger.debug("message id->"+m.getId());
                ReferenceCountUtil.release(message);
                Channel channel = User2ChannelMap.getUser2channelMap().get(accept);
                if (channel!=null){
                    channel.writeAndFlush(m);
                }
                super.channelRead(ctx,m);
            }else {
                result = MessageFactory.getMessage(Constant.ERROR,message.getId());
                ctx.writeAndFlush(result);
                ReferenceCountUtil.release(message);
            }
        }else if (message.getSubject().equals(Constant.LOG_IN)){
            logger.debug("log in to get message");
            logger.debug("sendUserId->"+message.getSendUserId());
            List<MessageVessel> messageVessels = messageService.get_unaccepted_message(message.getSendUserId());
            if (!messageVessels.isEmpty()) {
                for (MessageVessel messageVessel : messageVessels) {
                    logger.debug(messageVessel.toString());
                    ctx.writeAndFlush(MessageFactory.getMessage(messageVessel));
                }
            }
            super.channelRead(ctx,message);
        }else if (message.getSubject().equals(Constant.SUCCESS)){
            messageService.accept(Integer.parseInt(message.getId()));
        }else {
            super.channelRead(ctx,msg);
        }
    }
}
