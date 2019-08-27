package org.wuneng.web.postcard.listeners;

import org.json.JSONObject;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wuneng.web.postcard.services.UserESSearchService;

import java.io.IOException;

@Component
public class ElasticSearchListener {
    @Autowired
    UserESSearchService userESSearchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${mq.config.es.upsert}",autoDelete = "false"),
            exchange = @Exchange(value = "${mq.config.exchange}",type = ExchangeTypes.TOPIC),
            key = "*.update.user.*"
    ),errorHandler = "rabbitListenerErrorHandler")
    public void upsert_user(String message) throws IOException {
        JSONObject jsonObject = new JSONObject(message);
        Integer id = jsonObject.getInt("id");
        userESSearchService.insert_user(jsonObject,id);
    }


}
