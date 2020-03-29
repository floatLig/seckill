package com.zzl.seckill.validator;

import com.zzl.seckill.utils.ValidatorUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: Zzl
 * @Date: 17:23 2020/3/29
 * @Version 1.0
 **/
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println(value);
        if(required){
            return ValidatorUtil.isMobile(value);
        }else {
            if(StringUtils.isEmpty(value)){
                return true;
            }else{
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
