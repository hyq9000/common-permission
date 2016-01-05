<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
/** 第三方面系统在查询用户时所需要的查询条件字段名及值：
	格式为json 
*/
String dataJson=request.getParameter("dataJson");
%>
<head>
 <script type="text/javascript" >
 	var dataJson='<%=dataJson%>';
 </script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户分配角色</title>
<style>
		html { overflow-y: auto;overflow-x: hidden; } 
		.detail{cursor: pointer;}
	</style>
</head>
<body style='padding:0px;'>
<div id="user_grid"></div>
 <script type="text/javascript" src="<%=path%>/resources/js/jquery-1.7.2.min.js"></script>
 <link rel="stylesheet" type="text/css"	href="<%=path %>/resources/js/extjs/resources/css/ext-all.css"></link>
 <script type="text/javascript" src="<%=path %>/resources/js/extjs/ext-all.js"></script>
<script type="text/javascript" src="<%=basePath%>/ccore/resources/js/extendExtjs.js"></script>
<script type="text/javascript" src="<%=path%>/resources/js/common.js"></script>
<script type="text/javascript" src="<%=basePath %>/permissions/resources/user-js/user.js"></script>
 <script type="text/javascript" src="<%=basePath %>/resources/js/lock.js"></script>
 <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/css/lock.css" />
  <script type='text/javascript' src='<%=basePath %>/resources/js/extjs/locale/ext-lang-zh_CN.js' ></script>
<div id="roleuser"></div>
 <div id="lockmask" class="mask"></div>
<div id="lockmaskmsg" class="mask-msg">
		正在处理，请稍候。。。	
</div>
</body>
</html>