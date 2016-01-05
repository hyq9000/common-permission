/*
 * 作用于：/permissions/user/userRole.jsp页面
 * 功能说明：用户分配角色；
 * 依赖文件：jquery
 * 作者：zy
 * 创建时间：2012-8-08
 */

Ext.require([
             'Ext.grid.*',
             'Ext.data.*',
             'Ext.util.*',
             'Ext.state.*'
         ]);
//用户列表数据结构
Ext.define('User', {
    extend: 'Ext.data.Model',
    fields: [
       {name:'userId',type:'string',mapping:'userId'},
       {name:'userName',type:'string',mapping:'userName'},
       {name:'roleName',type:'string',mapping:'roleName'}
    ]
});
var path="../..";
var iconImg=path+'/resources/images/icons/';
var roleuser;
var store;
var user_id='';
var grid;
var roles='';
var topContainer;
var anniu;
var datajson;
Ext.onReady(function() {

	Ext.tip.QuickTipManager.init();
	var reader=Ext.create("Ext.data.reader.Json",{
		root:"items",
		totalProperty:'countSize',
		listeners:{
			//如果响应的内容不是一个json，那定是一个超时的<script>xxxx</script>格式的脚本；
			exception:function(reader,response, error,eOpts){
				executeScriptStr(response.responseText);
			}
		}	
	});
	
		
	//用户列表的数据源
	 store = Ext.create('Ext.data.ArrayStore', {
	        model: 'User',
	        pageSize:20,
	        proxy: { 
   	           //异步获取数据，这里的URL可以改为任何动态页面，只要返回JSON数据即可 
   	            type: 'ajax', 
   	            url: path+'/oo/getAllUser.action?dataJson='+encodeURI(dataJson), 
   	            reader: reader
   	        } 
	    });
// create the Grid
//创建用户列表
topContainer=grid = Ext.create('Ext.grid.Panel', {
    store: store,
    title: '用户分配角色管理',
    stateId: 'stateGrid',
    enableColumnHide:false,
    columns: [
    	{xtype: 'rownumberer',width:65,text:'序号'},
        {text:'用户编号',width:100,dataIndex: 'userId'},
        {text:'用户姓名',width:150,dataIndex: 'userName'},
        {text:'用户角色',width:310,dataIndex: 'roleName'},
        { text:"操作",sortable : false,
        	menuText:'操作',//因是actioncolumn类型，故此
        	xtype:'actioncolumn',
            width:50,
            items: [{
	                icon: iconImg+'icon_fpjs.gif',
	                tooltip: '分配<br>角色',
	                getClass:function(){return "detail"},
	                handler: function(view, rowIndex, colIndex) {
	                	if(roles=='true'){
		                 	var recd=view.getStore().getRange(rowIndex,rowIndex)[0];
		                 	var checkboxes = document.getElementsByName("jsonbox");  
		                 	user_id=recd.data.userId+"-_-"+recd.data.userName;
		                 	//获取用户拥有的角色
		                 	$.post(path+'/oo/getAllUser!getRole.action',{'userid':recd.data.userId},function (data){
		                 		executeScriptStr(data);
		                 		//循环清空勾选的复选框
		                 		for(var i=0;i<checkboxes.length;i++){  
		                 			checkboxes[i].checked = ''; 
		                 		}
		                 		datajson=data;
	                 			//当有数据返回时 true执行复选框勾选操作 false按钮名称改为“全选”
		                 		if(data!=''){
		                 			//勾选以前选择的角色
		                 			var da=data.split(',');
		                 			//循环勾选用户拥有的角色
			                 		for(var j=0;j<da.length;j++){
				                 		for(var i=0;i<checkboxes.length;i++){  
				                 			if(da[j]==checkboxes[i].value){
							             		checkboxes[i].checked = 'true'; 
							             		break; 
							             	}
							        	}  
						        	}
						        	//判断是“全选”还是“全不选”
						        	var bool=0;
						        	for(var i=0;i<checkboxes.length;i++){
						        		 if(!checkboxes[i].checked){
						        		 	bool+=1;
						        		 }else{
						        		 	bool+=0;
						        		 }
						        	}
						        	//当没有勾选一个复选框时按钮名称为“全选” 否则为“全不选”
						        	if(bool==checkboxes.length&&bool!=0||bool!=checkboxes.length&&bool!=0){
						        		anniu.setText('全选');
						        	}else if(bool==0){
						        		anniu.setText('全不选');
						        	}
					        	}else{
					        		anniu.setText('全选');
					        	}
					        	//滚动条的位置变为0
					        	document.getElementById('gundongtiao').scrollTop=0;
					        	roleuser.show();
		                 	});
	                 	}else{
	                 		Ext.MessageBox.alert("提示框","没有可以分配的角色，请添加角色"); 
	                 	}
					}
				}]				           
		}
    ],  
    bbar: Ext.create('Ext.PagingToolbar', {
            store: store,
            id:'userpaging',
            beforePageText:"跳到",
	        afterPageText:"页",
	        lastText:"最后<br>一页",
	        nextText:"下一页",
	        prevText:"上一页",
	        firstText:"第一页",
	        refreshText:"刷新",
	        displayInfo:true,
	        displayMsg:"当前显示 {0}-{1}行数据，总共有{2}行",
	        emptyMsg:"当前没有数据显示",
    		inputItemWidth:60,
    		doRefresh:function(){
    			//获取输入页码的输入框的值
    			var total=grid.getStore().getTotalCount();
    			var ye=total%20==0?total/20:parseInt(total/20+1);
    			var star=1;
				for(var i=0;i<50;i++){
					if(grid.getEl().dom.getElementsByTagName("input")[i].name=='inputItem'){
						star=grid.getEl().dom.getElementsByTagName("input")[i].value;
						break;
					}
				}
				if(isNaN(Number(star))){
					Ext.MessageBox.alert("提示框","您输入的页码有误，请重新输入!");
					return ;
				}
    			if(Number(star)>0&&Number(star)<=ye){
			        var myMask = new Ext.LoadMask(grid,{msg:"请稍等..."});
					myMask.show();
				    store.loadPage(Number(star),{
				    	callback:function(){
				    		myMask.hide();
				    	}
				    });
			    }else if(Number(star)>ye){
			    	var myMask = new Ext.LoadMask(grid,{msg:"请稍等..."});
					myMask.show();
				    store.loadPage(ye,{
				    	callback:function(){
				    		myMask.hide();
				    	}
				    });
			    }else{
			    	var myMask = new Ext.LoadMask(grid,{msg:"请稍等..."});
					myMask.show();
				    store.loadPage(1,{
				    	callback:function(){
				    		myMask.hide();
				    	}
				    });
			    }
		    }
    }),
    height: 600,
    width:document.body.clientWidth,
    renderTo: 'user_grid'
});
//当页面宽度改变是列表的宽度自动改变
window.onresize=function (){
	grid.setWidth(document.body.offsetWidth);
}
$(window).resize();	
//获取所有的角色	
$.post(path+'/oo/getAllUserRole.action',function (data){
	executeScriptStr(data);
		var html='';
		//返回值不等于false true循环构建角色复选框列表 false创建角色
		if(data!='false'){
			var json=eval('('+data+')');
			html+="<table><tr><td><div ='quanxuan'></div></td></tr></table>";
			html+="<div id='gundongtiao' style='width:95%; height:140px; overflow:auto;margin-left:5%;'>";
			for(var i=0;i<json.length;i++){
				var role_name=json[i].ROLE_NAME.length>6?json[i].ROLE_NAME.substr(0,6)+'...':json[i].ROLE_NAME;
				html+="<div style='width:33%; height:20px; float:left; text-align:left;' title='"+json[i].ROLE_NAME+"'><input type='checkbox' name='jsonbox' value='"+json[i].ROLE_ID+"' />&nbsp;"+role_name+"</div>";
			}
			html+="</div>";
			html+="<div style='width:99% ; height:20px; text-align:center'><div id='baocun'></div><div>";
			roles='true';
		}else{
			roles='false';
		}
		//分配角色的列表的弹出框
		roleuser=Ext.create('Ext.window.Window', {
				    title: '分配角色',
				    height: 200,
				    width: 400,
				    layout: 'fit',
				    resizable:false,
				    constrain:true,
				    modal:true,
				    contentEl:'roleuser',
				    closeAction:'hide',
				    html:html
				});
		roleuser.show();
		roleuser.close();
		//保存按钮
		Ext.create('Ext.Button', {
		    text: '保存',
		    width:50,
		    height:23,
		    renderTo: 'baocun',
		    handler: function() {
		        userrole();
		    }
		});
		//全选或者全不选按钮
		anniu=Ext.create('Ext.Button', {
		    text: '全选',
		    width:50,
		    height:23,
		    renderTo: 'baocun',
		    handler: function() {
		        quanxuan(this);
		    }
		});
});

var myMask = new Ext.LoadMask(grid,{msg:"请稍等..."});
myMask.show();
store.loadPage(1,{
	callback:function(){
		myMask.hide();
	}
});	
//当数据加载时，当前页的实际行数；
store.on("load",function(){
		//在分页条上显示实际行数；			
		//showActualRowCount(grid,'userpaging')
});
});
//用户分配角色的分配事件
function userrole(){
	var data=grid.getSelectionModel().getSelection();
	var roleId='';
	var userId='';
	var count=0;
	var boxs=document.getElementsByName('jsonbox');
	//循环获取勾选的复选框的值，并且构建成用“,”隔开的字符串
	for(var i=0;i<boxs.length;i++){
		if(boxs[i].checked){
			if(count!=0){
				roleId+=",";
			}
			roleId+=boxs[i].value;
			count++;
		}
	}
	var roles=roleId.split(',');
	var json=datajson.split(',');
	var countName=0;
	//检测这次操作有没有修改过，
	if(roles.length==json.length){
		for(var i=0;i<roles.length;i++){
			for(var j=0;j<json.length;j++){
				//检测修改前的id和修改后的id是否一样，一样则是没有修改countName为2
				if(roles[i]==json[j]){
					countName=2;
					break;
				}else{
					countName=1;
				}
			}
			if(countName==1){
				break;
			}
		}
		
		if(countName==2){
			roleuser.close();	
			Ext.MessageBox.alert("提示框","此次没有做任何修改！");
			return ;
		}
	}
	userId=user_id;
	//保存修改后的信息
	$.ajax({
		type:'post',
		url:path+'/oo/setUserRole.action',
		data:{userIds:userId,roleIds:roleId},
		success:function(data){
			executeScriptStr(data);
				//返回“true”本次操作是成功的  返回“false”操作失败
					if(data=='true'){
						if(roleId!='')
						Ext.MessageBox.alert("提示框","分配成功!");
						else
						Ext.MessageBox.alert("提示框","此次没有分配任何角色"); 
						//刷新列表的数据
						store.setProxy({
							//异步获取数据，这里的URL可以改为任何动态页面，只要返回JSON数据即可 
			   	            type: 'ajax', 
			   	            url: path+'/oo/getAllUser.action', 
			   	            reader: { 
			   	            	type:'json',
			   	                root: 'items',
			   	             	totalProperty: 'countSize'
			   	            }
						}); 
						
						var total=grid.getStore().getTotalCount();
		    			var ye=total%20==0?total/20:parseInt(total/20+1);
		    			var star=1;
						for(var i=0;i<50;i++){
							if(grid.getEl().dom.getElementsByTagName("input")[i].name=='inputItem'){
								star=grid.getEl().dom.getElementsByTagName("input")[i].value;
								break;
							}
						}
						if(isNaN(Number(star))){
							Ext.MessageBox.alert("提示框","您输入的页码有误，请重新输入!");
							return ;
						}
		    			if(Number(star)>0&&Number(star)<=ye){
					        var myMask = new Ext.LoadMask(grid,{msg:"请稍等..."});
							myMask.show();
						    store.loadPage(Number(star),{
						    	callback:function(){
						    		myMask.hide();
						    	}
						    });
					    }else if(Number(star)>ye){
					    	var myMask = new Ext.LoadMask(grid,{msg:"请稍等..."});
							myMask.show();
						    store.loadPage(ye,{
						    	callback:function(){
						    		myMask.hide();
						    	}
						    });
					    }else{
					    	var myMask = new Ext.LoadMask(grid,{msg:"请稍等..."});
							myMask.show();
						    store.loadPage(1,{
						    	callback:function(){
						    		myMask.hide();
						    	}
						    });
					    }
						roleuser.close();	
					}else{
						Ext.MessageBox.alert("提示框","分配失败"); 
					}
				}
	});
}
//全选按钮或者全不选按钮事件
function quanxuan(op){
	var boxs=document.getElementsByName('jsonbox');
	if(op.text=='全选'){
		//循环勾选复选框
		for(var i=0;i<boxs.length;i++){
			boxs[i].checked='true';
		}
		op.setText('全不选');
	}else{
		//循环去掉复选框的勾选
		for(var i=0;i<boxs.length;i++){
			boxs[i].checked='';
		}
		op.setText('全选');
	}
}
