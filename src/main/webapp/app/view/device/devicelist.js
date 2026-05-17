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
    listeners: {
        afterrender: function(grid) {
            var role = SYS_USER ? SYS_USER.sysuserrole : null;
            var columns = grid.columns;
            for (var i = 0; i < columns.length; i++) {
                if (columns[i].text === '维修记录') {
                    if (role === 1) {
                        columns[i].show();
                    } else {
                        columns[i].hide();
                    }
                    break;
                }
            }
        }
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
        dataIndex:'deviceyh.sysusername',
        flex:1,
    },{
        text:'状态',
        align:'center',
        dataIndex:'devicestate.stateDetail',
        flex:1,
    },{
        text:'维修记录',
        align:'center',
        flex:1,
        renderer: function(value, metaData, record) {
            var role = SYS_USER ? SYS_USER.sysuserrole : 1;
            if (role === 1) {
                return '<a href="#" class="view-repair-link" data-id="' + record.get('id') + '" style="color: blue; text-decoration: underline;">查看</a>';
            }
            return '';
        }
    },{
        text:'操作',
        align:'center',
        flex:1,
        renderer: function(value, metaData, record) {
            var stateDetail = record.get('devicestate') ? record.get('devicestate').stateDetail : '';
            var role = SYS_USER ? SYS_USER.sysuserrole : 1;
            console.log('操作列渲染 - 角色:', role, '状态:', stateDetail, '设备ID:', record.get('id'));
            
            if (role === 1) {
                if (stateDetail === '已录入待安检') {
                    return '<a href="#" class="check-device-link" data-id="' + record.get('id') + '" style="color: blue; text-decoration: underline;">安检</a>';
                } else if (stateDetail === '已安检待借用') {
                    return '<a href="#" class="shelve-device-link" data-id="' + record.get('id') + '" style="color: blue; text-decoration: underline;">下架</a>';
                } else if (stateDetail === '修理中') {
                    return '<a href="#" class="unshelve-device-link" data-id="' + record.get('id') + '" style="color: blue; text-decoration: underline;">上架</a>';
                } else if (stateDetail === '借用中待通过') {
                    return '<a href="#" class="approve-borrow-link" data-id="' + record.get('id') + '" style="color: green; text-decoration: underline;">通过</a> / <a href="#" class="reject-borrow-link" data-id="' + record.get('id') + '" style="color: red; text-decoration: underline;">拒绝</a>';
                } else if (stateDetail === '申请归还中待通过') {
                    return '<a href="#" class="approve-return-link" data-id="' + record.get('id') + '" style="color: green; text-decoration: underline;">批准</a>';
                }
            } else if (role === 2) {
                if (stateDetail === '已安检待借用') {
                    return '<a href="#" class="borrow-device-link" data-id="' + record.get('id') + '" style="color: blue; text-decoration: underline;">借用</a>';
                } else if (stateDetail === '借用中') {
                    var borrowerId = record.get('deviceyh') ? record.get('deviceyh').id : null;
                    var currentUserId = SYS_USER ? SYS_USER.id : null;
                    if (borrowerId === currentUserId) {
                        return '<a href="#" class="repair-device-link" data-id="' + record.get('id') + '" style="color: orange; text-decoration: underline;">申请报修</a> / <a href="#" class="return-device-link" data-id="' + record.get('id') + '" style="color: blue; text-decoration: underline;">退回</a>';
                    }
                }
            }
            
            return '-';
        }
    }],
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'deviceliststore',
        displayInfo: true,
        displayMsg: '显示第 {0} - {1} 条，共 {2} 条',
        emptyMsg: '没有数据'
    }

})