package com.common.permission.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import com.common.permission.entity.*;
import com.common.permission.service.PermissionService;
import com.common.dbutil.DaoHibernateImpl;
/**
 * 权限的添加删除修改查询
 * 2012-12-05
 * zhouya
 */
@Stateless
@Remote(PermissionService.class)
public class PermissionServiceImpl extends DaoHibernateImpl<PmPermission> implements
		PermissionService {

	//用户的权限url缓存
	 static Map<String,Object> mapAppliction=new HashMap<String, Object>();
	//用户权限的缓存
	 static Map<String,Object> userMap=new HashMap<String, Object>();
	/*
	 * 查询用户的单个角色
	 */
	public boolean boolUserPermission(PmRUserRole prur, PmPermission pp) throws Exception{
		StringBuffer sb=new StringBuffer();
		sb.append("select count(*) as count from pm_r_user_role prur LEFT JOIN pm_r_role_permissioin prrp on prur.ROLE_USER_RID=prrp.ROLE_PERMISSION_RID");
		sb.append(" LEFT JOIN pm_permission pp on pp.PERMISSION_ID=prrp.ROLE_PERMISSION_PID");
		sb.append(" where prur.ROLE_USER_UID=? and pp.PERMISSION_URI=?");
		Object [] obj=new Object[2];
		obj[0]=prur.getRoleUserUid();
		obj[1]=pp.getPermissionUri();
		//判断传入参数是否为空，查询用户和uri是否存在
		if(prur!=null&&pp!=null){
			List<Map> listCount = (List<Map>)this.executeQuery(sb.toString(), obj);
			//返回的记录数是否大于0
			if(listCount!=null&&listCount.size()>0){
				if(Integer.parseInt(listCount.get(0).get("count").toString())>0){
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	 * 根据用户来获取用户的权限
	 * prur 传入一个用户编号
	 * pp传入一个父节点编号
	 */
	public List<PmPermission> getUserPermission(PmRUserRole prur,PmPermission pp)  throws Exception{
		StringBuffer sb=new StringBuffer();
		sb.append("from PmPermission where permissionId in(select rolePermissionPid ");
		sb.append("from PmRRolePermissioin where rolePermissionRid in ");
		sb.append("(select roleUserRid from PmRUserRole where roleUserUid=?)) and permissionParentId=? ");
		Object [] obj=new Object[2];
		obj[0]=prur.getRoleUserUid();
		obj[1]=pp.getPermissionParentId();
		if(prur!=null){
			List<PmPermission> listPermission=(List<PmPermission>)this.query(sb.toString(), obj);
			if(listPermission!=null&&listPermission.size()>0){
				return listPermission;
			}
		}
		return null;
	}
	/*
	 * 根据用户来获取用户的权限
	 * prur 传入一个用户编号
	 */
	public List<Map> getUserPermission(PmRUserRole prur) throws Exception{
		//检测缓存中是否有传入的用户，有则从缓存中取得用户信息，没有则从数据库中查询
		if(!(userMap!=null&&userMap.size()>0&&userMap.get(prur.getRoleUserUid())!=null)){
			StringBuffer sb=new StringBuffer();
			sb.append("select * from pm_permission where PERMISSION_ID in (select role_permission_pid from pm_r_role_permissioin where role_permission_rid in (select role_user_rid from pm_r_user_role where role_user_uid=?) and role_permission_status=0)");
			Object [] obj=new Object[1];
			obj[0]=prur.getRoleUserUid();
			if(prur!=null){
				List<Map> listPermission=(List<Map>)this.executeQuery(sb.toString(), obj);
				if(listPermission!=null&&listPermission.size()>0){
					userMap.put(prur.getRoleUserUid(), listPermission);
					return listPermission;
				}else{
					return null;
				}
			}
		}else{
			return (List<Map>)userMap.get(prur.getRoleUserUid());
		}
		return null;
	}
	
	/*
	 * 查询系统所有的权限
	 */
	public List<PmPermission> getPermission() throws Exception{
		try {
			String hql="from PmPermission";
			List<PmPermission> listPermission=(List<PmPermission>)this.query(hql, null);
			//集合不为空且长度大于0返回集合
			if(listPermission!=null&&listPermission.size()>0){
				return listPermission;
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/*
	 * 根据角色获取权限
	 * role角色编号
	 */
	public List<PmPermission> getRolePermission(PmRole role) throws Exception{
		try {
			StringBuffer sb=new StringBuffer();
			sb.append("from PmPermission where permissionId in " +
					"(select rolePermissionPid from PmRRolePermissioin where rolePermissionRid=? and rolePermissionStatus=0)");
			Object [] obj=new Object[1];
			obj[0]=role.getRoleId();
			List<PmPermission> listPermission=(List<PmPermission>)this.query(sb.toString(), obj);
			//集合不为空且长度大于0返回集合
			if(listPermission!=null&&listPermission.size()>0){
				return listPermission;
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}

	/*
	 * 查询权限新添加的权限
	 * permissioinid 权限编号用逗号隔开
	 * pmrole角色编号
	 */
	public List<PmPermission> getRolePermissionSave(String permissioinId,PmRole pmRole) throws Exception{
		try {
			String addhql="from PmPermission where permissionId in (?)) and permissionId not in "+
			"(select rolePermissionPid from PmRRolePermissioin where rolePermissionRid=? and rolePermissionPid in (?) and rolePermissionStatus=0)";
			Object [] obj=new Object[3];
			obj[0]=permissioinId;
			obj[1]=pmRole.getRoleId();
			obj[2]=permissioinId;
			List<PmPermission> list = this.query(addhql, obj);
			return list;
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 通过传入的url和用户编号来查询用户是否有权限
	 * 如果用户来查询过一次后会到静态对象mapAppliction中来获得对象
	 * 但是这个地方会有一个bug如果信息被修改了，不会自动更新mapAppliction对象中的信息
	 */
	public List<Map> boolUserPermission(String url, String userId) throws Exception {
		try {
			Object objMap = null;
			//缓存是否有传入的用户信息
			if(mapAppliction!=null&&mapAppliction.size()>0){
				objMap=mapAppliction.get(userId);
			}
			//缓存没有用户信息到数据库中查，则直接返回缓存中的用户信息
			if(objMap==null){
				String sql1="select pr.role_id from pm_r_user_role prur LEFT JOIN pm_role pr on prur.ROLE_USER_RID=pr.role_id where prur.ROLE_USER_UID=? and pr.ROLE_STATUS=0";
				Object [] obj=new Object[1];
				obj[0]=userId;
				List<Map> list = this.executeQuery(sql1, obj);
				//检测用户是否有分配了角色
				if(list!=null&&list.size()>0){
					StringBuffer sb=new StringBuffer();
					//循环构建角色参数
					for(int i=0;i<list.size();i++){
						if(i!=0){
							sb.append(",");
						}
						sb.append(list.get(i).get("ROLE_ID"));
					}
					String sql="select pp.PERMISSION_URI from pm_permission pp LEFT JOIN pm_r_role_permissioin prrp on prrp.ROLE_PERMISSION_PID=pp.PERMISSION_ID where prrp.ROLE_PERMISSION_STATUS=0 and pp.PERMISSION_URI !='' and prrp.ROLE_PERMISSION_RID in ("+sb.toString()+") group by pp.PERMISSION_URI";
					//根据角色找出对应的权限
					List lists = this.executeQuery(sql);
					long time=System.currentTimeMillis();
					mapAppliction.put(userId, lists);
					return lists;
				}else{
					return null;
				}
			}else{
				return (List<Map>)objMap;
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/*
	 * 添加权限
	 */
	public boolean savePermission(PmPermission permission) throws Exception {
		try {
			this.add(permission);
			return true;
		} catch (Exception e) {
			throw e;
		}finally{
		    //刷新缓存 2014-06-04
			PermissionServiceImpl.freshPermissionBuffer();
		}
	}
	
	/*
	 * 传入permission来修改权限
	 */
	public boolean updPermission(PmPermission permission) throws Exception {
		try {
			this.update(permission);
			return true;
		} catch (Exception e) {
			throw e;
		}finally{
			mapAppliction=new HashMap<String, Object>();
			userMap=new HashMap<String, Object>();
		}
	}
	
	/*
	 * 传入permission对象来删除对应的权限
	 * 周亚
	 * 
	 */
	public boolean delPermission(PmPermission permission) throws Exception {
		try {
			this.delete(permission);
			return true;
		} catch (Exception e) {
			throw e;
		}finally{
		    //刷新缓存 2014-06-04
			PermissionServiceImpl.freshPermissionBuffer();
		}
	}
	
	/*
	 * 传入permission对象的permissionId来查询权限对象
	 */
	public PmPermission getPermissionId(PmPermission permission) throws Exception {
		try {
			String hql="from PmPermission where permissionId=?";
			Object []obj=new Object[1];
			obj[0]=permission.getPermissionId();
			List list = this.query(hql, obj);
			if(list!=null&&list.size()>0){
				//获得集合中的第一个permission对象
				return (PmPermission)list.get(0);
			}
		} catch (Exception e) {
			throw e;
		}
		return null;
	}
	
	/*
	 * 查询权限名称是否存在
	 */
	public boolean boolPermissionName(PmPermission pmPermission) throws Exception{
		try {
			String hql="from PmPermission where permissionName=?";
			Object []obj=new Object[1];
			obj[0]=pmPermission.getPermissionName();
			List list = this.query(hql, obj);
			if(list!=null&&list.size()>0){
				return false;
			}
		} catch (Exception e) {
			throw e;
		}
		return true;
	}
	
	public PmPermission getPmPermission(String permissionName) throws Exception {
		String ql="from PmPermission where permissionName=?";
		Object []obj=new Object[1];
		obj[0]=permissionName;
		List list = this.query(ql, obj);
		if(list!=null&&list.size()>0)
			return (PmPermission)list.get(0);
		return null; 
	}
	
	/**
	 * 当权限，用户与角色关系，角色与权限关系发生变更时，删除当前缓存；
	 */
	public static void freshPermissionBuffer(){
		mapAppliction=new HashMap<String, Object>();
	}	
}
