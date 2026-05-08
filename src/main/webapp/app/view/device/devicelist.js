Ext.define('AM.view.device.devicelist' ,{
    extend: 'Ext.panel.Panel',
    alias: 'widget.devicelist',
    border:true,


    layout:'vbox',

    items:[{
        xtype:'panel',
        layout: 'hbox',
        items:[{
            xtype:'textfield',
            width:240,
            height:30,
            name:'queryxp',  //提交时的参数名
            emptyText:'请输入设备芯片',
            fieldLabel:'芯片',
            labelWidth: 40,
            margin: '0 10 0 20',
        },{
            xtype:'textfield',
            width:240,
            height:30,
            name:'querylx',  //提交时的参数名
            emptyText:'请输入设备类型',
            fieldLabel:'类型',
            labelWidth: 40,
            margin: '0 10 0 10',//上 右 下 左
        },{
            xtype:'textfield',
            width:240,
            height:30,
            name:'queryxh',  //提交时的参数名
            emptyText:'请输入设备型号',
            fieldLabel:'型号',
            labelWidth: 40,
            margin: '0 10 0 10',
        },{
            xtype:'textfield',
            width:240,
            height:30,
            name:'querycs',  //提交时的参数名
            emptyText:'请输入设备厂商',
            fieldLabel:'厂商',
            labelWidth: 40,
            margin: '0 20 0 10',//上 右 下 左
        },{
            xtype:'button',
            text:'查询',
            action: 'devicesearch',
            width:60,
            height:30,
            margin: '0 0 0 -20',//上 右 下 左
            //iconCls:'x-fa fa-search',
            //handler:doSearch,

        }]
    },
        {
            xtype:'devicelistgrid',
            width:'100%',
            flex:1,
            margin: '20 0 0 0',//上 右 下 左
        },
        {
            xtype: 'panel',
            layout:'hbox',
            items: [{
                xtype: 'button',
                action: 'xz',
                text: '新增',
                height: '50',
            }, {
                xtype: 'button',
                action: 'sc',
                text: '删除',
                height: '50',
                // width: '500',
            }, {
                xtype: 'button',
                action: 'update',
                text: '修改',
                height: '50',
                // width: '500',
            }, {
                xtype: 'button',
                action: 'test',
                text: '测试',
                height: '50',
                // width: '500',
            }],
        }],

});

Ext.define('AM.view.device.devicelistgrid',{
    extend:'Ext.grid.Panel',
    alias:'widget.devicelistgrid',
    store:'deviceliststore',//拿数据
    columns:[{
        text:'芯片',
        align:'center',
        dataIndex:'devicexp',//与model里面的字段对应
        flex:1,
    },{
        text:'类型',
        align:'center',
        dataIndex:'devicetype',
        flex:1,
    },{
        text:'型号',
        align:'center',
        dataIndex:'devicexh',
        flex:1,
    },{
        text:'厂商',
        align:'center',
        dataIndex:'devicecs',
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
        dataIndex:'devicestate',
        flex:1,
    },{
        text:'操作',
        align:'center',
        dataIndex:'deviceop',
        flex:1,
    },{
        text:'操作1',
        align:'center',
        dataIndex:'deviceop1',
        flex:1,
    }],

})