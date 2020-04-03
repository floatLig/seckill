package com.zzl.seckill.rabbitMQ;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @Author: Zzl
 * @Date: 13:33 2020/4/3
 * @Version 1.0
 **/
@Service
public class MQReceiver {

    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.QUEUE)
    public void receive(String message){
        logger.info("receive message: " + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void receiveTopicQueue1(String message){
        logger.info("topicQueue1 receive message: " + message);
    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void receiveTopicQueue2(String message){
        logger.info("topicQueue2 receive message: " + message);
    }

    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void receiveHeaderQueue(byte[] message){
        logger.info("headerQueue receive message: " + new String(message));
    }


}
