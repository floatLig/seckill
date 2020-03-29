package com.zzl.seckill.dao;

import com.zzl.seckill.domain.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author: Zzl
 * @Date: 21:28 2020/3/28
 * @Version 1.0
 **/
@Mapper
public interface MiaoshaUserDao {

    /**
     * 查询用户
     * @param id 手机号
     * @return 返回用户对象
     */
    @Select("select * from miaosha_user where id = #{id}")
    public MiaoshaUser getById(@Param("id") long id);
}
