package com.zzl.seckill.vo;

import com.zzl.seckill.domain.OrderInfo;

/**
 * @Author: Zzl
 * @Date: 20:43 2020/4/1
 * @Version 1.0
 **/
public class OrderDetailVo {
    private GoodsVo goods;
    private OrderInfo order;

    public GoodsVo getGoods() {
        return goods;
    }

    public void setGoods(GoodsVo goods) {
        this.goods = goods;
    }

    public OrderInfo getOrder() {
        return order;
    }

    public void setOrder(OrderInfo order) {
        this.order = order;
    }
}
