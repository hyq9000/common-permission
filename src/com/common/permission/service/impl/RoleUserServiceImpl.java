package com.common.permission.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.common.dbutil.DaoHibernateImpl;
import com.common.permission.entity.*;
import com.common.permission.service.RoleUserService;
/**
 * 用户管理关系实现 
 * 2013-01-04
 * zhouya
 */
@Stateless
@Remote(RoleUserService.class)
public class RoleUserServiceImpl extends DaoHibernateImpl<PmRUserRole> implements RoleUserService {

	public boolean saveRoleUser(PmRUserRole prur)  throws Exception{
		if(prur!=null){
			try {
				this.add(prur);
				 //刷新缓存 2014-06-04
				PermissionServiceImpl.freshPermissionBuffer();
				return true;
			} catch (Exception e) {
				 //刷新缓存 2014-06-04
				PermissionServiceImpl.freshPermissionBuffer();
				throw e;
			}
		}
		return false;
	}

	public boolean delRoleUser(PmRUserRole prur)  throws Exception{
		if(prur!=null){
			try {
				this.delete(prur);
				return true;
			} catch (Exception e) {
				throw e;
			}finally{
			    //刷新缓存 2014-06-04
				PermissionServiceImpl.freshPermissionBuffer();
			}
		}
		return false;
	}
	
	public boolean updRoleUser(PmRUserRole prur) throws Exception {
		if(prur!=null){
			try {
				this.update(prur);
				
				PermissionServiceImpl.freshPermissionBuffer();
				return true;
			} catch (Exception e) {
				throw e;
			}finally{
			    //刷新缓存 2014-06-04
				PermissionServiceImpl.freshPermissionBuffer();
			}
		}
		return false;
	}
	
	public List<Map<String, String>> getUserRoleName(List<Map<String,String>> listMap) throws Exception{
		try {
			listMap.remove(listMap.size()==0?0:listMap.size()-1);
			String hql="select pm_role.ROLE_NAME as ROLE_NAME,pm_r_user_role.ROLE_USER_UID as ROLE_USER_UID from pm_r_user_role join pm_role on pm_role.ROLE_ID=pm_r_user_role.ROLE_USER_RID where ROLE_USER_UID in (";
				//循环构建参数问号
				for (int i = 0; i < listMap.size(); i++) {
					if(i!=0&&i!=listMap.size())
						hql+=",";
					hql+="?";
				}
				hql+=")";
				Object [] obj=new Object[listMap.size()];
				//循环添加参数
				for (int i = 0; i < listMap.size(); i++) {
					obj[i]=listMap.get(i).get("userId");
				}
				List list=this.executeQuery(hql, obj);
				//循环匹配用户编号，构建角色名称字符串
				for (Map<String,String> map : listMap) {
					StringBuffer sb=new StringBuffer();
					for (Object object : list) {
						Map maps = (Map)object;
						//匹配用户编号是否一样 true记录角色名称
						if(map.get("userId").equals(maps.get("ROLE_USER_UID").toString())){
							sb.append(maps.get("ROLE_NAME").toString()).append(",");
						}
					}
					map.put("roleName",sb.length()>0?sb.substring(0, sb.length()-1):"");
				}
				return listMap;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	public void delUserId(PmRUserRole prur)  throws Exception{
		try {
			List<PmRUserRole> list = this.getuserRole(prur.getRoleUserUid());
			//检测集合是否为空 true 循环删除 
			if(list!=null&&list.size()>0){
				for (int i=list.size();i>0;i--) {
					this.delete(list.get(i-1));
				}
			}
		} catch (Exception e) {
			throw e;
		}finally{
			//清空用户权限缓存
			PermissionServiceImpl.freshPermissionBuffer();
		}
		
	}
	
	public List<PmRUserRole> getuserRole(String userid) throws Exception{
		try {
			String ql="from PmRUserRole where roleUserUid=? ";
			Object [] obj=new Object[1];
			obj[0]=userid;
			List list = this.query(ql, obj);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}

	
}
