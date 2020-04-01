package com.zzl.seckill.redis;

/**
 * @Author: Zzl
 * @Date: 21:55 2020/4/1
 * @Version 1.0
 **/
public class OrderKey extends BasePrefix {
    public OrderKey(String prefix){
        super(prefix);
    }

    public static OrderKey getMiaoshaOrderByUidGid = new OrderKey("moug");
}
