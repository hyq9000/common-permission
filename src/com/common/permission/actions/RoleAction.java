package com.common.permission.actions;

import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.common.dbutil.Paging;
import com.common.log.Log;
import com.common.permission.entity.*;
import com.common.permission.service.RolePermissionService;
import com.common.permission.service.RoleService;
import com.common.permission.util.IfNullNature;
/**
 * 角色操作action包括增加删除修改列表展示
 * 姓名:周亚
 * 时间：2012-07-09
 *
 */
public class RoleAction extends InitAction{

	public RoleAction() throws Exception {
		super();
	}

	private  RoleService roleService;
	
	private RolePermissionService rolePermissionService;
	
	public RolePermissionService getRolePermissionService() {
		return rolePermissionService;
	}


	public void setRolePermissionService(RolePermissionService rolePermissionService) {
		this.rolePermissionService = rolePermissionService;
	}


	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	
	/**
	 * 分页获取每页的角色记录
	 * @return 跳转到列表展示页面
	 * 2012-07-09
	 */
	
	@SuppressWarnings("unchecked")
	public String getAllRoleJson() throws Exception{
		try {
			//int count=roleService.roleSize(); 有了dbutil的查总行机制，此代码可去了;
			int count=50;
			@SuppressWarnings("deprecation")
			Paging paging=new Paging(limit, (start/limit+1) , count);
			List<Map> rolelist=null;
			try {
				rolelist = roleService.roleList(paging);
				//将因DBUTIL机制加在LIST最后一个的分页对象,除去；
				if(rolelist!=null ){
					int size= rolelist.size();
					Object pg=rolelist.get(size-1);
					if(pg instanceof Paging)
						rolelist.remove(size-1);
						count=((Paging)pg).getTotalCount();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("countSize", count);
			map.put("items", rolelist);
			outPutJson(map);
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("错误:",e);
		}
		return null;
	}
	
	
	public RoleService getRoleService() {
		return roleService;
	}

	public void setPr(PmRole pr) {
		this.pr = pr;
	}

	//获取从页面返回的数据
	private PmRole pr;
	
	
	public PmRole getPr() {
		return pr;
	}

	/**
	 * 增加新的角色
	 * @return 返回是否成功 true成功 false失败
	 * 2012-07-09
	 */
	@Log(content="添加角色")
	public String saveRole() throws Exception{
		//检测session是否失效 true执行功能 否则返回overTime字符串
		if(!overTime()){
			//检测传入参数是否为null true功能操作 false不返回结果 （这个情况比较少）
			if(pr!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("RoleName");
				listObj.add("RoleStatus");
				//检测参数值是否为空或者为null true执行添加功能的操作 false返回“false”
				if(IfNullNature.natureIfNull(listObj, pr)){
					//检测添加操作是否成功 true修改appliction中的managerRoles数据 false返回“false”
					if(roleService.saveRole(pr)){
						//INIT_SYSTEM_DATA这个常量值，保持与WebContextUtil中的INIT_SYSTEM_DATA一致;
						Object obj = ServletActionContext.getServletContext().getAttribute("INIT_SYSTEM_DATA");
						if(obj==null){
							obj=new HashMap<String, Object>();
							ServletActionContext.getServletContext().setAttribute("INIT_SYSTEM_DATA",obj);
						}
						//修改appliction中的管理员权限列表
						Map<String,Object> map = (Map<String,Object>)obj;
						map.put("managerRoles", roleService.getRoleType("cloud_manager"));
						ServletActionContext.getServletContext().setAttribute("INIT_SYSTEM_DATA", map);
						
						//通知第三方刷新权限缓存 hyq 2014-06-04
						ActionUtils.flushPermission(ServletActionContext.getServletContext());
						outPutJson("true");
					}else
						outPutJson("false");
				}else{
					outPutJson("false");
				} 
			}
		}else{
			outPutJson("overTime");
		}
		return null;
	}
	
	/**
	 * 修改角色信息
	 * @return 返回是否成功
	 * 2012-07-09
	 */
	@Log(content="修改角色")
	public String updRole() throws Exception{
		//检测传入参数是否为null true功能操作 false不返回结果 （这个情况比较少）
		if(pr!=null){
			List<String> listObj=new ArrayList<String>();
			listObj.add("RoleName");
			listObj.add("RoleStatus");
			listObj.add("RoleId");
			//检测参数值是否为空或者为null true执行添加功能的操作 false返回“false”
			if(IfNullNature.natureIfNull(listObj, pr)){
				//检测修改操作是否成功 true修改appliction中的managerRoles数据 false返回“false”
				if(roleService.updRole(pr)){			
					
					//Object obj = ServletActionContext.getServletContext().getAttribute("initSystemData");
					//INIT_SYSTEM_DATA这个常量值，保持与WebContextUtil中的INIT_SYSTEM_DATA一致;
					Object obj = ServletActionContext.getServletContext().getAttribute("INIT_SYSTEM_DATA");
					if(obj==null){
						obj=new HashMap<String, Object>();
						ServletActionContext.getServletContext().setAttribute("INIT_SYSTEM_DATA",obj);
					}
					//修改appliction中的管理员权限列表
					Map<String,Object> map = (Map<String,Object>)obj;
					map.put("managerRoles", roleService.getRoleType("cloud_manager"));
					ServletActionContext.getServletContext().setAttribute("INIT_SYSTEM_DATA", map);
					
					//通知第三方刷新权限缓存 hyq 2014-06-04
					ActionUtils.flushPermission(ServletActionContext.getServletContext());
					outPutJson("true");
				}else{
					outPutJson("false");
				}
			}else{
				outPutJson("false");
			}
		}
		return null;
	}
	
	
	/**
	 * 删除或者停用角色
	 * @return 返回是否成功
	 * 2012-07-09
	 */
	public String delRole() throws Exception{
		//检测session是否失效 true执行功能 否则返回overTime字符串
		if(!overTime()){
			//检测传入参数是否为null true功能操作 false不返回结果 （这个情况比较少）
			if(pr!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("RoleId");
				//检测参数值是否为空或者为null true执行添加功能的操作 false返回“false”
				if(IfNullNature.natureIfNull(listObj, pr)){
					//检测删除操作是否成功 true修改appliction中的managerRoles数据 false返回“false”
					if(roleService.delRole(pr)){
						//删除权限和角色的关系信息
						rolePermissionService.delRolePermission(pr);
						//Object obj = ServletActionContext.getServletContext().getAttribute("initSystemData");
						//INIT_SYSTEM_DATA这个常量值，保持与WebContextUtil中的INIT_SYSTEM_DATA一致;
						Object obj = ServletActionContext.getServletContext().getAttribute("INIT_SYSTEM_DATA");
						if(obj==null){
							obj=new HashMap<String, Object>();
							ServletActionContext.getServletContext().setAttribute("INIT_SYSTEM_DATA",obj);
						}
						//修改appliction中的管理员权限列表
						Map<String,Object> map = (Map<String,Object>)obj;
						map.put("managerRoles", roleService.getRoleType("cloud_manager"));
						ServletActionContext.getServletContext().setAttribute("INIT_SYSTEM_DATA", map);
						
						//通知第三方刷新权限缓存 hyq 2014-06-04
						ActionUtils.flushPermission(ServletActionContext.getServletContext());
						return "true";
					}else{
						return "false";
					}
				}else{
					return "false";
				}
			}
		}else{
			outPutJson("overTime");
		}
		return "false";
	}
	
	/**
	 * 获取role的所有权限
	 * 周亚
	 * 2012-07-16
	 */
	public String getRoleId() throws Exception{
		//检测session是否失效 true执行功能 否则返回overTime字符串
		if(!overTime()){
			//检测传入参数是否为null true功能操作 false不返回结果 （这个情况比较少）
			if(pr!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("RoleId");
				//检测参数值是否为空或者为null true执行添加功能的操作 false返回“false”
				if(IfNullNature.natureIfNull(listObj, pr)){
					//根据编号获取角色信息
					pr=roleService.getRoleId(pr);
					StringBuffer sb=new StringBuffer();
					sb.append(pr.getRoleName()).append(",");
					sb.append(pr.getRoleDescribe()).append(",");
					sb.append(pr.getRoleStatus()).append(",");
					sb.append(pr.getRoleId());
					outPutJson(sb.toString());
					return null;
				}
			}
		}else{
			outPutJson("overTime");
		}
		return null;
	}
	
	private Integer start;//起始条数
	
	private Integer limit;//每页显示的条数
	
	public Integer getStart() {
		return start;
	}


	public void setStart(Integer start) {
		this.start = start;
	}


	public Integer getLimit() {
		return limit;
	}


	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public String roleids="";
	/**
	 * 检测角色是否有用户绑定，如果有绑定不能删除，没有绑定可以删除
	 * @return
	 * 
	 */
	@Log(content="删除角色")
	public String boolUserRole() throws Exception{
		try {
			ServletActionContext.getRequest().setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw e;
		}
		//检测session是否失效 true执行功能 否则返回overTime字符串
		if(!overTime()){
			//检测传入参数是否为null true功能操作 false不返回结果 （这个情况比较少）
			if(roleids!=null&&!"".equals(roleids)){
				String bool="true";
				pr=new PmRole();
				pr.setRoleId(Integer.parseInt(roleids));
				//检测是否有绑定用户 true执行删除操作 false 返回“false”
				if(roleService.ifRoleUser(pr)){
					if(!delRole().equals("true")){
						bool="false";
					}
				}else{
					bool="false";
				}
				outPutJson(bool);
			}else{
				outPutJson("false");
			}
		}else{
			outPutJson("overTime");
		}
		return null;
	}
	
	/**
	 * 判断用户是否绑定了该角色
	 * 绑定了false 没有绑定 true
	 */
	public void ifroleUserBoolean() throws Exception{
		//检测用户是否绑定角色 true返回“true” false返回“false”
		if(roleService.ifRoleUser(pr)){
			outPutJson("true");
		}else{
			outPutJson("false");
		}
	}
	
	
	public String getRoleids() {
		return roleids;
	}


	public void setRoleids(String roleids) {
		this.roleids = roleids;
	}


	/**
	 * 获取一个不带分页的角色集合，用来做用户分配角色
	 * @return 集合不为空返回集合信息， 为空返回false字符串
	 */
	public String getAllUserRole() throws Exception{
		List<Map> list = this.roleService.getAllUserRole();
		//检测返回集合是否为空 true返回集合 false返回“false”
		if(list!=null&&list.size()>0)
			outPutJson(list);
		else{
			outPutJson("false");
		}
		return null;
	}
	
	/**
	 * 判断角色名称是否存在，
	 * @return 返回null  如果存在输出true否则输出false
	 */
	public String ifRoleName() throws Exception{
		//检测session是否失效 true执行功能 否则返回overTime字符串
		if(!overTime()){
			//检测传入参数是否为null true功能操作 false不返回结果 （这个情况比较少）
			if(pr!=null){
				List<String> listObj=new ArrayList<String>();
				listObj.add("RoleName");
				//检测参数值是否为空或者为null true执行添加功能的操作 false返回“false”
				if(IfNullNature.natureIfNull(listObj, pr)){
					//检测角色名称是否存在 true返回“true” false返回“false”
					if(roleService.ifRoleName(pr)){
						outPutJson("true");
					}else{
						outPutJson("false");
					}
				}else{
					outPutJson("false");
				}
			}
		}else{
			outPutJson("overTime");
		}
		return null;
	}
	
	
	/**
	 * 批量生成角色
	 * zhouya
	 * @throws Exception
	 */
	@Log(content="批量生成产品角色")
	public void batchCreateRole() throws Exception{
		PrintWriter out=  ServletActionContext.getResponse().getWriter();
		try {
			int count=roleService.massProductionRole();
			//返回添加的总记录数
			out.print(count);
		} catch (Exception e) {
			out.print("false");
			Logger.getLogger(this.getClass()).error("错误:",e);
		}
	}
	
	/**
	 * 角色的启用或者停用操作
	 * @throws Exception
	 */
	@Log(content="角色启用停用")
	public void updateStatus() throws Exception{
		PrintWriter out=  ServletActionContext.getResponse().getWriter();
		if(pr!=null){
			try {
				this.roleService.booleanStatus(pr);
				//通知第三方刷新权限缓存 hyq 2014-06-04
				ActionUtils.flushPermission(ServletActionContext.getServletContext());
				out.print("true");
			} catch (Exception e) {
				out.print("false");
				Logger.getLogger(this.getClass()).error("错误:",e);
			}
		}
	}
	
	
	private String returnJsonString(boolean bool,String msg,String op){
		StringBuffer sb=new StringBuffer();
		sb.append("({");
		sb.append("success:").append(bool).append(",");
		if(msg!=null)
			sb.append("msg:").append("'").append(msg).append("'").append(",");
		if(op!=null)
			sb.append("op:").append(msg).append(",");
		sb.append("})");
		return sb.toString();
	}
}
