package com.zzl.seckill.service;

import com.zzl.seckill.dao.MiaoshaUserDao;
import com.zzl.seckill.domain.MiaoshaUser;
import com.zzl.seckill.utils.MD5Util;
import com.zzl.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author: Zzl
 * @Date: 21:40 2020/3/28
 * @Version 1.0
 **/
@Service
public class MiaoshaUserService {

    @Autowired
    MiaoshaUserDao miaoshaUserDao;

    public boolean login(HttpServletResponse response, LoginVo loginVo){
        if(loginVo == null){
            throw new RuntimeException();
        }

        String mobile = loginVo.getMobile();
        String formPasswd = loginVo.getPassword();
        //判断手机号是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if(user == null){
            throw new RuntimeException();
        }
        String dbPasswd = user.getPassword();
        //验证密码
        String saltDb = user.getSalt();
        String calcPass = MD5Util.formPasswdToDbPasswd(formPasswd, saltDb);
        if(!calcPass.equals(dbPasswd)){
            throw new RuntimeException();
        }
        return true;
    }

    private MiaoshaUser getById(Long id){
        return miaoshaUserDao.getById(id);
    }
}
