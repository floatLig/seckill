package com.zzl.seckill.controller;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.sun.org.apache.xpath.internal.operations.Bool;
import com.zzl.seckill.domain.User;
import com.zzl.seckill.rabbitMQ.MQSender;
import com.zzl.seckill.redis.RedisService;
import com.zzl.seckill.redis.UserKey;
import com.zzl.seckill.result.CodeMsg;
import com.zzl.seckill.result.Result;
import com.zzl.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Key;
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

    @Autowired
    RedisService redisService;

    @Autowired
    MQSender mqSender;

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
        //前端通过${name}获取name的值
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

    @RequestMapping("redis/get")
    @ResponseBody
    public Result<User> redisGet(){
        User user = redisService.get(UserKey.getById, "" + 1, User.class);
        return Result.success(user);
    }

    @RequestMapping("redis/set")
    @ResponseBody
    public Result<Boolean> redisSet(){
        User user = new User();
        user.setName("hello redis.set");
        user.setId(100);
        redisService.set(UserKey.getById, "" + 1, user);
        return Result.success(true);
    }

    @RequestMapping("mq")
    @ResponseBody
    public Result<String> mq(){
        mqSender.send("hello RabbitMQ");
        return Result.success("Hello RabbitMQ");
    }

    @RequestMapping("mq/header")
    @ResponseBody
    public Result<String> header(){
        mqSender.sendHeader("hello HeaderExchange");
        return Result.success("Hello HeaderExchange");
    }

    @RequestMapping("mq/fanout")
    @ResponseBody
    public Result<String> fanout(){
        mqSender.sendFanout("hello FanoutExchange");
        return Result.success("Hello FanoutExchange");
    }

    @RequestMapping("mq/topic")
    @ResponseBody
    public Result<String> topic(){
        mqSender.sendTopic("hello TopicExchange");
        return Result.success("Hello TopicExchange");
    }
}
