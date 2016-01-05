package com.common.permission.service;

import java.util.List;

import com.common.dbutil.Dao;
import com.common.permission.entity.*;
/**
 * 角色权限关系表  
 * 时间:2012-07-12
 * 姓名:zhouya
 *
 */
public interface RolePermissionService extends Dao<PmRRolePermissioin>{

	/**
	 * 给角色分配权限
	 * @param permissioinId 权限编号用逗号隔开的
	 * @param pmRole角色的编号
	 * @return返回一个boolean  true分配成功  false分配失败 
	 */
	public boolean getRolePermission(String permissioinId,PmRole pmRole) throws Exception;
	
	/**
	 * 角色权限的关系删除方法
	 * @param prrp 传入一个角色权限关系表对象
	 * @return 返回一个boolean  true删除成功  false删除失败
	 */
	public boolean delRolePermissionin(PmRRolePermissioin prrp) throws Exception;
	/**
	 * 角色权限的关系添加方法
	 * @param prrp传入一个角色权限关系表对象
	 * @return  返回一个boolean  true添加成功  false添加失败
	 */
	public boolean saveRolePermissionin(PmRRolePermissioin prrp) throws Exception;
	
	
	/**
	 * 角色权限的关系添加方法
	 * @param prrp传入一个角色权限关系表对象
	 * @return  返回一个boolean  true修改成功  false修改失败
	 */
	public boolean updRolePermissionin(PmRRolePermissioin prrp) throws Exception;
	
	/**
	 * 判断权限是否和角色绑定
	 * @param prrp 权限编号
	 * @return 返回true 没有绑定 false有绑定
	 */
	public boolean ifRolePermission(PmRRolePermissioin prrp) throws Exception;
	
	/**
	 * 角色被删除，系统需要删除角色和权限对应的关系表
	 * @param pr 角色对象
	 */
	public void delRolePermission(PmRole pr) throws Exception;
}
