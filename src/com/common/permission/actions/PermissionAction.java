package com.common.permission.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.DefaultSettings;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.springframework.context.annotation.Scope;

import com.common.log.Log;
import com.common.permission.entity.*;
import com.common.permission.service.PermissionService;
import com.common.permission.util.IfNullNature;
/**
 * 本action主要是对权限的访问及操作
 * 创建时间:2012-07-16
 * @author 周亚
 *	
 */
@Scope("prototype")
public class PermissionAction extends InitAction{

	
	public PermissionAction() throws Exception {
		super();
	}



	private  PermissionService permissionService;
	


	public PermissionService getPermissionService() {
		return permissionService;
	}


	public void setPermissionService(PermissionService permissionService) {
		this.permissionService = permissionService;
	}


	
	private PmRUserRole prur;
	
	
	public PmRUserRole getPrur() {
		return prur;
	}


	public void setPrur(PmRUserRole prur) {
		this.prur = prur;
	}
	//权限的父节点编号
	private String id="-1";

	
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	
	private String userid="123";
	
	public String getUserid() {
		return userid;
	}


	public void setUserid(String userid) {
		this.userid = userid;
	}


	PmPermission pp=new PmPermission();

	/*
	 * 根据用户来获取用户的所有权限
	 * @return 返回null 但是会输出一个json对象
	 */
	public String getUserPermission() throws Exception{
		try {
			prur=new PmRUserRole();
			prur.setRoleUserUid(userid);
			pp.setPermissionParentId(Integer.parseInt(id));
			//检测传入PmRUserRole是否为空 true执行获取用户权限操作
			if(prur!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("RoleUserUid");
				List<String> listPP=new ArrayList<String>();
				listPP.add("PermissionParentId");
				//检测RoleUserUid，PermissionParentId字段是否为空 true执行获取用户权限操作
				if(IfNullNature.natureIfNull(listObj, prur)&&IfNullNature.natureIfNull(listPP,pp)){
					List<PmPermission> listPermission = permissionService.getUserPermission(prur,pp);
					List<Tree> list=new ArrayList<Tree>();
					//循环创建tree对象
					for (PmPermission pmPermission : listPermission) {
						Tree tree=new Tree();
						tree.setId(pmPermission.getPermissionId().toString());
						tree.setText(pmPermission.getPermissionName());
						tree.setCls("folder");
						list.add(tree);
					}
					outPutJson(list);
				}
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("错误:",e);
		}
		return null;
	}
	
	/*
	 * 根据用户编号获取用户的所有权限
	 * @return 返回一个json
	 */
	@SuppressWarnings("rawtypes")
	public String getUserPermissions() throws Exception{
		try {
			prur=new PmRUserRole();
			prur.setRoleUserUid(userid);
			//检测传入PmRUserRole是否为空 true获取用户所有的权限
			if(prur!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("RoleUserUid");
				//检测RoleUserUid字段是否为空  true获取用户所有的权限
				if(IfNullNature.natureIfNull(listObj, prur)){
					List<Map> listPermission = permissionService.getUserPermission(prur);
					Map<String,Object> map=new HashMap<String, Object>();
					//检测用户是否有权限，true返回用户权限集合
					if(listPermission!=null&&listPermission.size()>0){
						map.put("root", listPermission);
						outPutJson(map);
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("错误:",e);
		}
		return null;
	}
	
	
	
	
	/*
	 * 获取所有的权限
	 * @return 
	 */
	public String getAllPermission() throws Exception{
		try {
			List<PmPermission> listPermission = permissionService.getPermission();
			outPutJson(listPermission);
			return null;
		} catch (Exception e) {
			throw e;
		}
	}
	
	private String str;
	
	public String getStr() {
		return str;
	}


	public void setStr(String str) {
		this.str = str;
	}


	
	private PmRole pmRole;
	
	public PmRole getPmRole() {
		return pmRole;
	}


	public void setPmRole(PmRole pmRole) {
		this.pmRole = pmRole;
	}


	/*
	 * 根据角色编号获得角色的所有权限
	 * @return 返回一个权限json
	 */
	public String getRolePermission() throws Exception{
		try {
			List<Map<String,Object>> listmap=new ArrayList<Map<String,Object>>();
			List<PmPermission> listPermission = permissionService.getPermission();
			//检测传入pmRole是否为空 true获取所有权限
			if(pmRole!=null){
				ArrayList<String> listObj = new ArrayList<String>();
				listObj.add("RoleId");
				//检测RoleId字段是否为空  true获取所有权限
				if(IfNullNature.natureIfNull(listObj, pmRole)){
					List<PmPermission> list = this.permissionService.getRolePermission(pmRole);
					//循环检测根据父级权限编号，获取子级权限
					for (PmPermission pmPermission : listPermission) {
						//检测是否是父级编号 true添加到集合中
						if(pmPermission.getPermissionParentId().toString().equals(id)){
							Map<String,Object> map=new HashMap<String, Object>();
							map.put("id", pmPermission.getPermissionId());
							map.put("text", pmPermission.getPermissionName());
							map.put("expanded", true);
							map.put("pid", pmPermission.getPermissionParentId());
							//检测是否是末级节点
							if(!getParent(listPermission,pmPermission.getPermissionId())){
								map.put("leaf", true);
							}else{
								map.put("leaf", false);
							}
							//检测节点是否被角色选中
							if(list!=null&&getBox(list,pmPermission.getPermissionId())){
								map.put("checked", true);
							}else{
								map.put("checked", false);
							}
							listmap.add(map);
						}
					}
					String json = JSONUtil.serialize(listmap);
					outPutJson(json);
				}
			}
			
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("错误:",e);
		}
		return null;
	}
	
	
	private PmPermission permission=new PmPermission();
	
	
	
	public PmPermission getPermission() {
		return permission;
	}


	public void setPermission(PmPermission permission) {
		this.permission = permission;
	}


	/*
	 * 添加权限
	 * @return 返回到权限页面
	 */
	@Log(content="添加权限")
	public String savePermission() throws Exception{
		//检测session中的用户是否超时 treu返回“overTime”
		if(!overTime()){
			//检测参数对象是否为null true执行添加操作
			if(permission!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("PermissionParentId");
				listObj.add("PermissionName");
				//检测参数PermissionParentId，PermissionName字段是否为空 执行添加操作
				if(IfNullNature.natureIfNull(listObj, permission)){
					//是否添加成功 true 返回“true” false 返回“false”
					if(permissionService.savePermission(permission)){
						outPutJson("ture");
						//通知第三方刷新权限缓存 hyq 2014-06-04
						ActionUtils.flushPermission(ServletActionContext.getServletContext());
					}else{
						outPutJson("false");
					}
					
				}
			}
		}else{
			outPutJson("overTime");
		}
		return null;
	}
	/*
	 * 判断节点的名称是否存在
	 */
	public void boolPermissionName() throws Exception{
		//检测session中的用户是否超时 treu返回“overTime”
		if(!overTime()){
			//检测权限对象是否为null true执行数据库查询
			if(permission!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("PermissionName");
				//检测PermissionName字段是否为空 true执行数据库查询
				if(IfNullNature.natureIfNull(listObj, permission)){
					//true没有存在false存在
					if(permissionService.boolPermissionName(permission)){
						outPutJson("true");
					}else{
						outPutJson("false");
					}
				}
			}
		}else{
			outPutJson("overTime");
		}
	}
	
	/*
	 * 根据权限名称获取权限
	 * @throws Exception
	 */
	public void getPermissionName() throws Exception{
		//检测session中的用户是否超时 treu返回“overTime”
		if(!overTime()){
			//检测权限对象是否为null true执行权限获取
			if(permission!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("PermissionName");
				//检测PermissionName字段是否为空 true执行权限获取
				if(IfNullNature.natureIfNull(listObj, permission)){
					PmPermission pm = permissionService.getPmPermission(permission.getPermissionName());
					//检测返回权限对象是否为空 true 返回“true” false返回权限编号
					if(pm==null){
						outPutJson("true");
					}else{
						outPutJson(pm.getPermissionId());
					}
				}
			}
		}else{
			outPutJson("overTime");
		}
	}
	
	
	/*
	 * 删除权限
	 */
	@Log(content="删除权限")
	public String delPermission() throws Exception{
		//检测session中的用户是否超时 treu返回“overTime”
		if(!overTime()){
			//检测权限对象是否为null true执行权限删除
			if(permission!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("PermissionId");
				//检测PermissionId字段是否为空 true执行删除操作
				if(IfNullNature.natureIfNull(listObj, permission)){
					PmPermission permissions = permissionService.getPermissionId(permission);
					permissionService.delPermission(permissions);
					//通知第三方刷新权限缓存 hyq 2014-06-04
					ActionUtils.flushPermission(ServletActionContext.getServletContext());
					outPutJson("true");
					return null;
				}
			}
		}else{
			outPutJson("overTime");
		}
		return null;
	}
	
	/*
	 * 修改获取需要修改的信息
	 */
	public String getPermissionId() throws Exception{
		//检测session中的用户是否超时 treu返回“overTime”
		if(!overTime()){
			//检测权限对象是否为null true执行权限修改
			if(permission!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("PermissionId");
				//检测PermissionId字段是否为空 true执行权限修改
				if(IfNullNature.natureIfNull(listObj, permission)){
					permission=permissionService.getPermissionId(permission);
					StringBuffer sb=new StringBuffer();
					sb.append(permission.getPermissionName()).append(",");
					sb.append(permission.getPermissionUri()).append(",");
					sb.append(permission.getPermissionParentId());
					outPutJson(sb.toString());
				}
			}
		}else{
			outPutJson("overTime");
		}
		return null;
	}
	
	/*
	 * 修改权限
	 */
	@Log(content="修改权限")
	public String updatePermission() throws Exception{
		//检测session中的用户是否超时 treu返回“overTime”
		if(!overTime()){
			//检测权限对象是否为null true执行权限修改
			if(permission!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("PermissionName");
				listObj.add("PermissionId");
				//检测PermissionName，PermissionId字段是否为空 true执行权限修改
				if(IfNullNature.natureIfNull(listObj, permission)){
					PmPermission perm = permissionService.getPermissionId(permission);
					perm.setPermissionName(permission.getPermissionName());
					perm.setPermissionUri(permission.getPermissionUri());
					//检测是否修改成功 true返回“true” false返回“false”
					if(permissionService.updPermission(perm)){
						//通知第三方刷新权限缓存 hyq 2014-06-04
						ActionUtils.flushPermission(ServletActionContext.getServletContext());
						outPutJson("true");
					}else{
						outPutJson("false");
					}
				}
			}
		}else{
			outPutJson("overTime");
		}
		return null;
	}
	
	private String type;
	public void setType(String type) {
		this.type = type;
	}


	public String getType() {
		return type;
	}


	/*
	 * 构建一个ext树对象tree然后构建成一个json
	 */
	public String getTree() throws Exception{
		List<PmPermission> list = permissionService.getPermission();
		List<Map<String,String>> listtree=new ArrayList<Map<String,String>>();
		//把所有的权限通过循环传入map中
		for (PmPermission pmPermission : list) {
			//检测是否有子集编号 true添加到集合中
			if(pmPermission.getPermissionParentId().toString().equals(id)){
				Map<String,String> map=new HashMap<String, String>();
				map.put("id", pmPermission.getPermissionId().toString());
				map.put("text", pmPermission.getPermissionName());
				map.put("leaf", "false");
				listtree.add(map);
			}
		}
		try {
			String json=JSONUtil.serialize(listtree);
			outPutJson(json);
		} catch (JSONException e) {
			Logger.getLogger(this.getClass()).error("错误:",e);
		}
		return null;
	}
	
	/*
	 * 判断树是不是有子节点
	 * @param list 树的集合
	 * @param id 树节点id
	 * @return 返回true有子节点 false没有子节点
	 * @throws Exception
	 */
	private boolean getParent(List<PmPermission> list,Integer id) throws Exception{
		for (PmPermission pmPermission : list) {
			if(pmPermission.getPermissionParentId().equals(id)){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * 判断树的节点是否被选中
	 * @param list 树的集合
	 * @param id 树的节点id
	 * @return 返回true被选中 false没有被选中
	 * @throws Exception
	 */
	private boolean getBox(List<PmPermission> list,Integer id) throws Exception{
		for (PmPermission pmPermission : list) {
			if(pmPermission.getPermissionId().equals(id)){
				return true;
			}
		}
		return false;
	}
	
	
	
}
