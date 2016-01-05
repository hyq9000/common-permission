<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>权限列表</title>
<style>
		html { overflow-y: auto;overflow-x: hidden; } 
	</style>
 <script type="text/javascript" src="<%=path%>/resources/js/jquery-1.7.2.min.js"></script>
 <link rel="stylesheet" type="text/css"	href="<%=path %>/resources/js/extjs/resources/css/ext-all.css"></link>
 <script type="text/javascript" src="<%=path %>/resources/js/extjs/ext-all.js"></script>
 <script type="text/javascript" src="<%=path%>/resources/js/common.js"></script>
 <script type="text/javascript" src="<%=basePath %>/permissions/resources/user-js/permission.js"></script>
 <script type='text/javascript' src='<%=basePath %>/resources/js/extjs/locale/ext-lang-zh_CN.js' ></script>
<style type="text/css">
.STYLE2 {color: #FF0000}
a:link {
	color: #0000CC;
	text-decoration: none;
}
a:visited {
	text-decoration: none;
	color: #0000CC;
}
a:hover {
	text-decoration: none;
	color: #0000CC;
}
a:active {
	text-decoration: none;
	color: #0000CC;
}
</style>
</head>

<body style='padding:0px;'>
<div id="tree-div"></div>
  <div id="addpermission" style="display:none">
  	<table width="100%" border="0">
  <tr>
    <td width="40%" height="30" align="right" valign="middle">权限名称:</td>
    <td width="60%" align="left" valign="middle"><input name="permission.permissionName" maxlength="10" id="permissionName">
      <span class="STYLE2">*</span></td>
  </tr>
  <tr>
    <td align="right" valign="middle">权限url:</td>
    <td height="30" align="left" valign="middle"><input name="permission.permissionUri" maxlength="225" id="permissionUri">
      </td>
  </tr>
  <tr>
    <td align="right" valign="middle">&nbsp;</td>
    <td height="30" align="left" valign="middle"><span id='roleadd' ></span><span id='quxiao' ></span>
	<input type="hidden" id='ParentId' name="permission.permissionParentId" value=""></td>
  </tr>
</table>
  </div>
    <div id="updpermission" style="display:none">
  	<table width="100%" border="0">
  <tr>
    <td width="40%" height="30" align="right" valign="middle">权限名称:</td>
    <td width="60%" align="left" valign="middle"><input name="permission.permissionName" maxlength="10" id="updpermissionName">
      <span class="STYLE2">*</span></td>
  </tr>
  <tr>
    <td align="right" valign="middle">权限url:</td>
    <td height="30" align="left" valign="middle"><input name="permission.permissionUri" maxlength="225" id="updpermissionUri">
      </td>
  </tr>
  <tr>
    <td align="right" valign="middle">&nbsp;</td>
    <td height="30" align="left" valign="middle"><span id='roleupd'></span>&nbsp;&nbsp;<span id='quxiaos' ></span>
	<input type="hidden" id='updParentId' name="permission.permissionParentId" value="">
	<input type="hidden" id='updId' name="permission.permissionParentId" value=""></td>
  </tr>
</table>
  </div>
<script type="text/javascript" src="<%=basePath %>/resources/js/lock.js"></script>
<link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/css/lock.css" />
<div id="roleuser"></div>
<div id="lockmask" class="mask"></div>
<div id="lockmaskmsg" class="mask-msg">
		正在处理，请稍候。。。	
</div>
</body>
</html>