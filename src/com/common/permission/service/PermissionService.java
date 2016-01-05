package com.common.permission.service;

import java.util.List;
import java.util.Map;

import javax.management.relation.Role;

import com.common.dbutil.Dao;
import com.common.permission.entity.*;
/**
 * 获取权限列表和查询是否有单个权限
 * 时间:2012-07-11
 * @author zhouya  
 *
 */
public interface PermissionService extends Dao<PmPermission> {

	/**
	 * 根据用户获取用户的权限
	 * @param prur 传入一个用户编号,传入一个父级编号
	 * @return 返回一个权限集合
	 */
	public List<PmPermission> getUserPermission(PmRUserRole prur,PmPermission pp) throws Exception;
	
	/**
	 * 查询用户是否有单个权限
	 * @param prur 传入一个用户编号
	 * @param pp 传入一个权限编号或者url
	 * @return 返回一个boolean true 有权限 false 没有权限
	 */
	public boolean boolUserPermission(PmRUserRole prur,PmPermission pp) throws Exception;
	
	/**
	 * 根据用户获取用户的权限
	 * @param prur 传入一个用户编号
	 * @return 返回一个权限集合
	 */
	public List<Map> getUserPermission(PmRUserRole prur) throws Exception;
	/**
	 * 查询系统所有的权限
	 * @return 返回一个权限集合
	 */
	public List<PmPermission> getPermission() throws Exception;
	
	
	/**
	 * 根据角色编号获得权限
	 * @param role 传入角色编号
	 * @return 返回一个权限集合
	 */
	public List<PmPermission> getRolePermission(PmRole role) throws Exception;
	
	/**
	 * 查询分配权限新添加的权限
	 * @param permissioinId  权限编号用逗号隔开
	 * @param pmRole  角色编号
	 * @return 返回一个权限集合
	 */
	public List<PmPermission> getRolePermissionSave(String permissioinId,PmRole pmRole) throws Exception;
	
	/**
	 * 获取用户权限的集合
	 * @param url 权限的url
	 * @param userId 用户编号
	 * @return 返回一个List<Map>集合
	 */
	public List<Map> boolUserPermission(String url,String userId) throws Exception;
	
	/**
	 * 添加权限
	 * @param permission 传入一个权限对象
	 * @return  返回true添加成功 false添加失败
	 */
	public boolean savePermission(PmPermission permission) throws Exception;
	
	/**
	 * 修改权限
	 * @param permission 传入一个权限对象
	 * @return  返回true修改成功 false修改失败
	 */
	public boolean updPermission(PmPermission permission) throws Exception;
	
	/**
	 * 删除权限
	 * @param permission 传入一个权限对象
	 * @return  返回true删除成功 false删除失败
	 */
	public boolean delPermission(PmPermission permission) throws Exception;
	
	/**
	 * 通过主键编号来获得权限对象
	 * @param permission 传入一个编号
	 * @return 返回一个完整的对象
	 */
	public PmPermission getPermissionId(PmPermission permission) throws Exception;
	
	/**
	 * 查找权限名称是否存在
	 * @param pmPermission 传入权限名称
	 * @return true 不存在 false 存在
	 */
	public boolean boolPermissionName(PmPermission pmPermission) throws Exception;
	
	/**
	 * 根据名称获取权限信息
	 * @param permissionName 权限名称
	 * @return 返回一个权限对象
	 * @throws Exception
	 */
	public PmPermission getPmPermission(String permissionName)throws Exception ;
}
