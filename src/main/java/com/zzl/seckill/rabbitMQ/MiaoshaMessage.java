package com.zzl.seckill.rabbitMQ;

import com.zzl.seckill.domain.MiaoshaUser;

/**
 * @Author: Zzl
 * @Date: 20:14 2020/4/3
 * @Version 1.0
 **/
public class MiaoshaMessage {
    private MiaoshaUser user;
    private long goodsId;

    public MiaoshaUser getUser() {
        return user;
    }

    public void setUser(MiaoshaUser user) {
        this.user = user;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
}
