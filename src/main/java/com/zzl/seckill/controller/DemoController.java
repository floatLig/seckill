package com.zzl.seckill.controller;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zzl.seckill.domain.User;
import com.zzl.seckill.result.CodeMsg;
import com.zzl.seckill.result.Result;
import com.zzl.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.ResultSet;

/**
 * @Author: Zzl
 * @Date: 21:53 2020/3/25
 * @Version 1.0
 **/
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    UserService userService;

    @RequestMapping("/")
    @ResponseBody
    String home(){
        System.out.println("hello world");
        return "hello world!";
    }

    // 1. rest api json 输出

    @RequestMapping("/hello")
    @ResponseBody
    public Result<String> hello(){
        //控制台输出的对象的哈希地址
        System.out.println(Result.success("hello, seckill"));
        //浏览器端输出的对象的json
        return Result.success("hello, seckill");
    }

    @RequestMapping("/helloError")
    @ResponseBody
    public Result<String> helloError(){
        //控制台输出的对象的哈希地址
        System.out.println(Result.error(CodeMsg.SERVER_ERROR));
        //浏览器端输出的对象的json
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    // 2. 页面

    @RequestMapping("/thymeleaf")
    public String thymeleaf(Model model){
        System.out.println("hello world");
        model.addAttribute("name", "Zzl");
        return "hello";
    }

    @RequestMapping("db/get")
    @ResponseBody
    public Result<User> deGet(){
        User user = userService.getById(1);
        return Result.success(user);
    }

    @RequestMapping("db/tx")
    @ResponseBody
    public Result<Boolean> dbTx(){
        userService.tx();
        return Result.success(true);
    }
}
