/*
 * 作用于：/permissions/role/rolelistjson.jsp页面
 * 功能说明：角色的增加删除修改；
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
//定义列表的数据结构
Ext.define('Role', { 
    extend: 'Ext.data.Model',
    fields: [
       {name:'ROLE_ID',type:'int',mapping:'ROLE_ID'},
       {name:'ROLE_NAME',type:'string',mapping:'ROLE_NAME'},
       {name:'ROLE_DESCRIBE',type:'string',mapping:'ROLE_DESCRIBE'},
       {name:'ROLE_STATUS',type:'int',mapping:'ROLE_STATUS'},
       {name:'ROLE_TYPE',type:'string',mapping:'ROLE_TYPE'}
    ]
});
var addrole;
var grid;
var store;//角色列表的数据源
var jobCmb;//角色下拉列表
var roleid;
var topContainer;
var roleType;
var myMask;
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
//角色列表的数据源
store = Ext.create('Ext.data.ArrayStore', {
    model: 'Role',
    pageSize:20,
    proxy: { 
       //异步获取数据,这里的URL可以改为任何动态页面,只要返回JSON数据即可 
        type: 'ajax', 
        url: '../../oo/getAllRoleJson.action', 
        reader: reader
    }
});
//状态下拉框的json
var boxDate=[
		{'value':0,"text":"启用"},			      
        {'value':1,"text":"停用"}			
    ];
//角色类型下拉列表数据源
var roleTypeData=[
		{'value':'cloud_manager',"text":"管理角色"},	
        {'value':'cloud_role',"text":"用户角色"}		
    ];
//状态的数据源
var	jobStore1 = Ext.create('Ext.data.Store', {
    fields: ['value','text'],
    data : boxDate
});
//角色类型数据源
var	roleTypeStore = Ext.create('Ext.data.Store', {
    fields: ['value','text'],
    data : roleTypeData
});
//角色类型下拉列表
jobCmb=Ext.create('Ext.form.ComboBox', {
				id:'comboxstatus',
				editable:false,
			    store: jobStore1,
			    displayField: 'text',
			    valueField: 'value',
			    renderTo:'roleStatus',
			    value:"请选择"
			});
//状态下拉列表
jobCmb1=Ext.create('Ext.form.ComboBox', {
				id:'comboxstatus1',
				editable:false,
			    store: jobStore1,
			    displayField: 'text',
			    valueField: 'value',
			    value:"请选择"
			});
//角色类型下拉列表
roleType=Ext.create('Ext.form.ComboBox', {
				id:'roleTypeID',
				editable:false,
			    store: roleTypeStore,
			    displayField: 'text',
			    valueField: 'value',
			    renderTo:'roletype',
			    value:"请选择"
			});
			
var roleTypes=Ext.create('Ext.form.ComboBox', {
				id:'roleTypeLie',
				editable:false,
			    store: roleTypeStore,
			    displayField: 'text',
			    valueField: 'value',
			    value:"请选择"
			});

//生成角色的列表 
topContainer=grid = Ext.create('Ext.grid.Panel', {
    store: store,
    dockedItems: [{
	        xtype: 'pagingtoolbar',
	        id:'rolepaging',
	        store: store,   // same store GridPanel is using
	        dock: 'bottom',
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
    			var total=grid.getStore().getTotalCount();//获取列表的总记录数
		    	var ye=total%20==0?total/20:parseInt(total/20+1);//计算总页数
		    	var star=1;//当前页码
		    	//循环获取当前input中的页码
				for(var i=0;i<50;i++){
					if(grid.getEl().dom.getElementsByTagName("input")[i].name=='inputItem'){
						star=grid.getEl().dom.getElementsByTagName("input")[i].value;
						break;
					}
				}
				//检测页码是否正确
				if(isNaN(Number(star))){
					Ext.MessageBox.alert("提示框","您输入的页码有误，请重新输入!");
					return ;
				}
				//当页码大于0且小于总页码刷新列表
    			if(Number(star)>0&&Number(star)<=ye){
			        var myMask = new Ext.LoadMask(grid,{msg:"请稍等..."});
					myMask.show();
				    store.loadPage(Number(star),{
				    	callback:function(){
				    		myMask.hide();
				    	}
				    });
				//当页码大于总页码刷新列表且页码回到最后一页
			    }else if(Number(star)>ye){
			    	var myMask = new Ext.LoadMask(grid,{msg:"请稍等..."});
					myMask.show();
				    store.loadPage(ye,{
				    	callback:function(){
				    		myMask.hide();
				    	}
				    });
				//否则回到第一页
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
	    }],
    selType: 'rowmodel',
    enableColumnHide:false,
    columns: [
    	{xtype: 'rownumberer',width:65,text:'序号',align:'right'},
        {header :'<strong>角色编号</strong>',width:75,sortable: true,dataIndex: 'ROLE_ID'},
        {header :'<strong>角色名称</strong>',width:150,sortable: true,editor:'textfield',dataIndex: 'ROLE_NAME'},
        {header :'<strong>角色描述</strong>',width:250,sortable: true,editor:'textfield',dataIndex: 'ROLE_DESCRIBE'},
        {header :'<strong>角色状态</strong>',width:75,sortable: true,editor:jobCmb1,dataIndex: 'ROLE_STATUS',
        renderer:function (value,metadata,record){
        	//获取每个状态值的名称
        	var index = jobStore1.find(Ext.getCmp('comboxstatus').valueField,value);   
            if(typeof(jobStore1.getAt(index))=='undefined'){
        		return '';
        	}else{
        		return jobStore1.getAt(index).get('text');
        	}
        }},{header :'<strong>角色类型</strong>',width:100,sortable: true,editor:roleTypes,dataIndex: 'ROLE_TYPE',
        renderer:function (value,metadata,record){
        	//当类型不为产品角色时则根据类型值获取对应的名称否则返回“产品角色”
        	if(value!='cloud_product'){
	        	var index = roleTypeStore.find(Ext.getCmp('roleTypeID').valueField,value);
	        	//检测名称如果不为undefined取集合中的名称   
	        	if(typeof(roleTypeStore.getAt(index))=='undefined'){
	        		return '';
	        	}else{
	        		return roleTypeStore.getAt(index).get('text');
	        	}
        	}else{
        		return '产品角色';
        	}
        }},{ text:"操作",sortable : false,
        	menuText:'操作',//因是actioncolumn类型，故此
        	xtype:'actioncolumn',
            width:100,
            items: [{
	                icon: '../../resources/images/icons/icon_sc.gif',
	                tooltip: '删除',
	                getClass:function(){return "detail"},
	                handler: function(view, rowIndex, colIndex) {
	                	//获取行数据对象
	                 	var recd=view.getStore().getRange(rowIndex,rowIndex)[0];
	                 	//执行删除函数
	                	deleterole(recd.data.ROLE_ID);
					}
				},{
	                icon: '../../resources/images/icons/icon_fpqx.gif',
	                tooltip: '分配<br>权限',
	                getClass:function(){return "detail"},
	                handler: function(view, rowIndex, colIndex) {
	                 	var recd=view.getStore().getRange(rowIndex,rowIndex)[0];
	                 	//获取角色编号
	                 	roleid=recd.data.ROLE_ID;
	                 	//count为0第一次进来
	                 	if(count==0){
	                 		//生成权限树
		                 	boxtree(roleid);
		                 	count=1;
	                 	}else{
	                 		//直接改变权限树的url来加载新的数据
	                 		stores.proxy.url=path+'/oo/getRolePermission.action?pmRole.roleId='+roleid;
	                 		stores.load();
	                 	}
	                 	//显示分配权限窗口
	                 	boxrole.show();
	                }
            	},{
            		//icon:'../../resources/images/icons/door.png',
            		getClass:function(v, meta, rec){	
            			//alert(rec.get('ROLE_STATUS')+"="+rec.get('ROLE_ID'));
            			//检测状态等于1改为启用 0为停用
		                if(rec.get('ROLE_STATUS')==1){
	                		this.items[2].tooltip='启用';	
		                	return 'detailqi';           			                		            	
		                }else if(rec.get('ROLE_STATUS')==0){
	                		this.items[2].tooltip='停用';	
		                	return 'detailting';       
		                }
		            },
	                handler: function(view, rowIndex, colIndex) {
	                	recd=view.getStore().getRange(rowIndex,rowIndex)[0];
	                	var status=recd.data.ROLE_STATUS==0?'停用':'启用';
	                		//给出是否提示框
							Ext.Msg.show({title:'操作确认',msg:'您确定要'+status+'吗？',buttons:Ext.MessageBox.YESNO, //设置消息按钮
						      buttonText:{  
					             yes: "确定", 
					             no: "取消"
					         },
					         icon:Ext.MessageBox.QUESTION,   //显示警告图标
					         fn: function (ops){
					         	//点击确定
					         	if(ops=='yes'){
					         		$.post('../../oo/ifRoleName!updateStatus.action',{'pr.roleStatus':recd.data.ROLE_STATUS==0?1:0,'pr.roleId':recd.data.ROLE_ID},function (data){
					         			//检测是否超时
										executeScriptStr(data);
										//返回值如果为‘true’刷新列表 ‘false’ 给出提示
					         			if(data=='true'){
					         				var total=grid.getStore().getTotalCount();
							    			var ye=total%20==0?total/20:parseInt(total/20+1);
							    			var star=1;
											for(var i=0;i<50;i++){
												Ext.MessageBox.alert("提示框",status+"成功!");
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
					         			}else if(data=='false'){
					         				Ext.MessageBox.alert("提示框",status+"失败!");
					         			}
								    });
					         	}
					         }
					     });	
	                }
            	}]				           
		}
    ],
    height: 600,
    width: document.body.clientWidth,
    loadMask: true,
    title: '角色管理',
    renderTo: 'roleList',
    tbar:[  
       {xtype:'button',text:'添加',icon:iconImg+'icon_tj.gif',handler:function (){
       		//清空前一次添加的输入的内容
        	setValue('roleName','');
			setValue('roleDescribe','');
			jobCmb.setValue('请选择');
			//jobStore1.loadData(boxDate);
			roleType.setValue('请选择');
			//roleTypeStore.loadData(roleTypeData);
			//往下拉列表中添加数据
			document.getElementById('addwinrole').style.display='';
			//显示添加添加窗口
       		addrole.show();
       }}
	    /*
			//此处专用于可以批量生成的一些角色,现暂不用，故注掉；
		  ,"-",{xtype:'button',icon:iconImg+'icon_pltj.gif',text:'批量生成产品角色',handler:function (){
       		//批量生成功能ajax
       		$.ajax({
			async:false,
			url:'../../oo/ifRoleName!batchCreateRole.action',
			success:function(data){
				//检测是否超时
				executeScriptStr(data);
				//返回记录数或者false 记录数大于0给出提示，并且刷新列表
				if(data>0){
					Ext.MessageBox.alert("提示框","角色生成成功,此次共生成"+data+"个角色!");
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
				if(data=='false'){
					Ext.MessageBox.alert("提示框","角色生成失败!");
				}
				if(data==0){
					Ext.MessageBox.alert("提示框","没有产品可生成角色!");
				}
			}
			});
       }}*/
    ],plugins: [
        Ext.create('Ext.grid.plugin.RowEditing', {//双击执行修改操作
            disabled:false,
        	cancelBtnText: '取消',
        	saveBtnText: '更新',
            clicksToMoveEditor: 1
        })
	]
});
//当浏览器的宽度发生改变时，自动改变ext列表的宽度
window.onresize=function (){
	grid.setWidth(document.body.offsetWidth);
}
$(window).resize();		
//在表中进行数据的修改
grid.on("edit", function(editor,e){
	$.ajax({
		type:"POST",
		url:"../../oo/updRole.action",
		data:{'pr.roleName':e.record.get('ROLE_NAME'),'pr.roleDescribe':e.record.get('ROLE_DESCRIBE'),'pr.roleStatus':e.record.get('ROLE_STATUS'),'pr.roleId':e.record.get('ROLE_ID'),'pr.roleType':e.record.get('ROLE_TYPE')},
		success:function(d){
			//检测是否超时
			executeScriptStr(d);
			e.record.commit();
		}							
	});
});
//判断是否可以被修改
grid.on("beforeedit",function(editor,e){
	var ROLE_TYPE=e.record.get('ROLE_TYPE');
	var rs=true;
	//检测角色类型不为产品类型才可以进行检测是否能进行修改 否则不能进行修改
	if(ROLE_TYPE!='cloud_product'){
    	var id=e.record.get('ROLE_ID');
    	$.ajax({
			async:false,
			url:'../../oo/ifRoleName!ifroleUserBoolean.action',
			data:{'pr.roleId':id},
			success:function(data){	
				executeScriptStr(data);
				//data为true返回true否则给出提示并且返回false
				if(data!='true'){
		  			Ext.MessageBox.alert("提示框","角色已经被绑定，不能进行修改!");
		  			rs=false;
		  		}else{
		  			rs=true;
		  		}	
			}
		});
	}else{
		Ext.MessageBox.alert("提示框","产品角色不能进行修改!");
		rs=false;
	}
	return rs;
});
//双击行时修改进行验证
 grid.on("validateedit",function(editor,e){
 	//验证角色名称
 	if(e.newValues.ROLE_NAME.replace(/[ ]/g,"")==''){
 		Ext.MessageBox.alert("提示框","您没有输入角色名称!");
		return false;
	}
	//验证角色名称长度
	if(e.newValues.ROLE_NAME.length>10){
		Ext.MessageBox.alert("提示框","您输入的角色名称不能超过10位!");
		return false;
	}
	//验证角色描述长度
	if(e.newValues.ROLE_DESCRIBE.length>30){
		Ext.MessageBox.alert("提示框","您输入的角色描述不能超过30位!");
		return false;
	}
	//验证角色名称是否有特殊字符
	if(e.newValues.ROLE_NAME.match(/^[\u4E00-\u9FA50-9a-zA-Z]{1,10}$/g)==null){
		Ext.MessageBox.alert("提示框","角色名称不能为特殊字符!");
		return false;
	}
	//验证角色名称是否存在
	var rs=true;
	if(e.newValues.ROLE_NAME.toUpperCase()!=e.record.get("ROLE_NAME").toUpperCase()){
		$.ajax({
			async:false,
			url:"../../oo/ifRoleName.action",
			data:{'pr.roleName':e.newValues.ROLE_NAME},
			success:function(data){	
				executeScriptStr(data);
				//data为false则给出提示并且返回false 否则返回true
				if(data=='false'){
					Ext.MessageBox.alert("提示框","您输入的角色名称已经存在");
					rs=false;
				}
				if(data=='true'){
					rs=true;
				}
			}
		});
	}
	return rs;
}); 
var count=0;
//删除角色的事件
function deleterole(op){
	//给出是否提示框
	Ext.Msg.show({title:'操作确认',msg:'您确定要删除吗？',buttons:Ext.MessageBox.YESNO, //设置消息按钮
	      buttonText:{  
             yes: "确定", 
             no: "取消"
         },
         icon:Ext.MessageBox.QUESTION,   //显示警告图标
         fn: function (ops){
         	//点击确定
         	if(ops=='yes'){
				var per=op;
				//检测是否绑定了用户
				$.ajax({type:'post',url:"../../oo/boolUserRole.action?roleids="+per,
					success:function (data){
						//检测用户是否超时
						executeScriptStr(data);
							//返回false 绑定了用户
							if(data=='false'){
				  				Ext.MessageBox.alert("提示框","已经有用户绑定了该角色，您不能删除该角色"); 
				  			}else{
				  				//返回true 执行刷新列表
				  				if(data=='true'){
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
							}
					},error:function (xtc){
						Ext.MessageBox.alert("提示框","系统正在维护中。。。"); 
					}
				});
			}
		}
	});
}

//页面加载时则取得数据并加到表中去；
var myMask = new Ext.LoadMask(grid,{msg:"请稍等..."});
myMask.show();
store.loadPage(1,{
	callback:function(){myMask.hide();
	}
});
//当数据加载时，当前页的实际行数；
store.on("load",function(){
		//在分页条上显示实际行数；			
		//showActualRowCount(grid,'rolepaging');
});	
//添加角色的弹出页面
addrole=Ext.create('Ext.window.Window', {
    title: '添加角色',
    height: 200,
    width: 400,
    modal:true,
    resizable:false,
    constrain:true,
    contentEl : "addwinrole",
    closeAction:'hide'
});
//分配角色的弹出页面
boxrole=Ext.create('Ext.window.Window', {
    title: '分配权限',
    height: 400,
    width: 600,
    modal:true,
    resizable:false,
    constrain:true,
    contentEl : "boxroletee",
    closeAction:'hide'
    
});
//当弹出的添加窗口关闭后删除先前输入框的值的事件
addrole.on({
 'hide':function(){ //当关闭这个窗口是执行
    setValue('roleName','');
	setValue('roleDescribe','');
	jobStore1.loadData(boxDate);
  }
});
//新增按钮
Ext.create('Ext.Button', {
    text: '新增',
    width:50,
    height:23,
    renderTo: 'roleadd',
    handler: function() {
        addroles();
    }
});
//取消按钮
Ext.create('Ext.Button', {
    text: '取消',
    width:50,
    height:23,
    renderTo: 'quxiao',
    handler: function() {
        addrole.close();
    }
});
});

//添加角色添加事件
function addroles(){
	if(getValue('roleName').replace(/[ ]/g,"")==''){
		Ext.MessageBox.alert("提示框","您没有填写角色名称!"); 
		return ;
	}
	if(jobCmb.getValue()=='请选择'){
		Ext.MessageBox.alert("提示框","您没有选择角色状态!"); 
		return ;
	}
	if(getValue('roleDescribe').length>30){
		Ext.MessageBox.alert("提示框","您输入的角色描述不能超过30位!");
		return false;
	}
	if(roleType.getValue()=="请选择"){
		Ext.MessageBox.alert("提示框","您没有选择角色类型!"); 
		return ;
	}
	if(getValue('roleName').match(/^[\u4E00-\u9FA50-9a-zA-Z]{1,10}$/g)==null){
			Ext.MessageBox.alert("提示框","角色名称不能为特殊字符!");
			return false;
	}
	//检测名称是否存在
	$.post("../../oo/ifRoleName.action",{'pr.roleName':getValue('roleName')},function (data){
		executeScriptStr(data);
		//data为false给出提示 为true执行添加操作
		if(data!='true'){
			Ext.MessageBox.alert("提示框","您输入的角色名称已经存在！"); 
		}else{
			//执行添加操作
			$.ajax({
				type:'post',
				url:'../../oo/saveRole.action',
				data:{'pr.roleName':getValue('roleName'),'pr.roleDescribe':getValue('roleDescribe'),'pr.roleStatus':jobCmb.getValue(),'pr.roleType':roleType.getValue()},
				beforeSend:function (){
					//关闭添加窗口
					addrole.close();
				},success:function (data){
					executeScriptStr(data);
					if(data=='true'){
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
					if(data=='false'){
						Ext.MessageBox.alert("提示框","角色添加失败"); 
					}

				},error:function (xtc){
					addrole.close();
					Ext.MessageBox.alert("提示框","系统正在维护中。。。"); 
				}
			});
		}
	});
}
var tree;
var stores;
 //分配角色的tree的生成
function boxtree(urlrole){

	var reader1=Ext.create("Ext.data.reader.Json",{
		listeners:{
			//如果响应的内容不是一个json，那定是一个超时的<script>xxxx</script>格式的脚本；
			exception:function(reader,response, error,eOpts){
				executeScriptStr(response.responseText);
			}
		}	
	});

stores = Ext.create('Ext.data.TreeStore', {
    proxy: {
        type: 'ajax',
        url: '../../oo/getRolePermission.action?pmRole.roleId='+urlrole,
        reader:reader1
    },
    nodeParam : "id"
});
tree = Ext.create('Ext.tree.Panel', {
     rootVisible : false, //显示根节点   
     root: {   
       id: '-1',  
       text: '纽曼',   
       expanded: true,   
       leaf: false ,
       checked :true
    },  
    viewConfig : {   
        loadingText : "加载数据..."  
    },   
    store: stores,
    useArrows: true,
    renderTo: 'tree',
    width: '100%',
    height: 370,
    dockedItems: [{
        xtype: 'toolbar',
        items: [{
            text: '展开',
            icon:'../../resources/images/icons/icon_zk.gif',
            handler: function(){
            	//展开所有节点
            	tree.collapseAll();
                tree.expandAll();
            }
        }, {
            text: '收拢',
            icon:'../../resources/images/icons/icon_sl.gif',
            handler: function(){
            	//收拢所有节点
                tree.collapseAll();
            }
        },{
            text: '保存',
            icon:'../../resources/images/icons/tick.png',
            handler: function(){
            	//保存勾选的权限
            	baocun();
            }
        }]
    }]
           
});
tree.on('checkchange', function(node, checked) { 
	if(!checked){  
	    node.expand();  
		//设置节点选中
	    node.checked = false; 
	  	//设置选择节点下所有子节点
	  	node.eachChild(function (child) {  
	      	child.set('checked', false); 
	      	var childArray=child.childNodes;
	      	childTree(childArray,false);
	  	});
	  	parentTree(node,false);
    }else{
	    //展开选中的节点
	    node.expand();  
	    //设置节点选中
	    node.checked = true; 
	    //设置选择节点下所有子节点
	    node.eachChild(function (child) {  
	        child.set('checked', true); 
	        var childArray=child.childNodes;
	        childTree(childArray,true);
	    });            
	    //如果所有的子节点被选中，父节点也应该被选中
	    parentTree(node,true);
     }
}, tree);   
}
//勾选节点的复选框时需要勾选父节点和去掉父节点时子节点需要去掉的事件
function childTree(chlidArray,checked){
 if(!Ext.isEmpty(chlidArray)){
   Ext.Array.each(chlidArray,function(childArrNode,index,fog){               
         childArrNode.set('checked', checked); 
         if(childArrNode.childNodes!=null){
           childTree(childArrNode.childNodes,checked);
         }
   });
 }
};
//递归父节点
function parentTree(rootTree,checked){
    var node= rootTree.parentNode;
    if(node.raw!=null&&node.raw!=null){
	    if(!Ext.isEmpty(node)&&node.raw.id!="0"){
	     	//当前节点是否选择状态，响效父节点
	     	if(checked){
	           	node.data.checked = checked;
	     	node.updateInfo({checked:checked});
	     	if(node!=null){
	     		parentTree(node,checked);
	     	}
	    }else{
	     	var falsestr=[];
	     	var arr=node.childNodes;
	     	Ext.Array.each(arr,function(arrNode,index,fog){
	      		var chfalse=arr[index].data.checked;
	      		if(!chfalse){
	      			falsestr.push(chfalse);
	      		}
	     	});
	     	if(falsestr.length==arr.length){
	           	node.data.checked = checked;
	     	node.updateInfo({checked:checked});
	     	if(node!=null){
	     		parentTree(node,checked);
	     	}
	    }
    }
    }
    }
};
//分配权限的保存事件
function baocun(){
	var check=tree.getChecked();
	var html='';
	//循环获取用户勾选的权限节点
	for(var i=0;i<check.length;i++){
		if(i!=0&&i!=check.length){
			html+=",";
		}
		html+=check[i].get('id');
	}
	//执行保存操作
	$.ajax({
		type:'post',
		url:'../../oo/distributionPermission.action',
		data:{'pmRole.roleId':roleid,'permissionId':html},
		beforeSend:function (){
			boxrole.close();
		},success:function (data){
			executeScriptStr(data);
			
			if(data=='true'){
				if(html=='-1'){
					Ext.MessageBox.alert("提示框","此次没有分配任何权限!"); 
				}else
					Ext.MessageBox.alert("提示框","保存成功"); 
			}else{
				if(data=='false')
					Ext.MessageBox.alert("提示框","保存失败");
			}
		},error:function (xtc){
			Ext.MessageBox.alert("提示框","系统正在维护中。。。"); 
		}
	});
}