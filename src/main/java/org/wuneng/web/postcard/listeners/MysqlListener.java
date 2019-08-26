package org.wuneng.web.postcard.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.wuneng.web.postcard.beans.User;
import org.wuneng.web.postcard.dao.UserMapper;
import org.wuneng.web.postcard.services.ProfilePhotoService;
import org.wuneng.web.postcard.services.RedisService;
import org.wuneng.web.postcard.services.UsersMapperService;

import java.io.DataInput;
import java.io.IOException;

@Component
public class MysqlListener {
    @Autowired
    UsersMapperService usersMapperService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RedisService redisService;


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${mq.config.mysql.update.user.status}",autoDelete = "false"),
            exchange = @Exchange(value = "${mq.config.exchange}",type = ExchangeTypes.TOPIC),
            key = "mysql.update.user.status.*"
    ),errorHandler = "rabbitListenerErrorHandler")
    public void update_user(String message) throws IOException {
        JSONObject jsonObject = new JSONObject(message);
        Integer id = jsonObject.getInt("id");
        User user = objectMapper.readValue(message,User.class);
        usersMapperService.update_user(user);
        redisService.delete_user(id);
    }

//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "${mq.config.mysql.update.user.photo}",autoDelete = "false"),
//            exchange = @Exchange(value = "${mq.config.exchange}",type = ExchangeTypes.TOPIC),
//            key = "mysql.update.user.photo.*"
//    ),errorHandler = "rabbitListenerErrorHandler")
//    public void update_user_photo(String message) {
//        JSONObject jsonObject = new JSONObject(message);
//        Integer id = jsonObject.getInt("id");
//        String file_name = jsonObject.getString("profile_photo");
//        usersMapperService.update_photo(file_name, id);
//        redisService.delete_user(id);
//    }
}
