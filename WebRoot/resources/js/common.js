/*
 * 作用于:所有页
 * 实现一些所有模块的通用逻辑
 * 依赖顺序:jquery
 * 作者：hyq
 * 创建时间：2012-12-10
 */
 
 
 /*
  * 执行服务端返回的script脚本；
  * scriptStr: 以<script>开头，以</script>结束的js脚本字符串
  */
 function executeScriptStr(scriptStr){ 	
 	var rs=$.trim(scriptStr);
 	//如果是script脚步本，则执行该脚本
 	if(rs.indexOf("<script>")!=-1){
		rs=rs.replace("<script>","").replace("</script>","");
		eval(rs);
	}
 }