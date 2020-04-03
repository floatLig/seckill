package com.zzl.seckill.service;

import com.zzl.seckill.domain.MiaoshaOrder;
import com.zzl.seckill.domain.MiaoshaUser;
import com.zzl.seckill.domain.OrderInfo;
import com.zzl.seckill.redis.MiaoshaKey;
import com.zzl.seckill.redis.RedisService;
import com.zzl.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Zzl
 * @Date: 23:55 2020/3/30
 * @Version 1.0
 **/
@Service
public class MiaoshaService {

    @Autowired
    GoodsService goodsService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Transactional
    public OrderInfo miaosha(MiaoshaUser user, GoodsVo goods){
        //减库存 下订单 写入秒杀订单
        boolean success = goodsService.reduceStock(goods);
        if(success){
            return orderService.createOrder(user, goods);
        }else {
            setGoodsOver(goods.getId());
            return null;
        }
    }

    /**
     * 判断之前是否已经秒杀过了
     */
    public long getMiaoshaResult(Long userId, long goodsId){
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(userId, goodsId);
        //秒杀成功
        if(order != null){
            return order.getOrderId();
        }else{
            boolean isOver = getGoodsOver(goodsId);
            if(isOver){
                return -1;
            }else {
                //让客户端继续轮询，抢秒杀
                return 0;
            }
        }
    }

    private void setGoodsOver(Long goodsId){
        redisService.set(MiaoshaKey.isGoodsOver, "" + goodsId, true);
    }

    private boolean getGoodsOver(long goodsId){
        return redisService.exists(MiaoshaKey.isGoodsOver, "" + goodsId);
    }

    public void reset(List<GoodsVo> goodsList) {
        goodsService.resetStock(goodsList);
        orderService.deleteOrders();
    }
}
