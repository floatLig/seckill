package com.zzl.seckill.rabbitMQ;

import com.zzl.seckill.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

/**
 * @Author: Zzl
 * @Date: 13:15 2020/4/3
 * @Version 1.0
 **/
@Service
public class MQSender {

    private static Logger logger = LoggerFactory.getLogger(MQSender.class);

    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoshaMessage(MiaoshaMessage miaoshaMessage){
        String msg = RedisService.beanToString(miaoshaMessage);
        logger.info("send miaosha message: " + msg);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
    }



    //
    // public void send(Object message){
    //     String msg = redisService.beanToString(message);
    //     logger.info("send message:" + msg);
    //     amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
    // }
    //
    // public void sendTopic(Object message){
    //     String msg = redisService.beanToString(message);
    //     logger.info("send topic message: " + msg);
    //     //发送消息需要“交换机名称” - “routingKey” - “发送的消息”
    //     amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.Key1", msg + " /routingKey:topic.key1");
    //     amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.Key2", msg + " /routingKey:topic.key2");
    //     amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.Fake", msg + " /routingKey:topic.keyFake");
    // }
    //
    // public void sendFanout(Object message){
    //     String msg = redisService.beanToString(message);
    //     logger.info("send fanout message: " + msg);
    //     amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
    // }
    //
    // public void sendHeader(Object message){
    //     String msg = redisService.beanToString(message);
    //     logger.info("send header message:" + msg);
    //     MessageProperties properties = new MessageProperties();
    //     properties.setHeader("header1", "value1");
    //     properties.setHeader("header2", "value2");
    //     Message obj = new Message(msg.getBytes(), properties);
    //     amqpTemplate.convertAndSend(MQConfig.HEADER_QUEUE, "", obj);
    // }
}
