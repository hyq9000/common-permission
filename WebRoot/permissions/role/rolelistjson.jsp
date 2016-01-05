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
<title>Insert title here</title>
<style>
.STYLE2 {color: #FF0000}
</style>
	<style>
		html { overflow-y: auto;overflow-x: hidden; } 
		.detail{cursor: pointer;margin-left:5px;}
		.detailqi{height: 16px;width: 22px;cursor: pointer;margin-left:5px;background-image:url('<%=basePath%>/resources/images/icons/ting.png');background-repeat :no-repeat;}
		.detailting{height: 16px;width: 22px;cursor: pointer;margin-left:5px;background-image:url('<%=basePath%>/resources/images/icons/qi.png');background-repeat :no-repeat;}
	</style>
 <script type="text/javascript" src="<%=path%>/resources/js/jquery-1.7.2.min.js"></script>
 <link rel="stylesheet" type="text/css"	href="<%=path %>/resources/js/extjs/resources/css/ext-all.css"></link>
 <script type="text/javascript" src="<%=path %>/resources/js/extjs/ext-all.js"></script>	
 <script type="text/javascript" src="<%=basePath%>/ccore/resources/js/extendExtjs.js"></script>
 <script type="text/javascript" src="<%=path%>/resources/js/common.js"></script>
 <script type="text/javascript" src="<%=basePath %>/permissions/resources/user-js/role.js"></script>
 <script type="text/javascript" src="<%=basePath %>/resources/js/public.js"></script>
 <script type="text/javascript" src="<%=basePath %>/resources/js/lock.js"></script>
 <link rel="stylesheet" type="text/css" href="<%=basePath %>/resources/css/lock.css" />
  <script type='text/javascript' src='<%=basePath %>/resources/js/extjs/locale/ext-lang-zh_CN.js' ></script>
</head>
<body style='padding:0px;'> 
<div id="roleList"></div>
<form id="rolefrom" method="post"></form>
<div id='addwinrole' style="display:none">
<form id="role" method="post">
	<table width="100%" border="0">
  <tr>
    <td width="40%" height="30" align="right" valign="middle">角色名称:</td>
    <td width="60%" align="left" valign="middle"><input name="pr.roleName" maxlength="10" id="roleName">
      <span class="STYLE2">*</span></td>
  </tr>
  <tr>
    <td align="right" valign="middle">角色描述:</td>
    <td height="30" align="left" valign="middle"><input name="pr.roleDescribe" maxlength="30" id="roleDescribe">
      </td>
  </tr>
  <tr>
    <td align="right" valign="middle">角色状态:</td>
    <td height="30" align="left" valign="middle"><span id='roleStatus' style='float:left'></span>
      <span class="STYLE2">*</span></td>
  </tr>
  <tr>
    <td align="right" valign="middle">角色类型:</td>
    <td height="30" align="left" valign="middle"><span id='roletype' style='float:left'></span>
      <span class="STYLE2">*</span></td>
  </tr>
  <tr>
    <td align="right" valign="middle">&nbsp;</td>
    <td height="30" align="left" valign="middle"><span id='roleadd'></span><span id='quxiao'></span></td>
  </tr>
</table>
	</form>
</div>
<div id='updwinrole' style="display:none">
<form id="role" method="post">
	<table width="100%" border="0">
  <tr>
    <td width="40%" height="30" align="right" valign="middle">角色名称:</td>
    <td width="60%" align="left" valign="middle"><input name="pr.roleName" id="roleNameupd">
      <span class="STYLE2">*</span></td>
  </tr>
  <tr>
    <td align="right" valign="middle">角色描述:</td>
    <td height="30" align="left" valign="middle"><input name="pr.roleDescribe" id="roleDescribeupd">
      </td>
  </tr>
  <tr>
    <td align="right" valign="middle">角色状态:</td>
    <td height="30" align="left" valign="middle"><select name="pr.roleStatus" id="roleStatusupd">
			<option value="-1">--请选择--</option>
			<option value="1">停用</option>
			<option value="0">启用</option>
		</select>
      <span class="STYLE2">*</span></td>
  </tr>
  <tr>
    <td align="right" valign="middle">&nbsp;<input type="hidden" name="pr.roleId" id='roleIdupd' value=""></td>
    <td height="30" align="left" valign="middle"><input type="button" value="修改" onclick="updroles()">&nbsp;&nbsp;<input type="button" value="取消" onclick="javascript:updrole.close();"></td>
  </tr>
</table>
	</form>
</div>
<div id='addrole'></div>
<div id='updrole'></div>
<div id='boxroletee'>
	<div id='tree'></div>
</div>
 <div id="lockmask" class="mask"></div>
<div id="lockmaskmsg" class="mask-msg">
		正在处理，请稍候。。。	
</div>
</body>
</html>