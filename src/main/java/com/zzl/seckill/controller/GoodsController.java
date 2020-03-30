package com.zzl.seckill.controller;

import com.zzl.seckill.domain.MiaoshaUser;
import com.zzl.seckill.redis.RedisService;
import com.zzl.seckill.result.Result;
import com.zzl.seckill.service.MiaoshaUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: Zzl
 * @Date: 20:10 2020/3/29
 * @Version 1.0
 **/
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    /**
     * 返回商品页面
     * @param model
     * @param user 登录的用户
     * @return 商品页面
     */
    @RequestMapping("/to_list")
    public String list(Model model, MiaoshaUser user){
        model.addAttribute("user", user);
        return "goods_list";
    }
}
