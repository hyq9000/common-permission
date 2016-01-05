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
</head>
<script type="text/javascript">
function login(){
	var userlogin=document.getElementById('userlogin');
	userlogin.action='<%=basePath %>/permissions/index.jsp';
	userlogin.submit();
}
</script>
<body>
<form id='userlogin' method="post">
<input type="text" name="userid" value="123">
<input type="button" value="登录" onclick="login()">
</form>
</body>
</html>