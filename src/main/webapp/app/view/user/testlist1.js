Ext.define('AM.view.user.testlist1' ,{
    extend: 'Ext.panel.Panel',
    alias: 'widget.userlist1',
    border:true,
    // layout:'vbox',
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
            // width: '500',
        },
        {
            xtype: 'button',
            action:'sc',
            text:'删除',
            height:'50',
            // width: '500',
        },
        {
            xtype: 'button',
            action:'update',
            text:'修改',
            height:'50',
            // width: '500',
        },
        {
            xtype: 'button',
            action:'test',
            text:'测试',
            height:'50',
            // width: '500',
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
            //角色字段是数字，需要转换为对应文本，1对应管理员，2对应用户
            return value===1?'管理员':'普通用户';
        }
    },{
        text:'操作',
        align:'center',
        xtype:'actioncolumn',
        dataIndex:'sysuseroperation',
        flex:1,
        renderer:function (grid,rowIndex,colIndex){
            //为按钮添加唯一的标识符
            return '<button class="operation-button" >查看</button>';
        },
        // items:[{
        //     xtype:'button',
        //     text:'查看',
        //     handler:function (grid,rowIndex,colIndex){
        //         var record = grid.getStore().getAt(rowIndex);
        //     }
        // }]

    }],

})