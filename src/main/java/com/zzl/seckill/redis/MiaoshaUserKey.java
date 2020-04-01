package com.zzl.seckill.redis;

/**
 * @Author: Zzl
 * @Date: 18:58 2020/3/29
 * @Version 1.0
 **/
public class MiaoshaUserKey extends BasePrefix {

    /**
     * TOKEN过期时间为2天
     */
    public static final int TOKEN_EXPIRE = 3600 * 24 * 2;
    public MiaoshaUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }


    public static MiaoshaUserKey token = new MiaoshaUserKey(TOKEN_EXPIRE, "token");
    public static KeyPrefix getById = new MiaoshaUserKey(0, "id");
}
