package org.wuneng.web.postcard.listeners;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.io.IOException;
import java.util.logging.Logger;

public class ErrorLogListener {
    private Logger logger = (Logger) LoggerFactory.getLogger(ErrorLogListener.class);

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${mq.config.log.error}",autoDelete = "false"),
            exchange = @Exchange(value = "${mq.config.exchange}",type = ExchangeTypes.TOPIC),
            key = "*.error"
    ),errorHandler = "rabbitListenerErrorHandler")
    public void update_user(String message) throws IOException {

    }
}
