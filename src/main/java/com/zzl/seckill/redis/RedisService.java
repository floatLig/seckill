package com.zzl.seckill.redis;

import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author: Zzl
 * @Date: 11:32 2020/3/28
 * @Version 1.0
 **/
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    /**
     * 获取当个对象
     * @param <T> 泛型
     * @return 返回获取的对象
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T>  clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            //从redis中查到readKey的值
            String str = jedis.get(realKey);
            T t = stringToBean(str, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    private void returnToPool(Jedis jedis) {
    }

    private <T> T stringToBean(String str, Class<T> clazz) {
        //TODO
        return null;
    }
}
