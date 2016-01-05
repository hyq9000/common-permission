/*
 * 作用于：通用固定参数和固定方法
 * 功能说明：加载提示的开关方法
 * 依赖文件：无
 * 作者：zy
 * 创建时间：2012-8-08
 */
var path='../..';//项目名称
var iconImg=path+'/resources/images/icons/';//图片的路径
//获取值
//op控件id
function getValue(op){
	return document.getElementById(op).value;
}
//给文本框赋值
//op控件id
//value 需要给控件赋的值
function setValue(op,value){
	document.getElementById(op).value=value;
}
//构建jquery ajax
//url ajax访问的url地址
//data ajax请求参数
//返回值 返回ajax返回的值
function getRequest(url,data){
	var datas;
	var par;
	if(data!='')
	{
		for(var i=0;i<data.length;i++){
			var canshu=data[i].split(',');
			par+='&'+canshu[0]+'='+encodeURI(canshu[1]);
		}
		$.post(url+'?'+par.substring(1,par.length),function (dataname){
			datas=dataname;
		});
		return datas;
	}
	return '';
}

