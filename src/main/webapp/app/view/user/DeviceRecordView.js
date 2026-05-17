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
                name: 'searchKeyword',
                width:300,
                height:30,
                emptyText:' 请输入借用人/设备编号/详情查询',
            },{
                margin: '15 10 10 10',
                xtype: 'button',
                action:'select',
                text:'查询',
                height:'30',
            },{
                margin: '15 10 10 10',
                xtype: 'button',
                action:'export',
                text:'导出Excel',
                height:'30',
            }]
        },
        {
            border: false,
            xtype:'DeviceRecordGrid',
            width:'100%',
            flex:1,
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
        dataIndex:'device.devCpu.cpuname',
        flex:1,
    },{
        text:'类型',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device.devType.typename',
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
        dataIndex:'device.devManufacturer.manufacturername',
        flex:1,
    },
    {
        text:'借用人',
        align:'center',
        style:'font-size:16px',
        dataIndex:'borrowerUsername',
        flex:1,
    },
    {
        text:'借用日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'borrorDate',
        flex:1,
    },{
        text:'批准人',
        align:'center',
        style:'font-size:16px',
        dataIndex:'sysUser.sysusername',
        flex:1,
    },{
        text:'批准借用日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'approvalDate',
        flex:1,
    },{
        text:'归还日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'returnDate',
        flex:1,
    },{
        text:'批准归还人',
        align:'center',
        style:'font-size:16px',
        dataIndex:'returnApprovalUsername',
        flex:1,
    },{
        text:'批准归还日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'returnApprovalDate',
        flex:1,
    }],

})