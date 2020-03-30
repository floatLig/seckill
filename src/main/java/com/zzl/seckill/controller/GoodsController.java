package com.zzl.seckill.controller;

import com.zzl.seckill.domain.MiaoshaUser;
import com.zzl.seckill.redis.RedisService;
import com.zzl.seckill.result.Result;
import com.zzl.seckill.service.GoodsService;
import com.zzl.seckill.service.MiaoshaUserService;
import com.zzl.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    @Autowired
    GoodsService goodsService;

    /**
     * 返回商品页面
     * @param model
     * @param user 登录的用户
     * @return 商品页面
     */
    @RequestMapping("/to_list")
    public String list(Model model, MiaoshaUser user){
        //通过cookie查询登录的User信息
        model.addAttribute("user", user);
        //查询商品列表
        List<GoodsVo> goodsVoList = goodsService.listGoodsDao();
        model.addAttribute("goodsList", goodsVoList);
        return "goods_list";
    }
}
