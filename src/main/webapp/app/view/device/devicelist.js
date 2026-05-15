Ext.define('AM.view.device.devicelist' ,{
    extend: 'Ext.panel.Panel',
    alias: 'widget.devicelist',
    border: true,
    layout: 'border',

    items: [
        {
            xtype: 'toolbar',
            region: 'north',
            items: [
                {
                    xtype:'textfield',
                    width:180,
                    name:'queryxp',
                    emptyText:'请输入设备芯片',
                    fieldLabel:'芯片',
                    labelWidth: 40,
                    margin: '0 10 0 10',
                },
                {
                    xtype:'textfield',
                    width:180,
                    name:'querylx',
                    emptyText:'请输入设备类型',
                    fieldLabel:'类型',
                    labelWidth: 40,
                    margin: '0 10 0 0',
                },
                {
                    xtype:'textfield',
                    width:180,
                    name:'queryxh',
                    emptyText:'请输入设备型号',
                    fieldLabel:'型号',
                    labelWidth: 40,
                    margin: '0 10 0 0',
                },
                {
                    xtype:'textfield',
                    width:180,
                    name:'querycs',
                    emptyText:'请输入设备厂商',
                    fieldLabel:'厂商',
                    labelWidth: 40,
                    margin: '0 10 0 0',
                },
                {
                    xtype:'button',
                    text:'查询',
                    action: 'devicesearch',
                    width:60,
                    margin: '0 10 0 0',
                },
                '->',
                {
                    xtype: 'button',
                    action: 'xz',
                    text: '新增',
                    margin: '0 5 0 0',
                    padding: '5 15'
                },
                {
                    xtype: 'button',
                    action: 'sc',
                    text: '删除',
                    margin: '0 5 0 0',
                    padding: '5 15'
                },
                {
                    xtype: 'button',
                    action: 'update',
                    text: '修改',
                    padding: '5 15'
                }
            ]
        },
        {
            xtype:'devicelistgrid',
            region: 'center'
        }
    ]
});

Ext.define('AM.view.device.devicelistgrid',{
    extend:'Ext.grid.Panel',
    alias:'widget.devicelistgrid',
    store:'deviceliststore',
    autoScroll:true,
    forceFit:true,
    viewConfig: {
        loadMask: true
    },
    columns:[{
        text:'芯片',
        align:'center',
        dataIndex:'devCpu.cpuname',
        flex:1,
    },{
        text:'类型',
        align:'center',
        dataIndex:'devType.typename',
        flex:1,
    },{
        text:'型号',
        align:'center',
        dataIndex:'devicexh',
        flex:1,
    },{
        text:'厂商',
        align:'center',
        dataIndex:'devManufacturer.manufacturername',
        flex:1,
    },{
        text:'序列号',
        align:'center',
        dataIndex:'devicesn',
        flex:1,
    },{
        text:'编号',
        align:'center',
        dataIndex:'deviceno',
        flex:1,
    },{
        text:'送测日期',
        align:'center',
        dataIndex:'devicescdata',
        flex:1,
    },{
        text:'安检日期',
        align:'center',
        dataIndex:'deviceajdata',
        flex:1,
    },{
        text:'归还厂商日期',
        align:'center',
        dataIndex:'deviceghdata',
        flex:1,
    },{
        text:'借用人',
        align:'center',
        dataIndex:'deviceyh',
        flex:1,
    },{
        text:'状态',
        align:'center',
        dataIndex:'devicestate.stateDetail',
        flex:1,
    },{
        text:'操作',
        align:'center',
        dataIndex:'deviceop',
        flex:1,
    }],
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'deviceliststore',
        displayInfo: true,
        displayMsg: '显示第 {0} - {1} 条，共 {2} 条',
        emptyMsg: '没有数据'
    }

})