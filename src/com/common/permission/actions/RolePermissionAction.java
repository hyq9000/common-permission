package com.common.permission.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.common.log.Log;
import com.common.permission.entity.*;
import com.common.permission.service.RolePermissionService;
import com.common.permission.util.IfNullNature;
/**
 * 角色权限action
 * @author zhouya
 *
 */
public class RolePermissionAction extends InitAction{

	public RolePermissionAction() throws Exception {
		super();
	}


	private RolePermissionService rolePermissionService;
	
	private PermissionAction permissionAction;
	public PermissionAction getPermissionAction() {
		return permissionAction;
	}
	public void setPermissionAction(PermissionAction permissionAction) {
		this.permissionAction = permissionAction;
	}
	public RolePermissionService getRolePermissionService() {
		return rolePermissionService;
	}
	public void setRolePermissionService(RolePermissionService rolePermissionService) {
		this.rolePermissionService = rolePermissionService;
	}
	
	
	private String permissionId;
	
	public String getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}
	
	private PmRole pmRole;
	
	public PmRole getPmRole() {
		return pmRole;
	}
	public void setPmRole(PmRole pmRole) {
		this.pmRole = pmRole;
	}
	
	@Log(content = "角色分配权限")
	public String distributionPermission() throws Exception{
		try {
			out=ServletActionContext.getResponse().getWriter();
		} catch (IOException e) {
			throw e;
		}
		//检测session中用户是否为空
		if(!overTime()){
			//检测permissionId字段和pmRole角色对象不为空
			if(permissionId!=null&&!"".equals(permissionId)&&pmRole!=null){
				ArrayList<String> listObj = new ArrayList<String>();
				listObj.add("RoleId");
				//检测角色对象RoleId字段不为空 false返回“false”
				if(IfNullNature.natureIfNull(listObj, pmRole)){
					//权限是否分配成功 true返回“true” false返回“false”
					if(this.rolePermissionService.getRolePermission(permissionId, pmRole)){
						//通知第三方刷新权限缓存 hyq 2014-06-04
						ActionUtils.flushPermission(ServletActionContext.getServletContext());
						out.print("true");
					}else{
						out.print("false");
					}
				}else{
					out.print("false");
				}
			}else{
				out.print("false");
			}
		}else{
			outPutJson("overTime");
		}
		return null;
	}
	
	private PmRRolePermissioin prrp;
	public PmRRolePermissioin getPrrp() {
		return prrp;
	}
	public void setPrrp(PmRRolePermissioin prrp) {
		this.prrp = prrp;
	}
	/**
	 * 判断是否可以删除权限
	 * @return
	 * zhouya
	 * 2012-07-18
	 */
	public String ifRolePermission() throws Exception{
		//检测session用户是否为空
		if(!overTime()){
			//检测参数对象不为空
			if(prrp!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("RolePermissionPid");
				//检测RolePermissionPid字段不为空
				if(IfNullNature.natureIfNull(listObj, prrp)){
					//true 执行删除操作，false返回“false”
					if(this.rolePermissionService.ifRolePermission(prrp)){
						PmPermission permission=new PmPermission();
						permission.setPermissionId(prrp.getRolePermissionPid());
						permissionAction.setPermission(permission);
						return permissionAction.delPermission();
					}else{
						out.print("false");
					}
				}else{
					out.print("false");
				}
			}else{
				out.print("false");
			}
		}else{
			outPutJson("overTime");
		}
		return null;
	}
	
	/**
	 * 判断权限节点是否绑定了角色
	 * true没有绑定角色 false绑定了角色
	 */
	public void ifrolePermission() throws Exception{
		//session中的用户是否为空
		if(!overTime()){
			//检测是否绑定了角色true 返回“true” false 返回“false”
			if(this.rolePermissionService.ifRolePermission(prrp)){
				outPutJson("true");
			}else{
				outPutJson("false");
			}
		}else{
			outPutJson("overTime");
		}
	}
}
