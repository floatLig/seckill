package com.zzl.seckill.result;

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

    // 登录模块 5002XX

    // 商品模块 5003XX

    // 订单模块 5004XX

    // 秒杀模块 5005XX


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
