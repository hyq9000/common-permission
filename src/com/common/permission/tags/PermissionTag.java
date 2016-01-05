package com.common.permission.tags;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.common.log.UserName;
import com.common.permission.annotation.UserId;
import com.common.permission.service.PermissionService;
import com.common.permission.util.UrlReconstruction;

/**
 * 自定义标签，判断是否有权限访问 周亚 2012-07-16
 * 需要在web.xml配置文件中配置
 */
public class PermissionTag extends TagSupport {

	private static final long serialVersionUID = 1L;
	private String url;// 传入参数访问地址
	private String user;// 用户编号
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	// TODO: session名称到时候从web.xml中读取；

	@Override
	public int doStartTag() throws JspException {
		try {
			ServletContext servletContext = pageContext.getServletContext();
			WebApplicationContext wac = WebApplicationContextUtils	.getRequiredWebApplicationContext(servletContext);
			PermissionService permissionService = (PermissionService) wac.getBean("permissionService"); // 获取spring对象
			if (url != null && !"".equals(url)) {
				if (user != null && !"".equals(user)) {
					// 当用户传入后的操作
					// 主要是做url的参数值的替换，所有的参数值都会被替换成为 "?"
					String urlReconsruction = UrlReconstruction.Url(url);
					// 到数据库中查找用户是否有这个权限
					if (getSession(user, urlReconsruction, permissionService)) {
						return EVAL_BODY_INCLUDE;
					}
				} else {
					// 当用户没有传入时的操作（从session中获得用户信息）
					HttpSession session = pageContext.getSession();
					// 从web.xml中获取session名称
					String sessionName = pageContext.getServletContext()
							.getInitParameter("sessionName");
					Object sessionUser = session.getAttribute(sessionName);
					if (sessionUser != null) {
						Object userId=null;
						//取得该登录用词的ID值
						try {
							Method[] ms=sessionUser.getClass().getMethods();
							for(Method m :ms){
								if(m.getAnnotation(UserId.class)!=null){
									userId=m.invoke(sessionUser, null).toString();
									break;
								}
							}
						} catch (Exception e) {		
							e.printStackTrace();
						}
						String urlReconsruction = UrlReconstruction	.Url(url);
						if (getSession(userId.toString(), urlReconsruction,permissionService)) {
							return EVAL_BODY_INCLUDE;
						}					
						
						/*try {
							
							// 如果是CcoreManager对象查询本用户是否有这个权限
							
							
							CcoreManager ccoreManager = (CcoreManager) sessionUser;
							String urlReconsruction = UrlReconstruction	.Url(url);
							if (getSession(ccoreManager.getManagerId().toString(), urlReconsruction,permissionService)) {
								return EVAL_BODY_INCLUDE;
							}
						} catch (Exception e) {
							// 如果是CcoreAccountInfo对象查询本用户是否有这个权限
							CcoreAccountInfo ccoreAccountInfo = (CcoreAccountInfo) sessionUser;
							String urlReconsruction = UrlReconstruction	.Url(url);
							if (getSession(
									ccoreAccountInfo.getAccountLoginName(),
									urlReconsruction, permissionService)) {
								return EVAL_BODY_INCLUDE;
							}
						}*/
					}
				}
			}
		} catch (Exception e) {
		 e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private final String SESSION_NAME = "SESSION_PERMISSION_URI";

	/**
	 * 到数据库中查找用户的权限 userid 用户编号 url 权限代码 返回：true有权限 false没有权限
	 */
	private boolean getSession(String userid, String url,
			PermissionService permissionService) throws Exception {
		// 从session里面取每个用户的权限
		Object obj = null;
		// 有就直接判断
		if (obj == null) {
			// 没有就直接到数据库中取
			List<Map> listUri = permissionService.boolUserPermission(url,
					userid);
			Map<String, List<Map>> mapUri = new HashMap<String, List<Map>>();
			if (listUri != null && listUri.size() > 0) {
				mapUri.put(userid, listUri);
				// 然后存入session中
				// pageContext.getSession().setAttribute(SESSION_NAME,mapUri);
				for (Map mapUris : listUri) {
					if (url.equals(mapUris.get("PERMISSION_URI"))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 把权限url转换转换为用"#"隔开的字符串
	 * 
	 * @param listUri
	 *            权限的集合
	 * @return 返回一个字符串
	 */
	private String getUrlName(List<Map> listUri) {
		StringBuffer sb = new StringBuffer();
		sb.append("'");
		for (int i = 0; i < listUri.size(); i++) {
			if (i != 0) {
				sb.append("#");
			}
			sb.append(listUri.get(i).get("PERMISSION_URI"));
		}
		sb.append("'");
		return sb.toString();
	}
}
