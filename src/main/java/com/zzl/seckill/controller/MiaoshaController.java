package com.zzl.seckill.controller;

import com.zzl.seckill.domain.MiaoshaUser;
import com.zzl.seckill.rabbitMQ.MQSender;
import com.zzl.seckill.rabbitMQ.MiaoshaMessage;
import com.zzl.seckill.redis.GoodsKey;
import com.zzl.seckill.redis.MiaoshaKey;
import com.zzl.seckill.redis.OrderKey;
import com.zzl.seckill.redis.RedisService;
import com.zzl.seckill.result.CodeMsg;
import com.zzl.seckill.result.Result;
import com.zzl.seckill.service.GoodsService;
import com.zzl.seckill.service.MiaoshaService;
import com.zzl.seckill.service.MiaoshaUserService;
import com.zzl.seckill.service.OrderService;
import com.zzl.seckill.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Zzl
 * @Date: 23:20 2020/3/30
 * @Version 1.0
 **/
@Controller
@RequestMapping("/miaosha")
public class MiaoshaController implements InitializingBean {

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

    @Autowired
    MQSender mqSender;

    private HashMap<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     * 初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        if(goodsVoList == null){
            return;
        }
        for (GoodsVo goodsVo : goodsVoList){
            //把商品信息先存放在缓存中
            redisService.set(GoodsKey.getMiaoshaGoodsStock, "" + goodsVo.getId(), goodsVo.getStockCount());
            //把商品是否秒杀完置为false
            localOverMap.put(goodsVo.getId(), false);
        }
    }

    @RequestMapping(value = "do_miaosha", method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> list(Model model, MiaoshaUser user,
                                  @RequestParam("goodsId") long goodsId){
        if(user == null){
            // return "login";
            //不再是返回整个页面，而是返回数据对象
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //内存标记，减少redis访问. 挡住商品秒杀完，才进来的用户
        boolean over = localOverMap.get(goodsId);
        if(over){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //预减库存， 一开始是10
        long stock = redisService.decr(GoodsKey.getMiaoshaGoodsStock, "" + goodsId);
        if(stock < 0){
            localOverMap.put(goodsId, true);
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }
        //消息队列
        MiaoshaMessage mm = new MiaoshaMessage();
        mm.setUser(user);
        mm.setGoodsId(goodsId);
        mqSender.sendMiaoshaMessage(mm);
        //排队中
        return Result.success(0);


        // //判断库存
        // GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
        // int stock = goods.getStockCount();
        // if(stock <= 0){
        //     // model.addAttribute("errmsg", CodeMsg.MIAO_SHA_OVER.getMsg());
        //     // return "miaosha_fail";
        //
        //     return Result.error(CodeMsg.MIAO_SHA_OVER);
        // }
        // //防止重复秒杀
        // MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdAndGoodsId(user.getId(), goodsId);
        // if(order != null){
        //     // model.addAttribute("errmsg", CodeMsg.REPEAT_MIAOSHA.getMsg());
        //     // return "miaosha_fail";
        //     return Result.error(CodeMsg.REPEAT_MIAOSHA);
        // }
        // //减库存 下订单 写入秒杀订单
        // //根据用户，商品创建秒杀订单
        // OrderInfo orderInfo = miaoshaService.miaosha(user, goods);
        // // model.addAttribute("orderInfo", orderInfo);
        // // model.addAttribute("goods", goods);
        // // return "order_detail";
        // return Result.success(orderInfo);
    }

    @RequestMapping(value = "result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> miaoshaResult(Model model, MiaoshaUser user,
                                      @RequestParam("goodsId") long goodsId){
        model.addAttribute("user", user);
        if(user == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long result = miaoshaService.getMiaoshaResult(user.getId(), goodsId);
        return Result.success(result);
    }


    @RequestMapping(value="/reset", method=RequestMethod.GET)
    @ResponseBody
    public Result<Boolean> reset(Model model) {
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for(GoodsVo goods : goodsList) {
            goods.setStockCount(10);
            redisService.set(GoodsKey.getMiaoshaGoodsStock, ""+goods.getId(), 10);
            localOverMap.put(goods.getId(), false);
        }
        redisService.delete(OrderKey.getMiaoshaOrderByUidGid);
        redisService.delete(MiaoshaKey.isGoodsOver);
        miaoshaService.reset(goodsList);
        return Result.success(true);
    }

}
