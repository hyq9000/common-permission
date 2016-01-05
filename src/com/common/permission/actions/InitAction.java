package com.common.permission.actions;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;

import com.common.web.WebContextUtil;
/**
 * action的一些公共的资源比如request，session，response，out等
 * 创建时间:2012-08-16
 * @author zhouya
 *
 */
public class InitAction {

	private HttpServletRequest request;
	
	private HttpServletResponse response;
	
	private HttpSession session;
	
	PrintWriter out;
	
	//构造方法获取一些公共资源
	public InitAction() throws Exception{
		try {
			request=ServletActionContext.getRequest();
			response=ServletActionContext.getResponse();
			session=request.getSession();
			out = response.getWriter();
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			Logger.getLogger(this.getClass()).error("错误:",e);
		}
	}
	
	//输出json对象
	public void outPutJson(Object obj) throws Exception{
		try {
			out.print(JSONUtil.serialize(obj));
		} catch (JSONException e) {
			Logger.getLogger(this.getClass()).error("错误:",e);
		}
	}
	
	//输出字符串
	public void outPutJson(String str) throws Exception{
		out.print(str);
	}
	
	
	
	public PrintWriter getOut() {
		return out;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpSession getSession() {
		return session;
	}
	
	/*
	 * 获取session中的用户信息
	 * @return 有返回true否则返回false
	 */
	public boolean overTime(){
		//TODO:用户登陆验证
		/*Object obj = session.getAttribute(WebContextUtil.USER);
		if(obj==null){
			return true;
		}*/
		return false;
	}
}
