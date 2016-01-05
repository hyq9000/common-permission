package com.common.permission.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Dao;
import com.common.dbutil.Paging;
import com.common.permission.entity.*;
/**
 * 角色的操作类
 * 主要是添加删除修改
 * 时间:2012-07-09
 * zhouya
 *
 */
public interface RoleService extends Dao<PmRole> {
	
	/**
	 * 添加角色
	 * @param pr  pmrole对象
	 * @return  返回boolean true 成功  false  失败
	 */
	public boolean saveRole(PmRole pr) throws Exception;
	
	/**
	 * 删除角色
	 * @param pr  pmrole对象
	 * @return  返回boolean true 成功  false  失败
	 */
	public boolean delRole(PmRole pr) throws Exception;
	
	/**
	 * 修改角色
	 * @param pr  pmrole对象
	 * @return  返回boolean true 成功  false  失败
	 */
	public boolean updRole(PmRole pr) throws Exception;
	
	/**
	 * 带分页的角色列表
	 * @param paging  分页对象
	 * @return  返回一个role对象的集合
	 */
	public List roleList(Paging paging) throws Exception;
	
	/**
	 * 获取角色的总记录数
	 * @return  返回一个integer的整数
	 */
	public Integer roleSize() throws Exception;
	
	
	/**
	 * 根据角色编号获得整个角色信息
	 * @param pr 传入一个角色实体对象roleId必须有
	 * @return true返回一个完整的实体对象 false 返回null
	 */
	public PmRole getRoleId(PmRole pr) throws Exception;
	
	/**
	 * 判断是否有用户绑定了该角色
	 * @param prur 角色编号
	 * @return 返回true用户绑定该角色 false角色没有被绑定
	 */
	public boolean ifRoleUser(PmRole role) throws Exception;
	
	/**
	 * 返回一个没有分页的角色列表
	 * @return true返回所有的角色对象 false返回null
	 */
	public List<Map> getAllUserRole() throws Exception;
	
	/**
	 * 判断角色名称 是否存在
	 * @param role 角色对象
	 * @return true角色名称存在 false角色名称不存在
	 */
	public boolean ifRoleName(PmRole role) throws Exception;
	
	/**
	 * 根据角色类型，获取角色列表
	 * @param type 角色类型
	 * @return true返回角色集合 false返回null
	 */
	public List<PmRole> getRoleType(String type) throws Exception;
	
	/**
	 * 获取所有的角色
	 * @return true返回角色集合 false返回null
	 */
	public List<PmRole> getAllRole() throws Exception;
	
	/**
	 * 批量生成角色名称
	 * @return 0没有生成任何数据 其他的则是生成对应的条数
	 * @throws Exception
	 */
	public int massProductionRole() throws Exception ;
	
	
	/**
	 * 根据角色名称和角色类型查找数据
	 * @param name 角色名称
	 * @return 返回 true返回角色对象 false返回null
	 * @throws Exception
	 */
	public PmRole getRoleNameType(String name) throws Exception;
	
	/**
	 * 角色的启用停用
	 * @param pmRole 角色对象
	 * @throws Exception
	 */
	public void booleanStatus(PmRole pmRole)throws Exception;
}
