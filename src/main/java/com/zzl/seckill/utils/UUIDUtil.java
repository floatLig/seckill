package com.zzl.seckill.utils;

import java.util.UUID;

/**
 * @Author: Zzl
 * @Date: 18:49 2020/3/29
 * @Version 1.0
 **/
public class UUIDUtil {
    public static String uuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
