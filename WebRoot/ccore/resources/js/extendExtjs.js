/*
 * 作用于：/ccore所有Ext.grid.Panel中带分页显示的js；
 * 功能说明：扩展extjs中的一些功能，以适应我们的系统；
                                 引用该js时，位置应在extjs与用户js的中间；
 * 依赖文件：jquery,extjs
 * 作者：hyq
 * 创建时间：2012-10-22
 */
 
 /*
  * 使得在第一页中的[displayMsg]值显示为实际行数，而非固定行数；
  * grid : extjs表格(Ext.grid.Panel)组件对象
  * pagingtoolbarId:分页工具条的id属性值
  */
 function showActualRowCount(grid,pagingtoolbarId){
 	/**
 	//因仍无法解决当前页实际行数与DisplayMsg的一致问题，所以先将DisplayMsg消息去掉；
 	//以后待缓存的问题解决后，再恢复显示；  
 	var com=grid.getDockedComponent(pagingtoolbarId);
	var msg=Ext.String.format(
           com.displayMsg,
           com.getPageData().fromRecord,
           com.getPageData().currentPage==1?grid.getStore().getCount():com.getPageData().toRecord,//第一页实际行数
           com.getPageData().total
    );
    //如果结果行不为0，则显示格式化字符串，否则显示“为空”字符串；
    if(grid.getStore().getCount()!=0)
		com.child('#displayItem').setText(msg);
	else
		com.child('#displayItem').setText(com.emptyMsg);
	*/
	var com=grid.getDockedComponent(pagingtoolbarId);
	//如果结果行不为0，则显示格式化字符串，否则显示“为空”字符串；
    if(grid.getStore().getCount()!=0)
		com.child('#displayItem').setText("此次查出 <b>"+com.getPageData().total+"</b> 行数据");
	else
		com.child('#displayItem').setText(com.emptyMsg);	
}

/*
 * 为分页工具条加一个跳到按钮：重写了Ext.toolbar.Paging的return对象；
 */
Ext.define('Ext.newsmy.extends.Paging',{
    	override:"Ext.toolbar.Paging",
    	getPagingItems:function() {
        var me = this;
        return [{
            itemId: 'first',
            tooltip: me.firstText,
            overflowText: me.firstText,
            iconCls: Ext.baseCSSPrefix + 'tbar-page-first',
            disabled: true,
            handler: me.moveFirst,
            scope: me
        },{
            itemId: 'prev',
            tooltip: me.prevText,
            overflowText: me.prevText,
            iconCls: Ext.baseCSSPrefix + 'tbar-page-prev',
            disabled: true,
            handler: me.movePrevious,
            scope: me
        },
        '-',       
        {//在这里把以前的文本label，改成一个按钮并绑定处理事件；
         	scope: me,
            xtype: 'button',
            itemId: 'jumpto',
            text: me.beforePageText,
            handler:function(){
            	var me=this;
            	var  pageData = me.getPageData();            	
	            var pageNum = me.readPageFromInput(pageData);
	            //如果在页号输入有效，则加载指定页数据；	
				if (pageNum !== false) {
	                pageNum = Math.min(Math.max(1, pageNum), pageData.pageCount);
	                if(me.fireEvent('beforechange', me, pageNum) !== false){
	                    me.store.loadPage(pageNum);
	                }
	            }   
            }
        }, 
        {
            xtype: 'numberfield',
            itemId: 'inputItem',
            name: 'inputItem',
            cls: Ext.baseCSSPrefix + 'tbar-page-number',
            allowDecimals: false,
            minValue: 1,
            hideTrigger: true,
            enableKeyEvents: true,
            keyNavEnabled: false,
            selectOnFocus: true,
            submitValue: false,
            // mark it as not a field so the form will not catch it when getting fields
            isFormField: false,
            width: me.inputItemWidth,
            margins: '-1 2 3 2',
            listeners: {
                scope: me,
                keydown: me.onPagingKeyDown
            }
        },                
        {
            xtype: 'tbtext',
            itemId: 'afterTextItem',
            text: Ext.String.format(me.afterPageText, 1)
        },
        '-',
        {
            itemId: 'next',
            tooltip: me.nextText,
            overflowText: me.nextText,
            iconCls: Ext.baseCSSPrefix + 'tbar-page-next',
            disabled: true,
            handler: me.moveNext,
            scope: me
        },{
            itemId: 'last',
            tooltip: me.lastText,
            overflowText: me.lastText,
            iconCls: Ext.baseCSSPrefix + 'tbar-page-last',
            disabled: true,
            handler: me.moveLast,
            scope: me
        },
        '-',
        {
            itemId: 'refresh',
            tooltip: me.refreshText,
            overflowText: me.refreshText,
            iconCls: Ext.baseCSSPrefix + 'tbar-loading',
            handler: me.doRefresh,
            scope: me
        }];
    }
 });
 