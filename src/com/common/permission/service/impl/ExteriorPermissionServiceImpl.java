package com.common.permission.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;

import org.apache.struts2.json.JSONUtil;

import com.common.permission.entity.PmRUserRole;
import com.common.permission.entity.PmRole;
import com.common.permission.service.ExteriorPermissionService;
import com.common.permission.service.RoleService;
import com.common.dbutil.DaoHibernateImpl;
import com.common.dbutil.DaoJpaImpl;
import com.common.dbutil.Paging;
/**
 * 用来给用户绑定角色的实现
 * zhouya
 * 2013-01-04
 */
@SuppressWarnings("unchecked")
@Stateless
@Local(ExteriorPermissionService.class)
public class  ExteriorPermissionServiceImpl extends DaoHibernateImpl<PmRUserRole> implements ExteriorPermissionService {

	//角色service
	@EJB(beanName = "RoleServiceImpl")
	private RoleService roleService;
	
	//分页获取所有的角色
	public List<PmRole> getAllRole(Paging paging) throws Exception{
		//判断分页对象paging不为空查询数据
		if(paging!=null){
			List<PmRole> list = this.query("from PmRole", paging, null);
			return list;
		}
		return null;
	}

	/*
	 * 通过传入用户编号和角色编号来给用户绑定一个角色
	 * userid用户编号
	 * roleid角色编号
	 * userName 用户名称
	 */
	public boolean setUserPermission(String userId, String roleId,String userName) throws Exception {
		try {
			String sqlpar="select * from pm_r_user_role where ROLE_USER_UID=?";
			Object[] objname=new Object[1];
			objname[0]=userId;
			List<Map> list = this.executeQuery(sqlpar, objname);
			//循环删除角色和用户的关系
			for (Map objs : list) {
				//PmRUserRole prur=new PmRUserRole();
				//prur.setRoleUserId(Integer.parseInt(objs.get("ROLE_USER_ID").toString()));
				PmRUserRole prur=this.getById(Integer.parseInt(objs.get("ROLE_USER_ID").toString()));
				this.delete(prur);
			}
			//添加角色和用户的关系
			if(userId!=null&&!"".equals(userId)&&roleId!=null&&!"".equals(roleId)){
				PmRUserRole prur=new PmRUserRole();
				prur.setRoleUserRid(Integer.parseInt(roleId));
				prur.setRoleUserUid(userId);
				prur.setUserName(userName);
				this.add(prur);
				return true;
			}
			return false;
		} catch (Exception e) {
			throw e;
		}finally{
			PermissionServiceImpl.mapAppliction=new HashMap<String, Object>();
		}
	}

	/*
	 * 通过传入userRoles的一个集合，来给用户绑定角色
	 * 这是一个可以传入多用户和多个角色来绑定角色
	 * userRoles是一个用户和角色的集合里面是map，map的key是userId和roleId
	 */
	public boolean setUserPermission(List<Map<String, String>> userRoles)  throws Exception{
		try {
			//循环添加角色和用户的关系
			if(userRoles!=null&&userRoles.size()>0){
				for (Map<String, String> map : userRoles) {
					PmRUserRole prur=new PmRUserRole();
					prur.setRoleUserRid(Integer.parseInt(map.get("roleId")));
					prur.setRoleUserUid(map.get("userId"));
					this.add(prur);
				}
				return true;
			}
			return false;
		} catch (NumberFormatException e) {
			throw e;
		}
	}

	/*
	 * 这个是给多个用户绑定同一个角色
	 * userList是一个用户的列表
	 * roleId这个是角色的编号
	 */
	public boolean setUserPermission(List<String> userList, String roleId) throws Exception {
		try {
			//循环添加用户和角色的关系
			if(userList!=null&&userList.size()>0&&roleId!=null&&!"".equals(roleId)){
				for (String str : userList) {
					PmRUserRole prur=new PmRUserRole();
					prur.setRoleUserRid(Integer.parseInt(roleId));
					prur.setRoleUserUid(str);
					this.add(prur);
				}
				return true;
			}
			return false;
		} catch (NumberFormatException e) {
			throw e;
		}
	}
	
	
	/*
	 * 会先删除这个用户以前的角色
	 * 这个是传入roleName来绑定用户，这个方法是多个用户绑定角色
	 * userList是用户的编号
	 * roleNameList是角色名称
	 */
	public boolean setUserPermission(List<String> userList,
			List<String> roleNameList)  throws Exception{
		try {
			if(userList!=null&&userList.size()>0&&roleNameList!=null&&roleNameList.size()>0){
				//循环删除用户和角色的关系
				for (String str : userList) {
					String sql="select * from pm_r_user_role where ROLE_USER_UID=?";
					Object[] obj=new Object[1];
					obj[0]=str;
					List<Map> list = this.executeQuery(sql, obj);
					for (Map objs : list) {
						PmRUserRole prur=new PmRUserRole();
						prur.setRoleUserId(Integer.parseInt(objs.get("ROLE_USER_ID").toString()));
						this.delete(prur);
					}
				}
				//循环添加角色和用户的关系
				for (String userId : userList) {
					for (String roleName : roleNameList) {
						String sql="select role_id from pm_role where role_name=? and role_status=? and role_type=?";
						Object []obj=new Object[3];
						obj[0]=roleName;
						obj[1]=0;
						obj[2]="cloud_product";
						List<Map> listMap = this.executeQuery(sql, obj);
						if(listMap!=null&&listMap.size()>0){
							String role_id=listMap.get(0).get("ROLE_ID").toString();
							PmRUserRole prur=new PmRUserRole();
							prur.setRoleUserRid(Integer.parseInt(role_id));
							prur.setRoleUserUid(userId);
							this.add(prur);
						}
					}
				}
				return true;
			}
		} catch (Exception e) {
			throw e;
		}finally{
			PermissionServiceImpl.mapAppliction=new HashMap<String, Object>();
		}
		return false;
	}

	/*
	 * 会先删除用户以前的角色
	 * 给一个用户绑定多个角色
	 * userid用户编号
	 * roleNameList是角色名称集合
	 */
	public boolean setUserPermission(String userId, List<String> roleNameList) throws Exception {
			try {
				String sqlpar="select * from pm_r_user_role where ROLE_USER_UID=?";
				Object[] objname=new Object[1];
				objname[0]=userId;
				List<Map> list = this.executeQuery(sqlpar, objname);
				//循环删除角色和用户的关系
				for (Map objs : list) {
					PmRUserRole prur=new PmRUserRole();
					prur.setRoleUserId(Integer.parseInt(objs.get("ROLE_USER_ID").toString()));
					this.delete(prur);
				}
				if(userId!=null&&!"".equals(userId)&&roleNameList!=null&&roleNameList.size()>0){
					//循环添加角色和用户的关系
					for (String roleName : roleNameList) {
						String sql="select role_id from pm_role where role_name=? and role_status=? and role_type=?";
						Object []obj=new Object[3];
						obj[0]=roleName;
						obj[1]=0;
						obj[2]="cloud_product";
						List<Map> listMap = this.executeQuery(sql, obj);
						if(listMap!=null&&listMap.size()>0){
							String role_id=listMap.get(0).get("ROLE_ID").toString();
							PmRUserRole prur=new PmRUserRole();
							prur.setRoleUserRid(Integer.parseInt(role_id));
							prur.setRoleUserUid(userId);
							this.add(prur);
						}
					}
					return true;
				}
			} catch (Exception e) {
				throw e;
			}finally{
				PermissionServiceImpl.mapAppliction=new HashMap<String, Object>();
			}
			return false;
	}



	public List<PmRole> getRoleType(String type)  throws Exception{
			return this.roleService.getRoleType(type);
	}

	@Override
	public List<Map<String, String>> getAllRole() throws Exception {
		List list = this.executeQuery("select ROLE_ID as roleId,ROLE_NAME as roleName,ROLE_STATUS as roleStatus from pm_role");
		return list==null?null:(List<Map<String, String>>)list;
	}

	@Override
	public Map<String, String> getUserRole(String userId) throws Exception{
		List list = this.executeQuery("SELECT ROLE_ID AS roleId,ROLE_NAME AS roleName,ROLE_STATUS AS roleStatus FROM pm_role,pm_r_user_role WHERE ROLE_USER_RID=ROLE_ID  AND ROLE_USER_UID=?",userId);
		return (Map<String, String>)(list!=null && list.size()>0 ? list.get(0):null);
	}

	@Override
	public List<Map<String, String>> getUserRoleList(String userId) throws Exception{
		List list = this.executeQuery("SELECT ROLE_ID AS roleId,ROLE_NAME AS roleName,ROLE_STATUS AS roleStatus FROM pm_role,pm_r_user_role WHERE ROLE_USER_RID=ROLE_ID  AND ROLE_USER_UID=?",userId);
		return (List<Map<String, String>>)list;
	}
}
