package com.zzl.seckill.service;

import com.sun.org.apache.bcel.internal.classfile.Code;
import com.zzl.seckill.dao.MiaoshaUserDao;
import com.zzl.seckill.domain.MiaoshaUser;
import com.zzl.seckill.exception.GlobalException;
import com.zzl.seckill.redis.MiaoshaUserKey;
import com.zzl.seckill.redis.RedisService;
import com.zzl.seckill.result.CodeMsg;
import com.zzl.seckill.utils.MD5Util;
import com.zzl.seckill.utils.UUIDUtil;
import com.zzl.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
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

    @Autowired
    RedisService redisService;

    public static final String COOKIE_NAME_TOKEN = "token";

    public String login(HttpServletResponse response, LoginVo loginVo) {
        if (loginVo == null) {
            throw new GlobalException(CodeMsg.LOGIN_USER_ERROR);
        }

        //获取登录的信息
        String mobile = loginVo.getMobile();
        String formPasswd = loginVo.getPassword();
        //判断用户是否存在
        MiaoshaUser user = getById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        String dbPasswd = user.getPassword();
        //验证密码
        String saltDb = user.getSalt();
        String calcPass = MD5Util.formPasswdToDbPasswd(formPasswd, saltDb);
        if (!calcPass.equals(dbPasswd)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        System.out.println("登录成功, 账号为：" + mobile);
        //生成cookie
        String token = UUIDUtil.uuid();
        System.out.println("token为：" + token);
        addCookie(response, token, user);
        return token;
    }

    /**
     * 没有使用redis
     */
    private MiaoshaUser fakeGetById(Long id) {
        return miaoshaUserDao.getById(id);
    }

    /**
     * 添加redis
     */
    private MiaoshaUser getById(Long id) {
        //取缓存
        MiaoshaUser user = redisService.get(MiaoshaUserKey.getById, ""+ id, MiaoshaUser.class);
        if(user != null){
            return user;
        }
        //读数据库
        user = miaoshaUserDao.getById(id);
        //写缓存
        if(user != null){
            redisService.set(MiaoshaUserKey.getById, "" + id, user);
        }
        return user;
    }

    public boolean updatePassword(String token, long id, String formPass){
        //取user
        MiaoshaUser user = getById(id);
        if(user == null){
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //更新数据库
        MiaoshaUser toBeUpdate = new MiaoshaUser();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPasswdToDbPasswd(formPass, user.getSalt()));
        miaoshaUserDao.update(toBeUpdate);
        //处理缓存
        redisService.delete(MiaoshaUserKey.getById, "" + id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(MiaoshaUserKey.token, token, user);
        return true;
    }

    private void addCookie(HttpServletResponse response, String token, MiaoshaUser user) {
        //存放到redis
        redisService.set(MiaoshaUserKey.token, token, user);
        //cookie
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public MiaoshaUser getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        MiaoshaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoshaUser.class);
        //延长有效期
        if (user != null) {
            //重新设置新的cookie，更新时间
            addCookie(response, token, user);
        }
        return user;
    }
}
