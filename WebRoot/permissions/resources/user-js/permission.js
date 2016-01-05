/*
 * 作用于：/permissions/permission/permissionlist.jsp页面
 * 功能说明：权限的添加删除修改；
 * 依赖文件：jquery
 * 作者：zy
 * 创建时间：2012-8-08
 */

Ext.require([
    'Ext.tree.*',
    'Ext.data.*',
    'Ext.window.MessageBox'
]);
var addpermission;
var updpermission;
var store;
var tree;
var node;
var trees;
var topContainer;
var namePaging;
Ext.onReady(function() {

	var reader=Ext.create("Ext.data.reader.Json",{
		listeners:{
			//如果响应的内容不是一个json，那定是一个超时的<script>xxxx</script>格式的脚本；
			exception:function(reader,response, error,eOpts){
				executeScriptStr(response.responseText);
			}
		}	
	});


	//创建权限树的数据源
    store = Ext.create('Ext.data.TreeStore', {
        proxy: {
            type: 'ajax',
            url: '../../oo/getTree.action?type=noUri',
            reader:reader
        },
        nodeParam : "id"
    });
   		

	//创建权限树
   topContainer= tree = Ext.create('Ext.tree.Panel', {
	     rootVisible : false, //显示根节点   
	     root: {   
	       id: '-1',  
	       text: '纽曼',   
	       expanded: true,   
	       leaf: false ,
	       hidden:true
	    },  
	    viewConfig : {   
	        loadingText : "加载数据..."  
	    },   
        store: store,
        useArrows: true,
        renderTo: 'tree-div',
        width: document.body.clientWidth,
        height: 613,
        title:'权限管理',
        dockedItems: [{
            xtype: 'toolbar',
            items: [{
                text: '展开',
                icon:'../../resources/images/icons/icon_zk.gif',
                handler: function(){
                	tree.collapseAll();
                    tree.expandAll();
                }
            }, {
                text: '收拢',
                icon:'../../resources/images/icons/icon_sl.gif',
                handler: function(){
                    tree.collapseAll();
                }
            }]
        }],//添加权限树的右击事件
        listeners :{'itemcontextmenu': function(tree, record, item, index, event) {   
    			event.preventDefault();   
   	 			rightClick.showAt(event.getXY());
   	 			node=record;
   	 			trees=tree;
			}
        }
               
    });
    window.onresize=function (){
		tree.setWidth(document.body.offsetWidth);
	}
	$(window).resize();		
    tree.expandAll();
    //创建权限树的右击菜单
    var rightClick = new Ext.menu.Menu({   
            id : 'rightClickCont',   
            floating :true,   
            plain : true,   
            items : [{   
                       id : 'rMenu1',   
                        text : '添加权限',
                        icon:'../../resources/images/icons/icon_tj.gif',   
                        // 增加菜单点击事件   
                        handler : function() {
	                        if(node.getDepth()<4){
	                        	addpermission=Ext.create('Ext.window.Window', {
					    			title: '添加权限',
					    			height: 150,
					    			width: 400,
					    			modal:true,
					    			resizable:false,
					    			constrain:true,
					    			contentEl:'addpermission',
					    			closeAction:'hide'
								});
								var id='';
									id=node.raw.id;
								document.getElementById('ParentId').value=id;
								document.getElementById('addpermission').style.display='';
								document.getElementById('permissionName').value='';
								document.getElementById('permissionUri').value='';
			    				addpermission.show(); 
	                        }else{
	                        	Ext.MessageBox.alert("提示框","权限只能添加4级"); 
	                        }
                        }   
                    }, {   
                        id : 'rMenu2',   
                        text : '编辑权限',
                        icon:'../../resources/images/icons/icon_bj.gif',  
                        handler : function() {  
                        			$.post('../../oo/getPermissionId.action',{'permission.permissionId':node.raw.id},function (data){
                        					executeScriptStr(data);
							  				var permission=data.split(',');
							  				document.getElementById('updpermissionName').value=permission[0];
											document.getElementById('updpermissionUri').value=permission[1]=='null'?'':permission[1];
											document.getElementById('updParentId').value=permission[2];
											namePaging=permission[0]+permission[2]+permission[1];
							  				updpermission=Ext.create('Ext.window.Window', {
							    				title: '修改权限',
							    				height: 150,
							    				width: 400,
							    				resizable:false,
							    				constrain:true,
							    				modal:true,
							    				contentEl:'updpermission',
							    				closeAction:'hide'
											});
											document.getElementById('updId').value=node.raw.id;
											document.getElementById('updpermission').style.display='';
					    					updpermission.show(); 
						  			});
                        }  
                    }, {   
                        id : 'rMenu3',   
                        text : '删除权限',
                        icon:'../../resources/images/icons/icon_sc.gif',  
                        handler : function() {   
                            Ext.Msg.show({
					              title:'操作确认',
					              msg:'您确定要删除吗？',
					              buttons:Ext.MessageBox.YESNO, //设置消息按钮
							      buttonText:{  
					                 yes: "确定", 
					                 no: "取消"
					             },
					             icon:Ext.MessageBox.QUESTION,   //显示警告图标
					             fn: function (ops){
					             	if(ops=='yes'){
		    	  						$.ajax({
											type:'post',
											url:"../../oo/ifRolePermission.action?prrp.rolePermissionPid="+node.raw.id,
											success:function(data){
												executeScriptStr(data);
													if(data=='true'){
					    	    		  				store.load({node:node.parentNode});
					    			  				}else{
					    			  					if(data!='false'){
						    			  					var json="{op:function (){"+data.substr(8,data.length-17)+"}}";
															eval('var rs='+json);
															if(rs.op)
																rs.op();
														}
					    				  				Ext.MessageBox.alert("提示框","已经有角色绑定了该权限，您不能删除该权限"); 
					    			  				}
											}
										});
		    						}
		    					 }
	    	 			 	});
                        }  
                    }]   
        });
Ext.create('Ext.Button', {
    text: '新增',
    width:50,
    height:23,
    renderTo: 'roleadd',
    handler: function() {
        save();
    }
});
Ext.create('Ext.Button', {
    text: '取消',
    width:50,
    height:23,
    renderTo: 'quxiao',
    handler: function() {
        addpermission.close();
    }
});
Ext.create('Ext.Button', {
    text: '修改',
    width:50,
    height:23,
    renderTo: 'roleupd',
    handler: function() {
        update();
    }
});
Ext.create('Ext.Button', {
    text: '取消',
    width:50,
    height:23,
    renderTo: 'quxiaos',
    handler: function() {
        updpermission.close();
    }
});
});

//添加权限树的节点的添加事件
function save(){
	var permissionName=document.getElementById('permissionName').value;
	var permissionUri=document.getElementById('permissionUri').value;
	var ParentId=document.getElementById('ParentId').value;
	if(permissionName.replace(/[ ]/g,"")==''){
		Ext.MessageBox.alert("提示框","您没有输入权限名称!"); 
		return;
	}
	if(permissionName.length>10){
		Ext.MessageBox.alert("提示框","您输入的权限名称过长");
		return ;
	}
	if(permissionUri.length>225){
		Ext.MessageBox.alert("提示框","您输入的权限url过长");
		return;
	}
	if(permissionName.match(/^[\u4E00-\u9FA50-9A-Za-z]{1,10}$/g)==null){
			Ext.MessageBox.alert("提示框","权限名称不能为特殊字符!");
			return false;
	}
	//是否输入权限名称 false给出提示 true执行添加操作
	if(permissionName.replace(/[ ]/g,"")!=''){
		$.post('../../oo/savePermission!boolPermissionName.action',{'permission.permissionName':permissionName},function (data){
			executeScriptStr(data);
				//data返回值如果为‘false’ 给出提示 其他的执行添加操作
				if(data=='false'){
					Ext.MessageBox.alert("提示框","您输入的权限名称已经存在"); 
					return ;
				}else{
					//检测是否超时
					if(data!='true'){
						var json="{op:function (){"+data.substr(8,data.length-17)+"}}";
						eval('var rs='+json);
						if(rs.op)
							rs.op();
					}else{
						$.ajax({
							type:'post',
							url:'../../oo/savePermission.action',
							data:{'permission.permissionName':permissionName
							,'permission.permissionUri':permissionUri,'permission.permissionParentId':ParentId},
							success:function(data){
								executeScriptStr(data);
									if(data=='ture')
									{
										Ext.MessageBox.alert("提示框","添加成功"); 
										store.load({node:node});
									}else{
										
										Ext.MessageBox.alert("提示框","添加失败"); 
									}
							},beforeSend:function (){
								addpermission.close();
								
							}
						});
					}
				}
		});
	}else{
		Ext.MessageBox.alert("提示框","您没有输入权限名称!"); 
	}
}
//修改权限树的节点保存事件
function update(){
	var permissionName=document.getElementById('updpermissionName').value;
	var permissionUri=document.getElementById('updpermissionUri').value;
	var ParentId=document.getElementById('updParentId').value;
	var Id=document.getElementById('updId').value;
	//是否输入权限名称 false给出提示 true执行修改操作
	if(permissionName.replace(/[ ]/g,"")!=''){
		if(permissionName.match(/^[\u4E00-\u9FA50-9a-zA-Z]{1,10}$/g)==null){
			Ext.MessageBox.alert("提示框","权限名称不能为特殊字符!");
			return false;
		}
		if(namePaging==(permissionName+ParentId+permissionUri)){
			Ext.MessageBox.alert("提示框","您没有对权限做任何修改"); 
			return ;
		}
		//检测名称是否存在 
		$.post('../../oo/savePermission!getPermissionName.action',{'permission.permissionName':permissionName,'permission.permissionId':Id},function (data){
			executeScriptStr(data);
					var bool=false;
					if(data=='true'){
						bool=true;
					}else{
						if(data==Id){
							bool=true;
						}else{
							bool=false;
						}
					}
					//true存在 false不存在
					if(bool){
						$.ajax({
							type:'post',
							url:'../../oo/updatePermission.action',
							data:{'permission.permissionName':permissionName
							,'permission.permissionUri':permissionUri
							,'permission.permissionParentId':ParentId,'permission.permissionId':Id},
							success:function(data){
								executeScriptStr(data);
									if(data=='true'){
										Ext.MessageBox.alert("提示框","修改成功"); 
										store.load({node:node.parentNode});
									}else{
										Ext.MessageBox.alert("提示框","修改失败"); 
									}
							},beforeSend:function (){
								updpermission.close();
							}
						});
					}else{
						Ext.MessageBox.alert("提示框","您输入的权限名称已经存在!"); 
					}
		});
	}else{
		Ext.MessageBox.alert("提示框","您没有输入权限名称!"); 
	}
}