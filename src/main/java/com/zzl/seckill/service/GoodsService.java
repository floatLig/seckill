package com.zzl.seckill.service;

import com.zzl.seckill.dao.GoodsDao;
import com.zzl.seckill.domain.MiaoshaGoods;
import com.zzl.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Zzl
 * @Date: 17:12 2020/3/30
 * @Version 1.0
 **/
@Service
public class GoodsService {

    @Autowired
    GoodsDao goodsDao;

    public List<GoodsVo> listGoodsDao(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsDao.getGoodsVoByGoodsId(goodsId);
    }

    public void reduceStock(GoodsVo goods){
        MiaoshaGoods g = new MiaoshaGoods();
        //获取秒杀对象
        g.setGoodsId(goods.getId());
        //减库存
        goodsDao.reduceStock(g);
    }
}
