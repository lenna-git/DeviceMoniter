Ext.define('AM.view.user.DeviceRecordView' ,{
    extend: 'Ext.panel.Panel',
    alias: 'widget.DeviceRecordView',
    border:true,
    layout: 'border',
    items:[
        {
            xtype: 'toolbar',
            region: 'north',
            items: [{
                margin: '0 10 0 10',
                xtype: 'textfield',
                name: 'searchKeyword',
                width:300,
                height:30,
                emptyText:'请输入借用人/设备编号/详情查询',
                fieldLabel:'查询',
                labelWidth: 40,
            },{
                xtype: 'button',
                action:'select',
                text:'查询',
                width:60,
            },{
                xtype: 'button',
                action:'export',
                text:'导出 Excel',
                width:80,
            }]
        },
        {
            border: false,
            xtype:'DeviceRecordGrid',
            region: 'center',
        }
    ],

});

Ext.define('AM.view.user.DeviceRecordGrid',{
    extend:'Ext.grid.Panel',
    alias:'widget.DeviceRecordGrid',
    store:'devicerecordstore',
    autoScroll:true,
    forceFit:true,
    viewConfig: {
        loadMask: true
    },
    columns:[
    {
        text:'ID',
        align:'center',
        style:'font-size:16px',
        dataIndex:'id',
        flex:1,
    }],
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'devicerecordstore',
        displayInfo: true,
        displayMsg: '显示第 {0} - {1} 条，共 {2} 条',
        emptyMsg: '没有数据'
    }

})
