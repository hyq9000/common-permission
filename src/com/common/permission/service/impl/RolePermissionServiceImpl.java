package com.common.permission.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.common.dbutil.DaoHibernateImpl;
import com.common.permission.entity.*;
import com.common.permission.service.RolePermissionService;
/**
 * 角色权限的添加删除修改查询
 * 2012-12-05
 * zhouya
 */
@Stateless
@Remote(RolePermissionService.class)
public class RolePermissionServiceImpl extends DaoHibernateImpl<PmRRolePermissioin> implements	RolePermissionService {

	/*
	 * 角色分配权限
	 */
	public boolean getRolePermission(String permissioinId,PmRole pmRole) throws Exception{
		try {
			if(permissioinId!=null&&!"".equals(permissioinId)&&pmRole!=null){
				//查询出需要删除的角色权限
				String hql="select * from pm_r_role_permissioin where role_permission_rid=? and" +
						" role_permission_pid not in (";
				String [] per=permissioinId.split(",");
				//循环构建参数问号
				for (int i = 0; i < per.length; i++) {
					if(i!=0&&i!=per.length)
						hql+=",";
					hql+="?";
				}
				hql+=") and role_permission_status=0";
				Object [] obj=new Object[1+per.length];
				obj[0]=pmRole.getRoleId();
				//循环添加参数
				for (int i = 0; i < per.length; i++) {
					obj[i+1]=per[i];
				}
				List<Map> list = this.executeQuery(hql, obj);
				List<String> listPid=new ArrayList<String>();
				if(list!=null&&list.size()>0){
					//循环更新角色的权限关系
					for (Map map : list) {
						PmRRolePermissioin prrp=new PmRRolePermissioin();
						prrp.setRolePermissionId(Integer.parseInt(map.get("ROLE_PERMISSION_ID").toString()));
						prrp.setRolePermissionPid(Integer.parseInt(map.get("ROLE_PERMISSION_PID").toString()));
						prrp.setRolePermissionRid(Integer.parseInt(map.get("ROLE_PERMISSION_RID").toString()));
						prrp.setRolePermissionStatus(1);
						if(!this.updRolePermissionin(prrp)){
							listPid.add(map.get("ROLE_PERMISSION_ID").toString());
						}
					}
				}
				if(!(listPid!=null&&listPid.size()>0)){
					
					//查询出需要角色需要添加的权限
					List<Map> listPermission = getRolePermissionSave(permissioinId, pmRole);
					//循环添加角色新权限
					for (Map map : listPermission) {
						PmRRolePermissioin prrp=new PmRRolePermissioin();
						prrp.setRolePermissionStatus(0);
						prrp.setRolePermissionPid(Integer.parseInt(map.get("PERMISSION_ID").toString()));
						prrp.setRolePermissionRid(pmRole.getRoleId());
						this.saveRolePermissionin(prrp);
					}
				}
				return true;
			}
		} catch (Exception e) {
			throw e;
		}finally{
			PermissionServiceImpl.userMap=new HashMap<String, Object>();
			PermissionServiceImpl.mapAppliction=new HashMap<String, Object>();
		}
		return false;
	}
	
	/*
	 * 角色权限的添加方法
	 */
	public boolean saveRolePermissionin(PmRRolePermissioin prrp) throws Exception{
		try {
			this.add(prrp);
			//刷新缓存 2014-06-04
			PermissionServiceImpl.freshPermissionBuffer();
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	 * 角色权限的修改方法
	 */
	public boolean updRolePermissionin(PmRRolePermissioin prrp) throws Exception{
		try {
			this.update(prrp);
			//刷新缓存 2014-06-04
			PermissionServiceImpl.freshPermissionBuffer();
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 角色权限的删除方法
	 */
	public boolean delRolePermissionin(PmRRolePermissioin prrp) throws Exception{
		try {
			this.delete(prrp);
			return true;
		} catch (Exception e) {
			throw e;
		}finally{
		    //刷新缓存 2014-06-04
			PermissionServiceImpl.freshPermissionBuffer();
		}
	}
	/*
	 * 根据权限编号查询出需要添加的权限
	 * @param permissioinId 权限的编号用逗号隔开
	 * @param pmRole 角色编号
	 * @return 返回一个权限编号
	 */
	private List<Map> getRolePermissionSave(String permissioinId,PmRole pmRole) throws Exception{
		try {
			String [] per=permissioinId.split(",");
			String addhql="select * from pm_permission where permission_id in (";
			//循环构建参数问号
			for (int i = 0; i < per.length; i++) {
				if(i!=0&&i!=per.length)
					addhql+=",";
				addhql+="?";
			}
			addhql+=") and permission_id not in "+
			"(select role_permission_pid from pm_r_role_permissioin where " +
			"role_permission_rid=? and role_permission_pid in (";
			//循环构建参数问号
			for (int i = 0; i < per.length; i++) {
				if(i!=0&&i!=per.length)
					addhql+=",";
				addhql+="?";
			}
			addhql+=") and role_permission_status=0)";
			Object [] obj=new Object[1+per.length*2];
			//循环添加参数
			for (int i = 0; i < per.length; i++) {
				obj[i]=per[i];
			}
			obj[per.length]=pmRole.getRoleId();
			//循环添加参数
			for (int i = 0; i < per.length; i++) {
				obj[per.length+1+i]=per[i];
			}
			List<Map> list = this.executeQuery(addhql, obj);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 判断角色和权限是否绑定
	 */
	public boolean ifRolePermission(PmRRolePermissioin prrp) throws Exception {
		try {
			String sql="select count(*) as count from pm_r_role_permissioin where " +
					"role_permission_pid=? and role_permission_status=0";
			Object [] obj=new Object[1];
			obj[0]=prrp.getRolePermissionPid();
			List<Map> list = this.executeQuery(sql, obj);
			//list对象是否为空 false 返回false
			if(list!=null&&list.size()>0){
				//list 中map key为count的值小于等于0返回true 否则检测权限是否有对应的角色
				if(Integer.parseInt(list.get(0).get("count").toString())<=0){
					return true;
				}else{
					sql="select count(*) as count from pm_role where ROLE_ID in (select ROLE_PERMISSION_RID from pm_r_role_permissioin where role_permission_pid=? and role_permission_status=0)";
					obj=new Object[1];
					obj[0]=prrp.getRolePermissionPid();
					list=this.executeQuery(sql, obj);
					if(list!=null&&list.size()>0){
						if(Integer.parseInt(list.get(0).get("count").toString())<=0){
							return true;
						}
					}
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return false;
	}
	
	/*
	 * 角色被删除角色和权限对应的关系也需要删除
	 * @param pr
	 */
	public void delRolePermission(PmRole pr) throws Exception{
		try {
			String hql="from PmRRolePermissioin where rolePermissionRid=?";
			Object[] obj=new Object[1];
			obj[0]=+pr.getRoleId();
			List<PmRRolePermissioin> list = this.query(hql, obj);
			if(list!=null&&list.size()>0){
				for (int i=list.size();i>0;i--) {
					PmRRolePermissioin prr = list.get(i-1);
					this.delete(prr);
				}
			}
			//刷新缓存 2014-06-04
			PermissionServiceImpl.freshPermissionBuffer();
		} catch (Exception e) {
			throw e;
		}
	}
}
