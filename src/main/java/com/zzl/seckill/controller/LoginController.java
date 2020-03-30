package com.zzl.seckill.controller;

import com.zzl.seckill.result.Result;
import com.zzl.seckill.service.MiaoshaUserService;
import com.zzl.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * @Author: Zzl
 * @Date: 21:06 2020/3/28
 * @Version 1.0
 **/
@Controller
@RequestMapping("/login")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private MiaoshaUserService userService;

    /**
     * 返回登录页面
     * @return 登录页面
     */
    @RequestMapping("to_login")
    public String toLogin() {
        return "login";
    }

    /**
     * 登录判断
     * @param response
     * @param loginVo 登录类，包括用户名、密码
     * @return 如果用户名、密码正确，返回“成功码”
     */
    @RequestMapping("do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        log.info(loginVo.toString());
        //登录
        userService.login(response, loginVo);
        return Result.success(true);
    }
}
