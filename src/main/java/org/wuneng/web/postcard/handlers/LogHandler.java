package org.wuneng.web.postcard.handlers;

import com.google.protobuf.ByteString;
import io.jsonwebtoken.Claims;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.wuneng.web.postcard.beans.CheckResult;
import org.wuneng.web.postcard.beans.Constant;

import org.wuneng.web.postcard.beans.PostCardMessage;
import org.wuneng.web.postcard.services.FriendService;
import org.wuneng.web.postcard.services.impl.FriendServiceImpl;
import org.wuneng.web.postcard.utils.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Set;

@Component
@ChannelHandler.Sharable
public class LogHandler extends ChannelInboundHandlerAdapter {
    @Resource
    FriendServiceImpl friendServiceImpl;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private static FriendService friendService;

    @PostConstruct
    public void init() {
        friendService = friendServiceImpl;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(MessageFactory.getMessage(Constant.SUCCESS,ByteString.copyFromUtf8("hello")));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.debug("LogHandler channelRead");
        PostCardMessage.Message message = (PostCardMessage.Message) msg;
        if (message!=null){
            ReferenceCountUtil.release(msg);
        }else {
            //判断主题是否是登陆
            if (message.getSubject().equals(Constant.LOG_IN)) {
                String token = message.getPayload().toStringUtf8();
                CheckResult checkResult = JWTUtil.validateJWT(token);
                //判断发送的token是否正确
                if (checkResult.isSuccess()) {
                    Claims claims = (Claims) checkResult.getPayload();
                    //判断token是否是登陆token
                    Integer id = Integer.parseInt(claims.getId());
                    logger.debug(id + "log in");
                    //将channel放入map
                    User2ChannelMap.getUser2channelMap().put(id, ctx.channel());
                    Channel2UserMap.getChannel2UserMap().put(ctx.channel(), id);
                    CheckResult friends_ids = friendService.get_friends_ids(id);
                    //判断是否可以获得其好友信息
                    if (friends_ids.isSuccess()) {
                        Set<Integer> friends = (Set<Integer>) friends_ids.getPayload();
                        PostCardMessage.Message log_in = MessageFactory.getMessage(Constant.LOG_IN, id);
                        //向用户好友发送用户已经登陆的信息 并且 给用户发送好友在线状态
                        Channel channel;
                        for (Integer i : friends) {
                            channel = User2ChannelMap.getUser2channelMap().get(i);
                            if (channel != null) {
                                logger.debug("send log in message to user ->" + i);
                                channel.writeAndFlush(log_in);
                            } else {
                                logger.debug("friend user ->" + i + " is not in channel map");
                                friends.remove(i);
                            }
                        }
                        logger.debug("user:" + id + " friends who is in map are : " + friends.toString());
                        PostCardMessage.Message friend_list = MessageFactory.getMessage(Constant.FRIENDS_LIST, id, ByteString.copyFromUtf8(friends.toString()));
                        ctx.channel().writeAndFlush(friend_list);
                    } else {
                        logger.debug("can't get friend!");
                    }
                    super.channelRead(ctx, msg);
                } else {
                    ctx.writeAndFlush(MessageFactory.getMessage(Constant.ERROR, ByteString.copyFromUtf8(Constant.Log_ERROR)));
                    ReferenceCountUtil.release(msg);
                }
            } else {
                super.channelRead(ctx, msg);
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.debug("LogHandler channelInactive");
        Integer id = Channel2UserMap.getChannel2UserMap().get(ctx.channel());
        logger.debug("user:"+id+" has log out!");
        if (id != null) {
            Channel2UserMap.getChannel2UserMap().remove(ctx.channel());
            User2ChannelMap.getUser2channelMap().remove(id);
            CheckResult friends_ids =friendService.get_friends_ids(id);
            if (friends_ids.isSuccess()) {
                Set<Integer> friends = (Set<Integer>) friends_ids.getPayload();
                PostCardMessage.Message log_out = MessageFactory.getMessage(Constant.LOG_OUT, id);
                //向用户好友发送用户已经登陆的信息并且给用户发送好友在线状态
                Channel channel;
                for (Integer i : friends) {
                    channel = User2ChannelMap.getUser2channelMap().get(i);
                    if (channel != null) {
                        channel.writeAndFlush(log_out);
                        logger.debug(log_out.getSubject());
                        logger.debug(String.valueOf(log_out.getSendUserId()));
                        super.channelInactive(ctx);
                    }
                }
            }
        } else {
            super.channelInactive(ctx);
        }
    }
}
