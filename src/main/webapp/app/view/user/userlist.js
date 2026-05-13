Ext.define('AM.view.user.userlist', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.userlist1',
    border:true,
    items:[
        {
            xtype:'testlistgrid',
            width:'100%',
            flex:1,
        },
        {
            xtype: 'button',
            action:'xj',
            text:'新增',
            height:'50',
        },
        {
            xtype: 'button',
            action:'sc',
            text:'删除',
            height:'50',
        },
        {
            xtype: 'button',
            action:'update',
            text:'修改',
            height:'50',
        },
        {
            xtype: 'button',
            action:'test',
            text:'测试',
            height:'50',
        },
    ],

});
Ext.define('AM.view.user.testlistgrid',{
    extend:'Ext.grid.Panel',
    alias:'widget.testlistgrid',
    store:'userliststore',
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
    },{
        text:'操作',
        align:'center',
        xtype:'actioncolumn',
        dataIndex:'sysuseroperation',
        flex:1,
        renderer:function (grid,rowIndex,colIndex){
            return '<button class="operation-button" >查看</button>';
        },

    }],

})