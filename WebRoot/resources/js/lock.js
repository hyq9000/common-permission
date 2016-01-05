/*
 * 作用于：通用
 * 功能说明：加载提示的开关方法
 * 依赖文件：无
 * 作者：zy
 * 创建时间：2012-8-08
 */
 //加载提示的开关方法
 //op字符串检测参数'open'
function lock(op){
	if(op=='open'){
		document.getElementById('lockmask').style.display='block';
		document.getElementById('lockmaskmsg').style.display='block';
	}else{
		document.getElementById('lockmask').style.display='none';
		document.getElementById('lockmaskmsg').style.display='none';
	}
}