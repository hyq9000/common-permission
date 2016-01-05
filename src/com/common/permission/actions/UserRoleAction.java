package com.common.permission.actions;

import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;








import java.util.Scanner;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.config.DefaultSettings;
import org.apache.struts2.json.JSONUtil;

import com.common.dbutil.Paging;
import com.common.log.Log;
import com.common.permission.entity.PmRUserRole;
import com.common.permission.service.ExteriorService;
import com.common.permission.service.RoleUserService;
/**
 * 用户角色action，主要是获取用户列表和给用户分配角色
 * @author zhouya
 */
public class UserRoleAction extends InitAction {
	private String allUserUrl;//获取三方系统的用户数据的url;
	private String conditionNames;//第三方系统的用户查询条件名称集，以','隔开
	public void setAllUserUrl(String allUserUrl) {
		this.allUserUrl = allUserUrl;
	}
	public void setConditionNames(String conditionNames) {
		this.conditionNames = conditionNames;
	}
	public UserRoleAction() throws Exception {
		super();
	}

	private int start;
	
	private int limit=10;
	
	private ExteriorService exteriorService;
	public void setExteriorService(ExteriorService exteriorService) {
		this.exteriorService = exteriorService;
	}

	private RoleUserService roleUserService;
	
	public RoleUserService getRoleUserService() {
		return roleUserService;
	}
	public void setRoleUserService(RoleUserService roleUserService) {
		this.roleUserService = roleUserService;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	
	/*
	 * 获取用户列表
	 */
	public String getAllUser() throws Exception{
		String dataJson=ServletActionContext.getRequest().getParameter("dataJson");
		String conditionURI="";
		if(dataJson!=null&& !dataJson.equals("")){
			Map obj=(Map)JSONUtil.deserialize(dataJson);
			String[] con_name_tmp=conditionNames.split(",");
			
			if(con_name_tmp!=null && con_name_tmp.length>=0){
				for(int i=0;i<con_name_tmp.length;i++){
					conditionURI+=con_name_tmp[i]+"="+obj.get(con_name_tmp[i])+"&";
				}
			}
		}
		
		//Paging paging=new Paging(limit, start/limit+1);
		//List<Map<String, String>> listMap = exteriorService.getAllUser(paging);
		
		/*
		 * 分页获取三方系统的所有用户ID及用户姓名的数据；
		 */
		URL url=new URL(allUserUrl+"?"+conditionURI+"pageNo="+(start/limit+1)+"&pageSize="+limit);
		URLConnection con=url.openConnection();
		Scanner reader=new Scanner(con.getInputStream(),"utf-8");
		String json="";
		while(reader.hasNext())
			json+=reader.next();
		Object rs=JSONUtil.deserialize(json);
		List<Map<String, String>> listMap=(List<Map<String, String>>)rs;
		Object countSize=listMap.get(listMap.size()-1).get("totalCount");//直接到结果里找到DBUTIL算出的总行数来;
		/*这是以前在DBUTIL还没有提供查取当前条件下总数的情况下，提供的临时处理方式；现在弃用
		String countSize=listMap.get(listMap.size()==0?0:listMap.size()-1).get("total");*/
		//将已经分配置的角色名称取出放到数据行中；
		listMap=roleUserService.getUserRoleName(listMap);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("countSize", countSize);
		map.put("items", listMap);
		outPutJson(map);
		return null;
	}
	
	
	//用来存储传入的单个或多个用户编号
	private String userIds;
	
	private String roleIds;
	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}
	
	/*
	 * 给用户分配角色
	 */
	@Log(content="用户分配角色")
	public String setUserRole() throws Exception{
		//检测session是否为空 true执行操作 false返回“overTime”
		if(!overTime()){
			String []roleId=roleIds.split(",");
			PmRUserRole prur=new PmRUserRole();
			String [] strName=userIds.split("-_-");
			prur.setRoleUserUid(strName[0]);
			//删除所有的角色用户关系
			this.roleUserService.delUserId(prur);			
			if(roleId!=null&&roleId.length>0){
				//循环添加新的角色用户关系
				for (String strRole : roleId) {
					if(strRole!=null&&!"".equals(strRole)){
						PmRUserRole prur_tmp=new PmRUserRole();
						prur_tmp.setUserName(strName[1]);
						prur_tmp.setRoleUserRid(Integer.parseInt(strRole));
						prur_tmp.setRoleUserUid(strName[0]);
						roleUserService.saveRoleUser(prur_tmp);
					}
				}
				//通知第三方刷新权限缓存 hyq 2014-06-04
				ActionUtils.flushPermission(ServletActionContext.getServletContext());
			}
			out.print("true");
		}else{
			outPutJson("overTime");
		}
			return null;
	}
	
	private String userid;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
	/*
	 * 获取用户已经拥有的权限
	 */
	public void getRole() throws Exception{
		//检测session是否为空 true执行操作 false返回“overTime”
		if(!overTime()){
			//检测参数是否为空
			if(userid!=null&&!"".equals(userid)){
				List<PmRUserRole> list = this.roleUserService.getuserRole(userid);
				StringBuffer sb=new StringBuffer();
				String roles="";
				//检测集合是否为null true构建用“,”隔开的字符串 false返回空字符串
				if(list!=null&&list.size()>0){
					//循环构建字符串
					for (PmRUserRole pmRUserRole : list) {
						sb.append(pmRUserRole.getRoleUserRid()).append(",");
					}
					//去掉最后最后一个“,”
					if(sb.length()>0){
						roles=sb.substring(0, sb.length()-1);
					}
				}
				out.print(roles);
			}
		}else{
			outPutJson("overTime");
		}
	}
}
