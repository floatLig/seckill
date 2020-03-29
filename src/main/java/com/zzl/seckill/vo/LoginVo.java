package com.zzl.seckill.vo;

import javax.validation.constraints.NotNull;

/**
 * @Author: Zzl
 * @Date: 21:20 2020/3/28
 * @Version 1.0
 **/
public class LoginVo {

    private String mobile;

    private String password;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
