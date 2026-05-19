Ext.define('AM.view.user.userlist', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.userlist1',
    border: true,
    layout: 'border',
    items: [
        {
            xtype: 'toolbar',
            region: 'north',
            items: [
                '->',
                {
                    xtype: 'button',
                    action: 'xj',
                    text: '新增',
                    iconCls: 'add-icon',
                    margin: '0 5 0 0',
                    padding: '5 15'
                },
                {
                    xtype: 'button',
                    action: 'sc',
                    text: '删除',
                    iconCls: 'delete-icon',
                    margin: '0 5 0 0',
                    padding: '5 15'
                },
                {
                    xtype: 'button',
                    action: 'update',
                    text: '修改',
                    iconCls: 'edit-icon',
                    padding: '5 15'
                }
            ]
        },
        {
            xtype: 'testlistgrid',
            region: 'center'
        }
    ]
});
Ext.define('AM.view.user.testlistgrid',{
    extend:'Ext.grid.Panel',
    alias:'widget.testlistgrid',
    store:'userliststore',
    autoScroll:true,
    forceFit:true,
    viewConfig: {
        loadMask: true
    },
    columns:[{
        text:'姓名',
        align:'center',
        dataIndex:'sysusername',
        flex:2,
    },{
        text:'角色',
        align:'center',
        dataIndex:'sysuserrole',
        flex:2,
        renderer:function (value){
            return value===1?'管理员':'普通用户';
        }
    }],
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'userliststore',
        displayInfo: true,
        displayMsg: '显示第 {0} - {1} 条，共 {2} 条',
        emptyMsg: '没有数据'
    }

})