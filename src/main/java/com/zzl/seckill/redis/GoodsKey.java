package com.zzl.seckill.redis;

import com.zzl.seckill.domain.Goods;

/**
 * @Author: Zzl
 * @Date: 14:51 2020/4/1
 * @Version 1.0
 **/
public class GoodsKey extends BasePrefix{
    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey getGoodsList = new GoodsKey(60, "goodsList");
    public static GoodsKey getGoodsDetail = new GoodsKey(60, "goodsDetail");
    public static GoodsKey getMiaoshaGoodsStock = new GoodsKey(0, "goodsStock");
}
