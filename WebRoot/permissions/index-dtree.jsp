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
<style type="text/css">
.dtree {
	font-family: Verdana, Geneva, Arial, Helvetica, sans-serif;
	font-size: 11px;
	color: #666;
	white-space: nowrap;
}
.dtree img {
	border: 0px;
	vertical-align: middle;
}
.dtree a {
	color: #333;
	text-decoration: none;
}
.dtree a.node, .dtree a.nodeSel {
	white-space: nowrap;
	padding: 1px 2px 1px 2px;
}
.dtree a.node:hover, .dtree a.nodeSel:hover {
	color: #333;
	text-decoration: underline;
}
.dtree a.nodeSel {
	background-color: #c0d2ec;
}
.dtree .clip {
	overflow: hidden;
}
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
<script type="text/javascript" src="<%=basePath %>/permissions/resources/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="<%=basePath %>/permissions/resources/js/dtree.js"></script>
</head>
<script type="text/javascript">
var d = new dTree('d');
$(function (){
	$.post("<%=basePath %>/oo/getUserPermissions.action?userid=<%=request.getParameter("userid") %>",
		function(data){
		  var jsonDatas = eval("(" + data + ")");
          //循环遍历数据
          jQuery.each(jsonDatas.root, function(item) {
        	  d.add(jsonDatas.root[item].permissionId
        			  ,jsonDatas.root[item].permissionParentId
        			  ,jsonDatas.root[item].permissionName,jsonDatas.root[item].permissionUri,'','mainFrame');
          });
        document.getElementById('tree').innerHTML=d;
        d.openAll();
	});

});



</script>
<body>
<table style="width: 120px; height: 10px; font-size: 12px;">
	<tr>
		<td width="60px" align="center"><a href="javascript:d.openAll();">全部打开</a></td>
		<td width="60px" align="center"><a href="javascript:d.closeAll();">全部关闭</a></td>
	</tr>
</table>
<div id="tree" ></div>
</body>
</html>