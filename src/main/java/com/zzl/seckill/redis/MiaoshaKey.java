package com.zzl.seckill.redis;

/**
 * @Author: Zzl
 * @Date: 20:43 2020/4/3
 * @Version 1.0
 **/
public class MiaoshaKey extends BasePrefix {

    private MiaoshaKey(String prefix){
        super(prefix);
    }
    public static MiaoshaKey isGoodsOver = new MiaoshaKey("goodsOver");
}
