package com.zzl.seckill.exception;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.zzl.seckill.result.CodeMsg;

/**
 * @Author: Zzl
 * @Date: 17:11 2020/3/29
 * @Version 1.0
 **/
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm(){
        return cm;
    }
}
