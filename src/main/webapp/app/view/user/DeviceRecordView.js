Ext.define('AM.view.user.DeviceRecordView' ,{
    extend: 'Ext.panel.Panel',
    alias: 'widget.DeviceRecordView',
    border:true,
    // layout:'hbox',
    items:[
        {
            xtype: 'panel',
            layout:'hbox',
            items: [{
                margin: '10 10 10 10',
                xtype: 'textfield',
                name: 'searchUserId',
                width:240,
                height:30,
                emptyText:' 请输入用户id查询',
            },{
                margin: '10 10 10 10',
                xtype: 'textfield',
                name: 'searchDetail',
                width:240,
                height:30,
                emptyText:' 请输入Detail查询',
            },{
                margin: '15 10 10 10',
                xtype: 'button',
                action:'select',
                text:'查询',
                height:'30',
            }]
        },
        {
            border: false,
            xtype:'DeviceRecordGrid',
            width:'100%',
            flex:1,
        },
        {
            xtype: 'panel',
            layout:{
                type:'hbox',
                pack:'end'
            },
            items: [
            {
                margin: '10 10 10 10',
                xtype: 'button',
                action:'xj',
                text:'新建',
                height:'30',
            },
            {
                margin: '10 10 10 10',
                xtype: 'button',
                action:'sc',
                text:'删除',
                height:'30',
            }
            ]
        }
    ],

});

Ext.define('AM.view.user.DeviceRecordGrid',{
    extend:'Ext.grid.Panel',
    alias:'widget.DeviceRecordGrid',
    store:'DeviceRecordStore',
    //
    columns:[
    {
        text:'借用人',
        align:'center',
        style:'font-size:16px',
        dataIndex:'sysUser.sysusername',
        flex:1,
    },
    {
        text:'设备编号',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device.deviceno',
        flex:1,
    },
    {
        text:'芯片',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device.devicexp',
        flex:1,
    },{
        text:'类型',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device.devicetype',
        flex:1,
    },{
        text:'型号',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device.devicexh',
        flex:1,
    },{
        text:'厂商',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device.devicecs',
        flex:1,
    },
    {
        text:'借用日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'borrorDate',
        flex:1,
    },{
        text:'归还日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'returnDate',
        flex:1,
    }],

})