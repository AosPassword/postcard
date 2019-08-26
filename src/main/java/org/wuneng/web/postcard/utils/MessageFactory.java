package org.wuneng.web.postcard.utils;

import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import org.wuneng.web.postcard.beans.MessageVessel;
import org.wuneng.web.postcard.beans.PostCardMessage;

public class MessageFactory {

    public static PostCardMessage.Message getMessage(String subject, ByteString payload,
                                                     Integer accept_user_id, Timestamp timestamp){
        PostCardMessage.Message.Builder builder = PostCardMessage.Message.newBuilder();
        builder.setSubject(subject).setPayload(payload).setAcceptUserId(accept_user_id).setTime(timestamp);
        return builder.build();
    }

    public static PostCardMessage.Message getMessage(PostCardMessage.Message message,Integer send_id,Timestamp timestamp){
        PostCardMessage.Message.Builder builder = PostCardMessage.Message.newBuilder();
        builder.setId(message.getId()).setSubject(message.getSubject()).setPayload(message.getPayload())
                .setAcceptUserId(message.getAcceptUserId()).setTime(timestamp)
                .setSendUserId(send_id);
        return builder.build();
    }

    public static PostCardMessage.Message getMessage(PostCardMessage.Message message,int id){
        PostCardMessage.Message.Builder builder = PostCardMessage.Message.newBuilder();
        builder.setId(String.valueOf(id)).setSubject(message.getSubject()).setPayload(message.getPayload())
                .setAcceptUserId(message.getAcceptUserId()).setTime(message.getTime())
                .setSendUserId(message.getSendUserId());
        return builder.build();
    }

    public static PostCardMessage.Message getMessage(String subject,String id){
        PostCardMessage.Message.Builder builder = PostCardMessage.Message.newBuilder();
        builder.setSubject(subject).setId(id);
        return builder.build();
    }

    public static PostCardMessage.Message getMessage(String subject, ByteString payload){
        PostCardMessage.Message.Builder builder = PostCardMessage.Message.newBuilder();
        builder.setSubject(subject).setPayload(payload);
        return builder.build();
    }

    public static PostCardMessage.Message getMessage(String subject, ByteString payload,Timestamp timestamp){
        PostCardMessage.Message.Builder builder = PostCardMessage.Message.newBuilder();
        builder.setSubject(subject).setPayload(payload).setTime(timestamp);
        return builder.build();
    }
    public static PostCardMessage.Message getMessage(String subject,Integer sendUserId,Integer acceptUserId){
        PostCardMessage.Message.Builder builder = PostCardMessage.Message.newBuilder();
        builder.setSubject(subject).setSendUserId(sendUserId).setAcceptUserId(acceptUserId);
        return builder.build();
    }

    public static PostCardMessage.Message getMessage(String subject, Integer sendUserId){
        PostCardMessage.Message.Builder builder = PostCardMessage.Message.newBuilder();
        builder.setSubject(subject).setSendUserId(sendUserId);
        return builder.build();
    }
    public static PostCardMessage.Message getMessage(String subject,Integer acceptUserId,ByteString payload){
        PostCardMessage.Message.Builder builder = PostCardMessage.Message.newBuilder();
        builder.setSubject(subject).setAcceptUserId(acceptUserId).setPayload(payload);
        return builder.build();
    }

    public static PostCardMessage.Message getMessage(MessageVessel messageVessel) {
        PostCardMessage.Message.Builder builder = PostCardMessage.Message.newBuilder();
        builder.setId(String.valueOf(messageVessel.getId())).setSendUserId(messageVessel.getSend_user_id())
                .setAcceptUserId(messageVessel.getAccept_user_id()).setTime(DateUtil.get_stamp_by_time(messageVessel.getTimestamp()));
        if (messageVessel.isIs_string()){
            builder.setPayload(ByteString.copyFromUtf8(messageVessel.getPayload()));
        }
        return builder.build();
    }

    public static PostCardMessage.Message getHeart() {
        PostCardMessage.Message.Builder builder = PostCardMessage.Message.newBuilder();
        builder.setSubject("beat");
        return builder.build();
    }
}
