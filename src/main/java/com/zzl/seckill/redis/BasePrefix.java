package com.zzl.seckill.redis;

import com.fasterxml.jackson.databind.ser.Serializers;

/**
 * @Author: Zzl
 * @Date: 11:51 2020/3/28
 * @Version 1.0
 **/
public abstract class BasePrefix implements KeyPrefix{

    private int expireSeconds;

    private String prefix;

    /**
     * 创建永不过期的prefix
     * @param prefix 前缀
     */
    public BasePrefix(String prefix){
        this(0, prefix);
    }

    public BasePrefix(int expireSeconds, String prefix){
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSeconds() {
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }
}
