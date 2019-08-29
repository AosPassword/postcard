package org.wuneng.web.postcard.handlers;

import com.google.protobuf.ByteString;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wuneng.web.postcard.beans.*;
import io.netty.channel.*;
import io.netty.util.ReferenceCountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wuneng.web.postcard.services.FriendService;
import org.wuneng.web.postcard.services.RedisService;
import org.wuneng.web.postcard.services.impl.FriendServiceImpl;
import org.wuneng.web.postcard.services.impl.RedisServiceImpl;
import org.wuneng.web.postcard.utils.Channel2UserMap;
import org.wuneng.web.postcard.utils.MessageFactory;
import org.wuneng.web.postcard.utils.User2ChannelMap;


import javax.annotation.PostConstruct;
import java.util.Set;

@Component
@ChannelHandler.Sharable
public class AddFriendHandler extends ChannelInboundHandlerAdapter {
    @Autowired
    RedisServiceImpl redisServiceImpl;
    @Autowired
    FriendServiceImpl friendServiceImpl;

    private static RedisService redisService;
    private static FriendService friendService;

    private Logger logger = LoggerFactory.getLogger(getClass());
    @PostConstruct
    public void init() {
        friendService = friendServiceImpl;
        redisService = redisServiceImpl;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PostCardMessage.Message message = (PostCardMessage.Message) msg;
        if (message.getSubject().equals(Constant.ADD_FRIEND_REQUEST)) {
            PostCardMessage.Message.Builder result = PostCardMessage.Message.newBuilder();
            if (message.getSendUserId()==message.getAcceptUserId()){
                ctx.writeAndFlush(MessageFactory.getMessage("error",ByteString.copyFromUtf8(Constant.CANT_ADD_YOUR_SELF)));
                ReferenceCountUtil.release(msg);
            }else {
                boolean has_friend = false;
                CheckResult friends_ids = friendService.get_friends_ids(message.getSendUserId());
                if (friends_ids.isSuccess()) {
                    Set<Integer> ids = (Set<Integer>) friends_ids.getPayload();
                    has_friend = ids.contains(message.getAcceptUserId());
                }
                //先判断双方是不是好友，是好友直接返回错误
                if (!has_friend) {
                    boolean is_exist = friendService.has_send_request(message.getSendUserId(), message.getAcceptUserId());
                    //查询你是否已经发送过请求信息
                    if (!is_exist) {
                        boolean has_send_request = friendService.has_send_request(message.getAcceptUserId(), message.getSendUserId());
                        //返回对方是否已经请求添加发送者为好友
                        if (!has_send_request) {
                            friendService.send_add_request(message.getSendUserId(), message.getAcceptUserId());
                            result.setSubject(Constant.SUCCESS);
                            result.setId(message.getId());
                            ctx.writeAndFlush(result.build());
                            Channel channel = User2ChannelMap.getUser2channelMap().get(message.getAcceptUserId());
                            if (channel != null) {
                                channel.writeAndFlush(message);
                            }
                        } else {
                            result.setSubject(Constant.ERROR);
                            result.setPayload(ByteString.copyFromUtf8(Constant.ACCEPT_USER_HAS_SEND_ADD_REQUEST));
                        }
                    } else {
                        result.setSubject(Constant.ERROR);
                        result.setPayload(ByteString.copyFromUtf8(Constant.ADD_REQUEST_HAS_BEEN_SEND));
                    }
                } else {
                    result.setSubject(Constant.ERROR);
                    result.setPayload(ByteString.copyFromUtf8(Constant.HAVE_BEEN_FRIENDS_WITH_ACCEPT_USER));
                }
                ctx.channel().writeAndFlush(result.build());
                ReferenceCountUtil.release(msg);
            }

        } else if (message.getSubject().equals(Constant.ACCEPT_FRIEND_RESPONSE)) {
            PostCardMessage.Message.Builder result = PostCardMessage.Message.newBuilder();
            int send_user_id = Channel2UserMap.getChannel2UserMap().get(ctx.channel());
            //redis会尝试将send user的对应add request删除
            //向mysql更新信息，并且删除redis中保存的双方好友数据
            //并且向accept user的accept response中添加此信息
            //如果消息更新失败，则说明对面并未发送加好友信息
            boolean is_exist = friendService.accept_add_response(send_user_id, message.getAcceptUserId());
            if (is_exist) {
                result.setSubject(Constant.SUCCESS);
                result.setId(message.getId());
                ctx.writeAndFlush(result);
                Channel channel = User2ChannelMap.getUser2channelMap().get(message.getAcceptUserId());
                if (channel != null) {
                    channel.writeAndFlush(message);
                }
            } else {
                result.setSubject(Constant.ERROR);
                result.setPayload(ByteString.copyFromUtf8(Constant.ADD_REQUEST_IS_NOT_EXIST));
            }
            ctx.channel().writeAndFlush(result.build());
            ReferenceCountUtil.release(msg);


        } else if (message.getSubject().equals(Constant.LOG_IN)) {
            //在登陆的时候获得请求好友的消息
            int send_user_id = Channel2UserMap.getChannel2UserMap().get(ctx.channel());
            Set<Integer> request = friendService.get_send_request(send_user_id);
            Set<String> response = redisService.get_accept_response(send_user_id);
            Set<MessageVessel> refuse = friendService.get_refuse(send_user_id);
            if (request != null) {
                for (Integer s : request) {
                    ctx.writeAndFlush(MessageFactory.getMessage(Constant.ADD_FRIEND_REQUEST, s, send_user_id));
                }
            }
            if (response != null) {
                for (String s : response) {
                    ctx.writeAndFlush(MessageFactory.getMessage(Constant.ACCEPT_FRIEND_RESPONSE, Integer.parseInt(s), send_user_id));
                    redisService.delete_accept_response(Integer.parseInt(s),send_user_id);
                }
            }
            if (refuse!=null){
                for (MessageVessel i : refuse){
                    ctx.writeAndFlush(MessageFactory.getMessage(Constant.REFUSE,i.getAccept_user_id(),send_user_id,String.valueOf(i.getId())));
                }
            }
        } else if (message.getSubject().equals(Constant.REFUSE)){
            FriendVessel friendVessel = new FriendVessel(message.getSendUserId(),message.getAcceptUserId());
            int i = friendService.refuse(friendVessel);
            if (i == 1){
                ctx.writeAndFlush(MessageFactory.getMessage(Constant.SUCCESS,message.getId()));
                Channel channel = User2ChannelMap.getUser2channelMap().get(message.getAcceptUserId());
                if (channel!=null){
                    channel.writeAndFlush(MessageFactory.getMessage(message,friendVessel.getId()));
                }
            }
            ReferenceCountUtil.release(msg);
        } else if (message.getSubject().equals(Constant.BEREFUSEED)){
            logger.debug(message.getId());
            friendService.beRefused(Integer.parseInt(message.getId()));
        }else {
            //传递到下一个handler
            super.channelRead(ctx, msg);
        }
    }
}
