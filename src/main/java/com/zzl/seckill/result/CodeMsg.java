package com.zzl.seckill.result;

import com.sun.org.apache.bcel.internal.classfile.Code;

/**
 * 状态信息（成功 / 异常）
 *
 * 用code表示状态码，msg表示该状态的信息
 * @Author: Zzl
 * @Date: 23:54 2020/3/25
 * @Version 1.0
 **/
public class CodeMsg {
    private int code;
    private String msg;

    private CodeMsg(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    /**
     * 通用的成功状态信息，code 为 0， msg 为 "success"
     */
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    /**
     * 通用的错误状态信息, code 为 500100，msg 为  "服务端异常"
     */
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");

    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");

    // 登录模块 5002XX

    public static CodeMsg SESSION_ERROR = new CodeMsg(500210, "Session不存在或者已经消失");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(500211, "登录密码不能为空");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(500212, "手机号码不能为空");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(50213, "手机号格式错误");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500214, "手机号不存在");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
    public static CodeMsg LOGIN_USER_ERROR = new CodeMsg(500216, "手机号和密码没有填写");

    // 商品模块 5003XX

    // 订单模块 5004XX

    // 秒杀模块 5005XX

    public CodeMsg fillArgs(Object...args){
        int code = this.code;
        //关于String.format的详细用法：https://blog.csdn.net/lonely_fireworks/article/details/7962171
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
