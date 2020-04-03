package com.zzl.seckill.controller;

import com.zzl.seckill.domain.MiaoshaUser;
import com.zzl.seckill.redis.GoodsKey;
import com.zzl.seckill.redis.RedisService;
import com.zzl.seckill.result.Result;
import com.zzl.seckill.service.GoodsService;
import com.zzl.seckill.service.MiaoshaUserService;
import com.zzl.seckill.vo.GoodsDetailVo;
import com.zzl.seckill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 返回商品页面
     * @param model
     * @param user 登录的用户
     * @return 商品页面
     */
    @RequestMapping(value = "/to_list", produces = "text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user){
        //通过cookie查询登录的User信息
        model.addAttribute("user", user);
        //先从缓存看看，看看能不能直接从缓存取
        String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        //如果缓存没有，则从数据库里查询商品列表
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        model.addAttribute("goodsList", goodsVoList);
        // return "goods_list";

        //手动渲染
        SpringWebContext context = new SpringWebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);
        html = thymeleafViewResolver.getTemplateEngine().process("goods_list", context);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList, "", html);
        }
        return html;
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

    /**
     * 加了redis缓存
     *
     * 根据前端传过来的商品id，用户信息，
     * 返回 用户信息，商品信息，秒杀的状态，秒杀的结束时间 到前端
     * @param user 前端传过来的用户信息
     * @param goodsId 前端传过来的商品id
     * @return
     */
    @RequestMapping(value = "/to_detail_redis/{goodsId}", produces = "text/html")
    @ResponseBody
    public String detailRedis(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                         @PathVariable("goodsId") long goodsId){
        model.addAttribute("user", user);

        //取缓存
        String html = redisService.get(GoodsKey.getGoodsDetail, "" + goodsId, String.class);
        if(!StringUtils.isEmpty(html)){
            return html;
        }

        //手动渲染
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
        // return "goods_detail";

        SpringWebContext context = new SpringWebContext(request, response,
                request.getServletContext(), request.getLocale(), model.asMap(), applicationContext);

        html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", context);
        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsDetail, "" + goodsId, html);
        }
        return html;
    }

    @RequestMapping(value = "/to_detail_vo/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detailReturnVo(HttpServletRequest request, HttpServletResponse response, Model model, MiaoshaUser user,
                                                @PathVariable("goodsId") long goodsId){
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);

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

        GoodsDetailVo vo = new GoodsDetailVo();
        vo.setGoods(goods);
        vo.setUser(user);
        vo.setRemainSeconds(remainSeconds);
        vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }
}
