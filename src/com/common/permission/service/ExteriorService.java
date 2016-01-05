package com.common.permission.service;

import java.util.*;

import com.common.dbutil.Paging;
/**
 * 获取外部用户接口，是外部系统需要实现的接口
 * 2013-01-04
 * zhouya,hyq
 */
public interface ExteriorService {
	
	/**
	 * 分页获取用户列表
	 * @param paging 分页对象
	 * @return 返回一个List<Map<String,String>>
	 * Map的key有：
	 * 	 <li>userId 用户编号
	 * 	 <li>userName 用户名称 
	 */
	public List<Map<String,String>> getAllUser(Paging paging) throws Exception;
}
