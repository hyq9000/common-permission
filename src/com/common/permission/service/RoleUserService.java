package com.common.permission.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.common.permission.entity.*;
/**
 * 用户角色关系操作
 * 2013-01-04
 * @author zhouya
 *
 */
public interface RoleUserService extends Dao<PmRUserRole>{

	/**
	 * 添加用户角色关系
	 * @param prur 用户角色关系对象
	 * @return true 添加成功 false 添加失败
	 */
	public boolean saveRoleUser(PmRUserRole prur) throws Exception; 
	

	/**
	 * 删除用户角色关系
	 * @param prur 用户角色关系对象
	 * @return true 删除成功 false 删除失败
	 */
	public boolean delRoleUser(PmRUserRole prur) throws Exception;
	

	/**
	 * 修改用户角色关系
	 * @param prur 用户角色关系对象
	 * @return true 修改成功 false 修改失败
	 */
	public boolean updRoleUser(PmRUserRole prur) throws Exception;
	
	
	/**
	 * 获取用户的角色
	 * @param listMap 用户编号
	 * @return 返回一个用户列表
	 */
	public List<Map<String, String>> getUserRoleName(List<Map<String,String>> listMap) throws Exception;
	
	/**
	 * 传入用户编号来删除，这个用户所有的数据
	 * @param prur 用户角色对象
	 */
	public void delUserId(PmRUserRole prur) throws Exception;
	
	
	/**
	 * 通过用户编号来获取用户的所有角色
	 * @param userid 用户编号
	 * @return 返回list pmruserrole集合
	 */
	public List<PmRUserRole> getuserRole(String userid) throws Exception;
	
}
