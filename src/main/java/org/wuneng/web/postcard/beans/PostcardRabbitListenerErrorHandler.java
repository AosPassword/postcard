package org.wuneng.web.postcard.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.stereotype.Component;

public class PostcardRabbitListenerErrorHandler implements RabbitListenerErrorHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public Object handleError(Message message, org.springframework.messaging.Message<?> message1, ListenerExecutionFailedException e) throws Exception {
        logger.error(message.toString()+e.toString());
        return message;
    }
}
