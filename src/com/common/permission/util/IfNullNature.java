package com.common.permission.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 本类是用来判断用户传入的对象参数是否为空
 * @author 周亚
 *	2012-07-16
 */
public class IfNullNature {

	/*
	 * 本方法是用来查询对象的那些字段是否有值
	 */
	public static boolean natureIfNull(List<String> listObj,Object obj){
		boolean bool=true;
		if(listObj!=null&&listObj.size()>0){
			//循环类的字段
			for (String string : listObj) {
					try {
						//通过反射去调取对象的get方法
						Method method = obj.getClass().getMethod("get"+string, null);
						//获得对象是否的返回值
						Object MethodObj = method.invoke(obj);
						if(MethodObj==null||"".equals(MethodObj)){
							bool=false;
						}
					} catch (SecurityException e) {
						bool=false;
					} catch (IllegalArgumentException e) {
						bool=false;
					} catch (NoSuchMethodException e) {
						bool=false;
					} catch (IllegalAccessException e) {
						bool=false;
					} catch (InvocationTargetException e) {
						bool=false;
					}
					if(!bool){
						break;
					}
			}
		}else{
			bool=false;
		}
		return bool;
		
	}
	
}
