package com.zzl.seckill.redis;

/**
 * KeyPrefix应该包含两个属性：<br>
 *     1. prefix 前缀 <br>
 *     2. expireSeconds 过期时间
 *
 * <br><br>
 *
 * 采用模板模式:<br>
 * 接口KeyPrefix  -->  抽象类BasePrefix --> 具体的实现类
 *
 * @Author: Zzl
 * @Date: 11:34 2020/3/28
 * @Version 1.0
 **/
public interface KeyPrefix {

    /**
     * 为了防止不同开发人员覆盖redis原有的key，
     * 在每个key前设置前缀
     * @return key加上前缀
     */
    public String getPrefix();

    /**
     * 返回过期秒数
     *
     * 如果为0，代表永不过期
     * @return 过期秒数
     */
    public int expireSeconds();
}
