package com.zzl.seckill.result;

/**
 * @Author: Zzl
 * @Date: 13:01 2020/3/26
 * @Version 1.0
 *
 * <T>是泛型
 **/
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    /**
     * 访问级别设置为private，只能通过静态属性调用构造函数，
     * 而不希望直接被外界调用。
     *
     * @param data
     */
    private Result(T data) {
        this.code = 0;
        this.msg = "success";
        this.data = data;
    }

    /**
     * @param cm 错误编码
     */
    private Result(CodeMsg cm){
        if(cm == null){
            return;
        }
        this.code = cm.getCode();
        this.msg = cm.getMsg();
    }

    /**
     * 静态方法，成功的时候调用
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T> success(T data){
        return new Result<T>(data);
    }

    /**
     * 静态方法，失败的时候调用
     * @param cm
     * @param <T>
     * @return
     */
    public static <T> Result<T> error(CodeMsg cm){
        return new Result<T>(cm);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}
