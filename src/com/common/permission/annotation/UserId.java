package com.common.permission.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解是用注解登录用户实体类中哪个属性是用户ID；
 * 用于在权限管理；
 * <br/>时间：2014-1-23
 * @author HYQ
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UserId {}
