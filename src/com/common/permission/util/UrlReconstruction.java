package com.common.permission.util;
/**
 * 对url的参数过滤
 * 周亚
 * 2012-07-16
 */
public class UrlReconstruction {

	/**
	 * 把url的参数值变成问号
	 * @param url
	 * @return
	 */
	public static String Url(String url){
		//检测url参数不为空
		if(url!=null&&!"".equals(url)){
			String [] urls=url.split("\\?");
			//通过“?”来进行字符串的截取
			if(urls!=null&&urls.length>1){
				String [] parameters=urls[1].split("&");
				StringBuffer sb=new StringBuffer();
				sb.append(urls[0]);
				sb.append("?");
				//循环把参数值替换成"?"
				for (int j=0;j<parameters.length;j++) {
					String parameter=parameters[j].substring(0, parameters[j].indexOf("="));
					sb.append(parameter+"=?");
					if(j!=parameters.length-1)
					sb.append("&");
				}
				return sb.toString();
			}
			return urls[0];
		}
		return url;
	}
	
	public static void main(String[] args) {
	}
}
