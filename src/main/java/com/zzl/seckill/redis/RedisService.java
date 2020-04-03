package com.zzl.seckill.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zzl
 * @Date: 11:32 2020/3/28
 * @Version 1.0
 **/
@Service
public class RedisService {

    @Autowired
    JedisPool jedisPool;

    /**
     * 通过key，从redis获取对应的值，并转化为相应的对象
     * @param <T> 泛型
     * @return 返回key对应的对象
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T>  clazz){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            //从redis中查到readKey的值
            String str = jedis.get(realKey);
            T t =  stringToBean(str, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 将json格式的字符串转化成Java对象
     * @param str json格式的字符串
     * @param clazz Java对象的类型
     * @param <T> 泛型
     * @return Java对象
     */
    @SuppressWarnings("unchecked")
    public static  <T> T stringToBean(String str, Class<T> clazz) {
        if(str == null || str.length() <= 0 || clazz == null){
            return null;
        }
        if(clazz == int.class || clazz == Integer.class){
            return (T) Integer.valueOf(str);
        }else if(clazz == long.class || clazz == Long.class){
            return (T) Long.valueOf(str);
        }else if(clazz == String.class){
            return (T) str;
        }else{
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    /**
     * 关闭jedis
     * @param jedis Jedis对象
     */
    private void returnToPool(Jedis jedis) {
        if(jedis != null){
            jedis.close();
        }
    }

    /**
     * 将键值对存入Redis
     *
     * @param prefix 前缀，为了提高可读性和防止重复，在每个key前面加上标识符
     * @param key key
     * @param value key对应的value
     * @return
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value){
        //需要连接redis
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(str == null || str.length() == 0){
                return false;
            }
            //生成真正的key
            String realKey = prefix.getPrefix() + key;

            int secondes = prefix.expireSeconds();
            if(secondes <= 0){
                jedis.set(realKey, str);
            }else{
                jedis.setex(realKey, secondes, str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 任意类型T 转成 字符串
     */
    public static  <T> String beanToString(T value) {
        if(value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class){
            return "" + value;
        }else if(clazz == String.class){
            return (String)value;
        }else if(clazz == long.class || clazz == Long.class){
            return "" + value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    public boolean exists(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            //jedis直接就有方法了
            return jedis.exists(realKey);
        }finally{
            returnToPool(jedis);
        }
    }

    public Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    public Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    public boolean delete(KeyPrefix prefix, String key) {
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            //生成真正的key
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }

    public boolean delete(KeyPrefix prefix) {
        if(prefix == null) {
            return false;
        }
        List<String> keys = scanKeys(prefix.getPrefix());
        if(keys==null || keys.size() <= 0) {
            return true;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.del(keys.toArray(new String[0]));
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if(jedis != null) {
                jedis.close();
            }
        }
    }

    private List<String> scanKeys(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> keys = new ArrayList<String>();
            String cursor = "0";
            ScanParams sp = new ScanParams();
            sp.match("*"+key+"*");
            sp.count(100);
            do{
                ScanResult<String> ret = jedis.scan(cursor, sp);
                List<String> result = ret.getResult();
                if(result!=null && result.size() > 0){
                    keys.addAll(result);
                }
                //再处理cursor
                cursor = ret.getStringCursor();
            }while(!cursor.equals("0"));
            return keys;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
