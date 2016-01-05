package com.common.permission.service;

import java.util.List;
import java.util.Map;

import com.common.dbutil.Paging;
import com.common.permission.entity.PmRole;
/**
 * 权限对外部api接口
 * 1：分页获取角色列表
 * 2：给用户分配角色
 * 2013-01-04
 * @author zhouya
 *
 */
public interface ExteriorPermissionService{

	/**
	 * 分页获取角色列表
	 * @param paging 分页对象
	 * @return 返回一个角色对象的集合
	 * @deprecated 这个方法不要用，后继版本会不再支持
	 */
	public List<PmRole> getAllRole(Paging paging) throws Exception;
	
	/**
	 * 获取所有的角色列表
	 * @return 返回一个角色Map的集合:MAP中的key格式是
	 * 	<Li>roleId的为角色ID
	 *  <li>KEY为值roleName的是角色名
	 *  <li>KEY为值roleStatus的是角色状态
	 */
	public List<Map<String,String>> getAllRole() throws Exception;
	
	/**
	 * 返回绑定到指定的用户ID的单个角色对象,如果有多个，只返回第一个
	 * @param userId 用户主键ID
	 * @return 返回一个MAP,MAP中的key格式是
	 * 	<Li>roleId的为角色ID
	 *  <li>roleName角色名
	 *  <li>roleStatus的是角色状态
	 */
	public Map<String,String> getUserRole(String userId) throws Exception;
	
	
	/**
	 * 返回绑定到指定的用户ID所有角色对象
	 * @param userId 用户主键ID
	 * @return 返回一个MAP,MAP中的key格式是
	 * 	<Li>roleId的为角色ID
	 *  <li>roleName角色名
	 *  <li>roleStatus的是角色状态
	 */
	public List<Map<String,String>> getUserRoleList(String userId) throws Exception;
	
	/**
	 * 给用户分配角色
	 * @param userId 用户编号
	 * @param roleId 角色编号
	 * @param userName 用户名称
	 * @return 返回一个布尔类型 true成功 false失败
	 */
	public boolean setUserPermission(String userId,String roleId,String userName) throws Exception;
	
	/**
	 * 给多个用户分配不同角色
	 * @param userRoles 一个集合里面是一个map，map中有两个key一个userId和roleId,这两个key必须有缺少一个都会失败
	 * @return 返回一个布尔类型 true成功 false失败
	 */
	public boolean setUserPermission(List<Map<String,String>> userRoles) throws Exception;
	
	/**
	 * 给多个用户分配同一个角色
	 * @param userList 用户集合里面不需有一个编号
	 * @param roleId 角色编号
	 * @return 返回一个布尔类型 true成功 false失败
	 */
	public boolean setUserPermission(List<String> userList,String roleId) throws Exception;
	
	/**
	 * 多个用户多个角色名称
	 * @param userList 用户集合
	 * @param roleNameList 角色名称集合
	 * @return返回一个布尔类型 true成功 false失败
	 */
	public boolean setUserPermission(List<String> userList,List<String> roleNameList) throws Exception;
	
	/**
	 * 单个用户多个角色名称
	 * @param userId 用户编号
	 * @param roleNameList 角色名称集合
	 * @return 返回一个布尔类型 true成功 false失败
	 */
	public boolean setUserPermission(String userId,List<String> roleNameList) throws Exception;
	
	/**
	 * 根据角色类型来获取角色列表
	 * @param type 类型名称
	 * @return 返回一个角色对象的集合
	 * @deprecated 这个方法不要用，后继版本会不再支持
	 */
	public List<PmRole> getRoleType(String type) throws Exception;
}
