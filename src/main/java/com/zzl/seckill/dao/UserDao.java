package com.zzl.seckill.dao;

import com.zzl.seckill.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Author: Zzl
 * @Date: 22:41 2020/3/26
 * @Version 1.0
 **/
@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    public User getById(@Param("id") int id);

    @Insert("insert into user(id, name) values(#{id}, #{name})")
    public int insert(User user);
}
