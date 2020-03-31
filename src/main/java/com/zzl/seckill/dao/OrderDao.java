package com.zzl.seckill.dao;

import com.zzl.seckill.domain.MiaoshaOrder;
import com.zzl.seckill.domain.OrderInfo;
import org.apache.ibatis.annotations.*;

/**
 * @Author: Zzl
 * @Date: 23:48 2020/3/30
 * @Version 1.0
 **/
@Mapper
public interface OrderDao {

    /**
     * 判断一个用户是否已经有秒杀订单了，如果有秒杀订单，说明该用户已经秒杀成功了，不应该再次秒杀
     */
    @Select("select * from miaosha_order where user_id = #{userId} and goods_id = #{goodsId}")
    public MiaoshaOrder getMiaoshaOrderByUserIdAndGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into order_info(user_id, goods_id, goods_name, goods_count, " +
            "goods_price, order_channel, status, create_date) values(" +
            "#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, " +
            "#{goodsPrice}, #{orderChannel}, #{status}, #{createDate} )")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insert(OrderInfo orderInfo);

    @Insert("insert into miaosha_order (user_id, goods_id, order_id) values(#{userId}, #{goodsId}, #{orderId})")
    public void insertMiaoshaOrder(MiaoshaOrder miaoshaOrder);
}
