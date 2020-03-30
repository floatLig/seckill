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
import org.springframework.web.bind.annotation.PathVariable;
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

    /**
     * 根据前端传过来的商品id，用户信息，
     * 返回 用户信息，商品信息，秒杀的状态，秒杀的结束时间 到前端
     * @param user 前端传过来的用户信息
     * @param goodsId 前端传过来的商品id
     * @return
     */
    @RequestMapping("/to_detail/{goodsId}")
    public String detail(Model model, MiaoshaUser user,
                         @PathVariable("goodsId") long goodsId){
        model.addAttribute("user", user);

        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        model.addAttribute("goods", goods);

        long startAt = goods.getStartDate().getTime();
        long endAt = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds = 0;
        if(now < startAt){
            //秒杀还没有开始
            miaoshaStatus = 0;
            remainSeconds = (int)((startAt - now) / 1000);
        }else if(now > endAt){
            //秒杀已结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{
            //秒杀进行中
            miaoshaStatus = 1;
            remainSeconds = 0;
        }

        model.addAttribute("miaoshaStatus", miaoshaStatus);
        model.addAttribute("remainSeconds", remainSeconds);
        // return "hello";
        return "goods_detail";
    }
}
