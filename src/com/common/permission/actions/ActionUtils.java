package com.common.permission.actions;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.servlet.ServletContext;

import com.common.log.ExceptionLogger;

/**
 * 提供其他action公共的一些逻辑实现
 * @author hyq 2014-06-04
 *
 */
public class ActionUtils {

	final static String FLUSH_PERMISSION_URL="flushPermissioinUrl";
	/**
	 * 调用外部第三方系统的刷新权限的功能
	 * @return 如果第三方系统返回"true"，则返回true,否则返回false;
	 */
	public static void flushPermission(final ServletContext application){			
		new Thread(new Runnable() {			
			@Override
			public void run() {
				try {
					String urlStr=application.getInitParameter(FLUSH_PERMISSION_URL);
					URL url=new URL(urlStr);
					URLConnection con=url.openConnection();
					
					Scanner reader=new Scanner(con.getInputStream(),"utf-8");
					String rs="";
					while(reader.hasNext())
						rs+=reader.next();
				} catch (Exception e) {
					ExceptionLogger.writeLog(e,ActionUtils.class );
				}
			}
		}).start();
		return ;
	}
}
