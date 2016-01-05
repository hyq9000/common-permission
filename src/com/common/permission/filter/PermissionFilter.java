package com.common.permission.filter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.common.log.UserName;
import com.common.permission.service.PermissionService;
import com.common.permission.util.UrlReconstruction;
/**
 * 过滤器，权限控制
 * @author zhouya
 * 2012-12-05
 */
public class PermissionFilter implements Filter {

	WebApplicationContext wac =null;
	ServletContext servletContext=null;
	public void init(FilterConfig filterConfig) throws ServletException {
		//获取servler上下文
		servletContext= filterConfig.getServletContext();
		wac=WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext); 
		
	}
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest)request;
			HttpServletResponse res = (HttpServletResponse)response;
			HttpSession session = req.getSession();
			String path=req.getContextPath();
			String sessionName=servletContext.getInitParameter("sessionName");
			Object sessionUser = session.getAttribute(sessionName);
			//判断用户是否登录
			if(sessionUser!=null){
				String userStr=getPermission(req, res, sessionUser);
				if("manager".equals(userStr)){
					req.getRequestDispatcher(path+"/permission/error.jsp").forward(request, response);//没有权限访问
				}else if("account".equals(userStr)){
					req.getRequestDispatcher(path+"/permission/error.jsp").forward(request, response);//没有权限访问
				}else if("error".equals(userStr)){
					req.getRequestDispatcher(path+"/permission/error.jsp").forward(request, response);//没有权限访问
				}
			}else{
				//没有登录跳转到登录页面
				req.getRequestDispatcher(path+"/ccore/login.html").forward(request, response);
			}
		} catch (Exception e) {
			
		}
	}

	public void destroy() {
		
	}
	/**
	 * 检测用户是否有权限访问
	 * sessionObj session中的用户
	 * 返回：“manager”管理员权限 “account”用户权限
	 */
	private String getPermission(HttpServletRequest req,HttpServletResponse res,Object sessionObj) throws Exception{
		String url=req.getContextPath();  //获取项目名称
		url = url + req.getServletPath(); //获取项目路径
		String queryString = req.getQueryString();//获取传入参数
		if(queryString!=null&&!"".equals(queryString)){
			url=url+"?"+queryString;
		}
		String urlReconsruction=UrlReconstruction.Url(url);//过滤url

		if(sessionObj!=null){//判断session是否有值
			Object userId=null;
			//取得该登录用词的ID值
			try {
				Method[] ms=Object.class.getMethods();
				for(Method m :ms){
					if(m.getAnnotation(UserName.class)!=null){
						userId=m.invoke(sessionObj, null).toString();
					}
				}
			} catch (Exception e) {		
				e.printStackTrace();
			}
			//判断用户是否有权限			
			if(getSession(userId.toString(), urlReconsruction,  req)){
				return "account";
			}
			
			//TODO:修改前台的关于account与manage
			/*try {
				CcoreManager ccoreManager=(CcoreManager)sessionObj;
				//判断用户是否有权限			
				if(getSession(ccoreManager.getManagerId().toString(), urlReconsruction,  req)){
					return "manager";
				}
			} catch (Exception e) {
				CcoreAccountInfo ccoreAccountInfo=(CcoreAccountInfo)sessionObj;
				//判断用户是否有权限			
				if(getSession(ccoreAccountInfo.getAccountMail(), urlReconsruction,  req)){
					return "account";
				}
			}*/
			
		}
		return "error";
	}

	/**
	 * 到数据库中查找用户的权限
	 * userid 用户编号
	 * url 权限代码
	 * 返回：true有权限 false没有权限
	 */
	private boolean getSession(String userid,String url,HttpServletRequest req) throws Exception{
		PermissionService permissionService = (PermissionService)wac.getBean("permissionService"); //获取spring对象
		String sessionTage=servletContext.getInitParameter("sessionTage");
		//从session里面取每个用户的权限
		Object obj = req.getSession().getAttribute(sessionTage);
		//有就直接判断
		if(obj!=null){
			Map<String,List<Map<String,Map<String,String>>>> map = (Map<String,List<Map<String,Map<String,String>>>>)obj;
			if(map!=null&&map.size()>0){
				Object objUri = map.get(userid);
				if(objUri!=null){
					List<Map<String,Map<String,String>>> list=(List<Map<String,Map<String,String>>>)objUri;
					for (Map<String, Map<String, String>> map2 : list) {
						if(url.equals(map2.get("PERMISSION_URI"))){
							return true;
						}
					}
				}else{
					List<Map> listUri = permissionService.boolUserPermission(url, userid);
					Map<String,List<Map>> mapUri=new HashMap<String,List<Map>>();
					if(listUri!=null&&listUri.size()>0){
						mapUri.put(userid, listUri);
						//然后存入session中
						req.getSession().setAttribute(sessionTage,mapUri);
						for (Map mapUris : listUri) {
							if(url.equals(mapUris.get("PERMISSION_URI"))){
								return true;
							}
						}
					}
				}
			}
		}else{
			//没有就直接到数据库中取
			List<Map> listUri = permissionService.boolUserPermission(url, userid);
			Map<String,List<Map>> mapUri=new HashMap<String,List<Map>>();
			if(listUri!=null&&listUri.size()>0){
				mapUri.put(userid, listUri);
				//然后存入session中
				req.getSession().setAttribute(sessionTage,mapUri);
				for (Map mapUris : listUri) {
					if(url.equals(mapUris.get("PERMISSION_URI"))){
						return true;
					}
				}
			}
		}
		return false;
	}
}
