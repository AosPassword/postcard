package org.wuneng.web.postcard.configs;

import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.wuneng.web.postcard.beans.PostcardRabbitListenerErrorHandler;

@Configuration
public class RabbitMqConfig {
    @Bean
    public RabbitListenerErrorHandler rabbitListenerErrorHandler(){
        return new PostcardRabbitListenerErrorHandler();
    }
}

