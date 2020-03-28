package com.zzl.seckill.redis;

/**
 * @Author: Zzl
 * @Date: 11:56 2020/3/28
 * @Version 1.0
 **/
public class UserKey extends BasePrefix {
    /**
     * 创建永不过期的userKey
     * @param prefix 前缀
     */
    public UserKey(String prefix) {
        super(prefix);
    }

    public static UserKey getById = new UserKey("id");
    public static UserKey getByName = new UserKey("name");
}
