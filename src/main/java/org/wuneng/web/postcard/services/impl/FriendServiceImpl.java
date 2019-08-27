package org.wuneng.web.postcard.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.wuneng.web.postcard.beans.CheckResult;
import org.wuneng.web.postcard.beans.FriendVessel;
import org.wuneng.web.postcard.beans.MessageVessel;
import org.wuneng.web.postcard.dao.FriendsMapper;
import org.wuneng.web.postcard.services.FriendService;
import org.wuneng.web.postcard.services.RedisService;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FriendServiceImpl implements FriendService {

    @Autowired
    FriendsMapper friendsMapper;
    @Autowired
    RedisService redisService;
    @Autowired
    ObjectMapper objectMapper;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public CheckResult get_friends_ids(Integer id)  {
        Set<String> ids = redisService.get_friends(id);
        CheckResult result = new CheckResult();
        Set<Integer> set;
        if (!ids.isEmpty()){
            logger.debug("redis get friends id");
            set = ids.stream().map(s -> Integer.parseInt(s)).collect(Collectors.toSet());
            result.setSuccess(true);
            result.setPayload(set);
        }else {
            logger.debug("mysql get friends id");
            set = friendsMapper.get_friends_ids(id);
            if (!set.isEmpty()) {
                redisService.put_friends(id, set);
                result.setSuccess(true);
                result.setPayload(set);
            }else {
                result.setSuccess(false);
            }
        }
        return result;
    }

    @Override
    public boolean send_add_request(Integer send, Integer accept){
        int i = friendsMapper.send_add_request(send, accept);
        if (i==1){
            redisService.delete_add_request(accept);
        }
        return true;
    }

    @Override
    public boolean accept_add_response(Integer send_user_id, Integer accept_user_id) {
        //将数据库的加好友请求设置为已经接受，如果加好友请求不存在则返回0
        int i = friendsMapper.accept_add_response(send_user_id, accept_user_id);
        if (i == 1) {
            //删除接收者名下的请求消息
            redisService.delete_add_request(accept_user_id,send_user_id);
            logger.debug("redis delete add request!");
            logger.debug("request send user->"+accept_user_id);
            logger.debug("request accept user->"+send_user_id);
            //删除双方好友缓存
            redisService.delete_friends(accept_user_id);
            redisService.delete_friends(send_user_id);
            redisService.put_accept_response(send_user_id,accept_user_id);
            return true;
        }
        return false;
    }

    @Override
    public boolean has_send_request(int sendUserId, int acceptUserId) {
        Set<Integer> ids = get_send_request(acceptUserId);
        if (ids.contains(sendUserId)) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public Set<Integer> get_send_request(int id) {
        Set<String> accept_responses = redisService.get_add_request(id);
        Set<Integer> ids;
        if (!accept_responses.isEmpty()){
           ids = accept_responses.stream().map(s ->Integer.parseInt(s)).collect(Collectors.toSet());
        }else {
            ids = friendsMapper.get_send_request(id);
            if (!ids.isEmpty()){
                for (Integer i : ids){
                    redisService.put_add_request(i,id);
                }
            }
        }
        return  ids;
    }

    @Override
    public int refuse(FriendVessel friendVessel) {
        return friendsMapper.refuse(friendVessel);
    }

    @Override
    public void beRefused(Integer id) {
        friendsMapper.refused(id);
    }

    @Override
    public Set<MessageVessel> get_refuse(int send_user_id) {
        return friendsMapper.get_refuse(send_user_id);
    }

    @Override
    public Integer delete_friend(int send_user_id, int accept_user_id){
        return friendsMapper.delete(send_user_id,accept_user_id);
    }


}
