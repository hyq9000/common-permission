package com.common.permission.service.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.common.dbutil.DaoHibernateImpl;
import com.common.dbutil.Paging;
import com.common.permission.entity.*;
import com.common.permission.service.RoleService;

/**
 * 角色管理的实现 对pm_role表的操作
 * 2013-01-04
 * zhouya
 */
@Stateless
@Remote(RoleService.class)
public class RoleServiceImpl extends DaoHibernateImpl<PmRole> implements RoleService {

	public boolean saveRole(PmRole pr)  throws Exception{
		if(pr!=null){
			try {
				this.add(pr);
				return true;
			} catch (Exception e) {
				throw e;
			}
		}
		return false;
	}

	public boolean delRole(PmRole pr) throws Exception {
		if(pr!=null){
			try {
				this.delete(pr);
				return true;
			} catch (Exception e) {
				throw e;
			}
		}
		return false;
	}

	public boolean updRole(PmRole pr)  throws Exception{
		if(pr!=null){
			try {
				this.update(pr);
				return true;
			} catch (Exception e) {
				throw e;
			}finally{
				//角色被修改，需要把用户权限的缓存清除
				PermissionServiceImpl.userMap=new HashMap<String, Object>();
				PermissionServiceImpl.mapAppliction=new HashMap<String, Object>();
			}
		}
		return false;
	}

	public List roleList(Paging paging) throws Exception{
		if(paging!=null){
			String sql="select * from pm_role order by role_id desc";
			List roleList = this.executeQuery(sql, paging, null);
			//集合不为null且集合长度大于0 返回集合对象 否则返回null
			if(roleList!=null&&roleList.size()>0){
				return roleList;
			}
			return null;
		}
		return null;
	}

	public Integer roleSize() throws Exception {
		String sql="select count(*) as count from pm_role";
		try {
			List<Map> list = this.executeQuery(sql);
			//检测集合不为null且长度大于0 返回集合第一个对象且转换为整形 否则返回0
			if(list!=null&&list.size()>0){
				return Integer.parseInt(list.get(0).get("count").toString());
			}
			return 0;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public PmRole getRoleId(PmRole pr)  throws Exception{
		//检测传入参数不为null 根据编号获取角色对象
		if(pr!=null){
			return this.getById(pr.getRoleId());
		}
		return null;
	}
	
	public boolean ifRoleUser(PmRole role)  throws Exception{
		try {
			//检测传入的角色对象不为空 否则返回false
			if(role!=null){
				String sql="select count(*) as count from pm_r_user_role where role_user_rid = ?";
				Object [] obj=new Object[1];
				obj[0]=role.getRoleId();
				List<Map> list = this.executeQuery(sql, obj);
				//集合不为空且集合长度大于0则获取集合的第一个对象 否则返回false
				if(list!=null&&list.size()>0){
					//获取集合第一个对象并且转换为int类型，整形小于等于0返回true 否则返回false
					if(Integer.parseInt(list.get(0).get("count").toString())<=0){
						return true;
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return false;
	}
	
	public List<Map> getAllUserRole() throws Exception{
		String sql="select * from pm_role where role_status=0 and role_type!='cloud_product'";
		try {
			List<Map> list = this.executeQuery(sql);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public boolean ifRoleName(PmRole role) throws Exception{
		try {
			String sql="select * from pm_role where role_name=? and role_type!='cloud_product'";
			Object [] obj=new Object[1];
			obj[0]=role.getRoleName();
			List list = this.executeQuery(sql, obj);
			//返回集合不为空切集合长度大于0 则返回false 否则返回true
			if(list!=null&&list.size()>0){
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	public List<PmRole> getRoleType(String type)  throws Exception{
		try {
			String ql="from PmRole where roleType=?";
			Object [] obj=new Object[1];
			obj[0]=type;
			List list = this.query(ql, obj);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	public List<PmRole> getAllRole() throws Exception{
		try {
			String ql="from PmRole";
			List list = this.query(ql, null);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public int massProductionRole() throws Exception {
		String sql="select PRODUCT_NAME from ccore_product where PRODUCT_NAME not in (select ROLE_NAME from pm_role where ROLE_TYPE='cloud_product')";
		List list = this.executeQuery(sql);
		//获取角色表中没有和产品名称对应的记录，循环添加到角色表中
		for (Object object : list) {
			Map map=(Map)object;
			PmRole pr=new PmRole();
			pr.setRoleName(map.get("PRODUCT_NAME").toString());
			pr.setRoleType("cloud_product");
			pr.setRoleStatus(0);
			this.add(pr);
		}
		return list!=null?list.size():0;
	}
	
	
	public PmRole getRoleNameType(String name) throws Exception{
		String ql="from PmRole where roleName=? and roleType=?";
		Object []obj=new Object[2];
		obj[0]=name;
		obj[1]="cloud_product";
		List list = this.query(ql, obj);
		//根据角色名称和角色类型获取角色信息，如果返回集合为空或者长度不为零返回集合的第一个对象
		if(list!=null&&list.size()>0){
			return (PmRole)list.get(0);
		}
		return null;
	}

	public void booleanStatus(PmRole pmRole) throws Exception {
		try {
			PmRole pm_role = this.getById(pmRole.getRoleId());
			pm_role.setRoleStatus(pmRole.getRoleStatus());
			this.updRole(pm_role);
		} catch (Exception e) {
			throw e;
		}finally{
			PermissionServiceImpl.mapAppliction=new HashMap<String, Object>();
			PermissionServiceImpl.userMap=new HashMap<String, Object>();
		}
	}

}
