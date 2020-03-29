package com.zzl.seckill.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Zzl
 * @Date: 17:35 2020/3/29
 * @Version 1.0
 **/
public class ValidatorUtil {

    private static final Pattern MOBILE_PATTERN = Pattern.compile("1\\d{10}");

    /**
     * 正则表达式匹配是否符合手机号码格式
     * @param src 输入的“手机号码”
     * @return 如果输入的“手机号码”符合规范，则返回true，否则返回false
     */
    public static boolean isMobile(String src){
        if(StringUtils.isEmpty(src)){
            return false;
        }
        Matcher m = MOBILE_PATTERN.matcher(src);
        return m.matches();
    }

    public static void main(String[] args) {
        System.out.println(isMobile("15612341234"));
    }
}
