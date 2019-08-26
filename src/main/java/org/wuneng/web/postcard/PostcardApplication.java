package org.wuneng.web.postcard;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@EnableRabbit
@SpringBootApplication
public class PostcardApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostcardApplication.class, args);
    }

}
