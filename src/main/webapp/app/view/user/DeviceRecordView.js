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
    },{
        text:'设备编号',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device',
        flex:2,
        renderer: function(value) {
            return value ? value.deviceno : '';
        }
    },{
        text:'序列号',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device',
        flex:2,
        renderer: function(value) {
            return value ? value.devicesn : '';
        }
    },{
        text:'芯片',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device',
        flex:2,
        renderer: function(value) {
            return value && value.devCpu ? value.devCpu.cpuname : '';
        }
    },{
        text:'型号',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device',
        flex:2,
        renderer: function(value) {
            return value ? value.devicexh : '';
        }
    },{
        text:'类型',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device',
        flex:1,
        renderer: function(value) {
            return value && value.devType ? value.devType.typename : '';
        }
    },{
        text:'厂商',
        align:'center',
        style:'font-size:16px',
        dataIndex:'device',
        flex:2,
        renderer: function(value) {
            return value && value.devManufacturer ? value.devManufacturer.manufacturername : '';
        }
    },{
        text:'借用人',
        align:'center',
        style:'font-size:16px',
        dataIndex:'borrowUser',
        flex:2,
        renderer: function(value) {
            return value ? value.sysusername : '';
        }
    },{
        text:'借用日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'borrorDate',
        flex:2,
    },{
        text:'批准人',
        align:'center',
        style:'font-size:16px',
        dataIndex:'sysUser',
        flex:2,
        renderer: function(value) {
            return value ? value.sysusername : '';
        }
    },{
        text:'批准借用日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'approvalDate',
        flex:2,
    },{
        text:'归还日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'returnDate',
        flex:2,
    },{
        text:'批准归还人',
        align:'center',
        style:'font-size:16px',
        dataIndex:'returnApprovalUser',
        flex:2,
        renderer: function(value) {
            return value ? value.sysusername : '';
        }
    },{
        text:'批准归还日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'returnApprovalDate',
        flex:2,
    },{
        text:'详情',
        align:'center',
        style:'font-size:16px',
        dataIndex:'detail',
        flex:2,
    }],
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'devicerecordstore',
        displayInfo: true,
        displayMsg: '显示第 {0} - {1} 条，共 {2} 条',
        emptyMsg: '没有数据'
    }

})
