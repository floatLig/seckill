package com.zzl.seckill.controller;

import com.zzl.seckill.domain.MiaoshaOrder;
import com.zzl.seckill.domain.MiaoshaUser;
import com.zzl.seckill.domain.OrderInfo;
import com.zzl.seckill.redis.RedisService;
import com.zzl.seckill.result.CodeMsg;
import com.zzl.seckill.result.Result;
import com.zzl.seckill.service.GoodsService;
import com.zzl.seckill.service.MiaoshaService;
import com.zzl.seckill.service.MiaoshaUserService;
import com.zzl.seckill.service.OrderService;
import com.zzl.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: Zzl
 * @Date: 23:20 2020/3/30
 * @Version 1.0
 **/
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController {

    @Autowired
    MiaoshaUserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;

    @RequestMapping(value = "do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<OrderInfo> list(Model model, MiaoshaUser user,
                                  @RequestParam("goodsId") long goodsId){
        if(user == null){
            // return "login";
            //不再是返回整个页面，而是返回数据对象
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        //判断库存
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        int stock = goods.getStockCount();
        if(stock <= 0){
            // model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
            // return "miaosha_fail";

            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //防止重复秒杀
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if(order != null){
            // model.addAttribute("errmsg", CodeMsg.REPEAT_MIAOSHA.getMsg());
            // return "miaosha_fail";
            return Result.error(CodeMsg.REPEAT_MIAOSHA);
        }
        //减库存 下订单 写入秒杀订单
        //根据用户，商品创建秒杀订单
        OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        // model.addAttribute("orderInfo", orderInfo);
        // model.addAttribute("goods", goods);
        // return "order_detail";
        return Result.success(orderInfo);
    }
}
