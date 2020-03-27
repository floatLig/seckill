package com.zzl.seckill.service;

import com.zzl.seckill.dao.UserDao;
import com.zzl.seckill.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author: Zzl
 * @Date: 22:45 2020/3/26
 * @Version 1.0
 **/
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getById(int id){
        return userDao.getById(id);
    }

    @Transactional
    public boolean tx(){
        User u1 = new User();
        u1.setId(2);
        u1.setName("222");
        userDao.insert(u1);

        User u2 = new User();
        u2.setId(1);
        u2.setName("1111");
        userDao.insert(u2);

        return true;
    }
}
