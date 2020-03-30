package com.zzl.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author: Zzl
 * @Date: 17:11 2020/3/28
 * @Version 1.0
 **/
public class MD5Util {
    /**
     * 将传入的字符串进行MD5操作
     * @param src 传入的字符串
     * @return MD5加密后的字符串
     */
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String SALT = "1a2b3c4d";

    /**
     * 浏览器端，将用户输入的密码进行一次MD5操作。（SALT是固定的）
     * @param inputPasswd 用户在浏览器输入的密码
     * @return MD5加密后的密码
     */
    public static String inputPasswdToFormPasswd(String inputPasswd){
        //浏览器端第一次加密
        //有加""和没有加""的结果是不一样的
        String str = "" + SALT.charAt(0) + SALT.charAt(2) + inputPasswd + SALT.charAt(5) + SALT.charAt(4);
        //浏览器端第一次MD5
        return md5(str);
    }

    public static String formPasswdToDbPasswd(String formPasswd, String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPasswd + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /**
     * 浏览器到数据库的两次MD5加密
     * @param inputPasswd 用户输入的密码
     * @param saltDb 服务器上随机的盐
     * @return 两次MD5加密的密码，该加密后的密码会保存在数据库
     */
    public static String inputPasswdToDbPasswd(String inputPasswd, String saltDb){
        String formPass = inputPasswdToFormPasswd(inputPasswd);
        String dbPass = formPasswdToDbPasswd(formPass, saltDb);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(inputPasswdToDbPasswd("111111", "1afa2b3c4d"));
    }

}
