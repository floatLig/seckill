package com.zzl.seckill.rabbitMQ;

import com.zzl.seckill.domain.Goods;
import com.zzl.seckill.domain.MiaoshaOrder;
import com.zzl.seckill.domain.MiaoshaUser;
import com.zzl.seckill.redis.OrderKey;
import com.zzl.seckill.redis.RedisService;
import com.zzl.seckill.service.GoodsService;
import com.zzl.seckill.service.MiaoshaService;
import com.zzl.seckill.service.OrderService;
import com.zzl.seckill.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.stereotype.Service;

/**
 * @Author: Zzl
 * @Date: 13:33 2020/4/3
 * @Version 1.0
 **/
@Service
public class MQReceiver {

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @Autowired
    GoodsService goodsService;

    private static Logger logger = LoggerFactory.getLogger(MQReceiver.class);

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message){
        logger.info("receive message: " + message);
        MiaoshaMessage mm = RedisService.stringToBean(message, MiaoshaMessage.class);
        MiaoshaUser user = mm.getUser();
        long goodsId = mm.getGoodsId();

        //数据库真实的库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0){
            return;
        }
        //判断是否已经秒杀到了
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null){
            return;
        }
        //减库存 下订单 写入秒杀系统
        miaoshaService.miaosha(user, goods);
    }


    // @RabbitListener(queues = MQConfig.QUEUE)
    // public void receive(String message){
    //     logger.info("receive message: " + message);
    // }
    //
    // @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    // public void receiveTopicQueue1(String message){
    //     logger.info("topicQueue1 receive message: " + message);
    // }
    //
    // @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    // public void receiveTopicQueue2(String message){
    //     logger.info("topicQueue2 receive message: " + message);
    // }
    //
    // @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    // public void receiveHeaderQueue(byte[] message){
    //     logger.info("headerQueue receive message: " + new String(message));
    // }


}
