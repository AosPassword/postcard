package org.wuneng.web.postcard.listeners;

import com.aliyuncs.exceptions.ClientException;
import org.checkerframework.checker.units.qual.A;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wuneng.web.postcard.services.MathService;
import org.wuneng.web.postcard.services.RedisService;
import org.wuneng.web.postcard.services.SMSService;

@Component
public class SMSListener {
    @Autowired
    SMSService smsService;
    @Autowired
    MathService mathService;
    @Autowired
    RedisService redisService;
    @Value("${code_life}")
    Integer code_life;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${mq.config.sms.send_code}",autoDelete = "false"),
            exchange = @Exchange(value = "${mq.config.exchange}",type = ExchangeTypes.TOPIC),
            key = "sms.send.code.*"
    ),errorHandler = "rabbitListenerErrorHandler")
    public void send_code(String message) throws ClientException {
        JSONObject jsonObject = new JSONObject(message);
        long phone_number = jsonObject.getLong("phone_number");
        String code = jsonObject.getString("code");
        String prefix = jsonObject.getString("prefix");
        redisService.put_phone_number(phone_number,code,prefix,code_life);
        logger.debug(code);
        smsService.sendSMS(phone_number,code);
    }
}
