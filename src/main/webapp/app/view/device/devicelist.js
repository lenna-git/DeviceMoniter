Ext.define('AM.view.device.devicelist' ,{
    extend: 'Ext.panel.Panel',
    alias: 'widget.devicelist',
    border: true,
    layout: 'border',

    items: [
        {
            xtype: 'toolbar',
            region: 'north',
            overflowHandler: 'menu',
            listeners: {
                afterrender: function(toolbar) {
                    var role = SYS_USER ? SYS_USER.sysuserrole : null;
                    if (role !== 1) {
                        toolbar.items.each(function(item) {
                            var action = item.action;
                            if (action && (action === 'xz' || action === 'sc' || action === 'update' || action === 'export' || action === 'import')) {
                                item.hide();
                            }
                        });
                    }
                }
            },
            items: [
                {
                    xtype:'textfield',
                    width:150,
                    name:'queryxp',
                    emptyText:'请输入设备芯片',
                    fieldLabel:'芯片',
                    labelWidth: 40,
                    margin: '0 10 0 10',
                },
                {
                    xtype:'textfield',
                    width:150,
                    name:'querylx',
                    emptyText:'请输入设备类型',
                    fieldLabel:'类型',
                    labelWidth: 40,
                    margin: '0 10 0 0',
                },
                {
                    xtype:'textfield',
                    width:150,
                    name:'queryxh',
                    emptyText:'请输入设备型号',
                    fieldLabel:'型号',
                    labelWidth: 40,
                    margin: '0 10 0 0',
                },
                {
                    xtype:'textfield',
                    width:150,
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
                    action: 'import',
                    text: '导入Excel',
                    margin: '0 5 0 0',
                    padding: '5 15',
                    width: 90,
                    handler: function() {
                        // 创建文件上传表单
                        var uploadForm = Ext.create('Ext.form.Panel', {
                            fileUpload: true,
                            items: [{
                                xtype: 'filefield',
                                name: 'file',
                                fieldLabel: '选择文件',
                                labelWidth: 60,
                                width: 350,
                                buttonText: '浏览...',
                                allowBlank: false,
                                accept: '.xlsx,.xls'
                            }],
                            buttons: [{
                                text: '上传',
                                handler: function() {
                                    var form = this.up('form').getForm();
                                    if (form.isValid()) {
                                        form.submit({
                                            url: 'deviceaction/importExcel',
                                            waitMsg: '正在导入...',
                                            success: function(fp, o) {
                                                var result = o.result;
                                                if (result.success) {
                                                    Ext.Msg.alert('成功', result.message, function() {
                                                        var store = Ext.data.StoreMgr.lookup('deviceliststore');
                                                        store.reload();
                                                    });
                                                } else {
                                                    Ext.Msg.alert('失败', result.message);
                                                }
                                            },
                                            failure: function(fp, o) {
                                                Ext.Msg.alert('错误', '导入失败，请稍后重试');
                                            }
                                        });
                                    }
                                }
                            }, {
                                text: '取消',
                                handler: function() {
                                    uploadWindow.close();
                                }
                            }]
                        });
                        
                        var uploadWindow = Ext.create('Ext.window.Window', {
                            title: '导入设备信息',
                            width: 400,
                            height: 150,
                            modal: true,
                            layout: 'fit',
                            items: [uploadForm]
                        });
                        
                        uploadWindow.show();
                    }
                },
                {
                    xtype: 'button',
                    action: 'export',
                    text: '导出Excel',
                    margin: '0 5 0 0',
                    padding: '5 15',
                    width: 90,
                    handler: function() {
                        var toolbar = this.up('toolbar');
                        var queryxp = toolbar.down('textfield[name=queryxp]').getValue();
                        var querylx = toolbar.down('textfield[name=querylx]').getValue();
                        var queryxh = toolbar.down('textfield[name=queryxh]').getValue();
                        var querycs = toolbar.down('textfield[name=querycs]').getValue();
                        
                        var url = 'deviceaction/exportExcel?';
                        var params = [];
                        if (queryxp) params.push('devicexp=' + encodeURIComponent(queryxp));
                        if (querylx) params.push('devicetype=' + encodeURIComponent(querylx));
                        if (queryxh) params.push('devicexh=' + encodeURIComponent(queryxh));
                        if (querycs) params.push('devicecs=' + encodeURIComponent(querycs));
                        
                        url += params.join('&');
                        
                        window.location.href = url;
                    }
                },
                {
                    xtype: 'button',
                    action: 'update',
                    text: '修改',
                    margin: '0 5 0 0',
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
        loadMask: true,
        listeners: {
            cellmouseenter: function(view, td, cellIndex, record, tr, rowIndex, e) {
                var column = view.columns[cellIndex];
                var dataIndex = column.dataIndex;
                var value = record.get(dataIndex);
                if (value && typeof value === 'object') {
                    value = Ext.encode(value);
                }
                td.setAttribute('data-qtip', value ? String(value) : '');
            }
        }
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
        text:'序号',
        align:'center',
        width:60,
        renderer: function(value, metaData, record, rowIndex) {
            return rowIndex + 1;
        }
    },{
        text:'芯片',
        align:'center',
        dataIndex:'devCpu.cpuname',
        flex:1,
        renderer: function(value, metaData, record) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'类型',
        align:'center',
        dataIndex:'devType.typename',
        flex:1,
        renderer: function(value, metaData, record) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'型号',
        align:'center',
        dataIndex:'devicexh',
        flex:1,
        renderer: function(value, metaData, record) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'厂商',
        align:'center',
        dataIndex:'devManufacturer.manufacturername',
        flex:1,
        renderer: function(value, metaData, record) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'序列号',
        align:'center',
        dataIndex:'devicesn',
        flex:1,
        renderer: function(value, metaData, record) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'编号',
        align:'center',
        dataIndex:'deviceno',
        flex:1,
        renderer: function(value, metaData, record) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'送测日期',
        align:'center',
        dataIndex:'devicescdata',
        flex:1,
        renderer: function(value, metaData, record) {
            var displayValue = value;
            if (value && typeof value === 'string') {
                displayValue = value.replace('T', ' ');
            }
            if (!displayValue) displayValue = '';
            metaData.tdAttr = 'title="' + displayValue + '" data-qtip="' + displayValue + '"';
            return displayValue;
        }
    },{
        text:'安检日期',
        align:'center',
        dataIndex:'deviceajdata',
        flex:1,
        renderer: function(value, metaData, record) {
            var displayValue = value;
            if (value && typeof value === 'string') {
                displayValue = value.replace('T', ' ');
            }
            if (!displayValue) displayValue = '';
            metaData.tdAttr = 'title="' + displayValue + '" data-qtip="' + displayValue + '"';
            return displayValue;
        }
    },{
        text:'归还厂商日期',
        align:'center',
        dataIndex:'deviceghdata',
        flex:1,
        renderer: function(value, metaData, record) {
            var displayValue = value;
            if (value && typeof value === 'string') {
                displayValue = value.replace('T', ' ');
            }
            if (!displayValue) displayValue = '';
            metaData.tdAttr = 'title="' + displayValue + '" data-qtip="' + displayValue + '"';
            return displayValue;
        }
    },{
        text:'借用人',
        align:'center',
        dataIndex:'deviceyh.sysusername',
        flex:1,
        renderer: function(value, metaData, record) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'状态',
        align:'center',
        dataIndex:'devicestate.stateDetail',
        flex:1,
        renderer: function(value, metaData, record) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'维修记录',
        align:'center',
        flex:1,
        renderer: function(value, metaData, record) {
            var role = SYS_USER ? SYS_USER.sysuserrole : 1;
            var displayValue = role === 1 ? '查看' : '';
            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(displayValue) + '"';
            if (role === 1) {
                return '<a href="#" class="view-repair-link" data-id="' + record.get('id') + '" style="color: blue; text-decoration: underline;">查看</a>';
            }
            return '';
        }
    },{
        text:'操作',
        align:'center',
        width: 220,
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
                } else if (stateDetail === '借出中待修理') {
                    return '<a href="#" class="confirm-repair-link" data-id="' + record.get('id') + '" style="color: orange; text-decoration: underline;">维修</a>';
                } else if (stateDetail === '转借中待管理员批准') {
                    console.log('管理员批准转借状态检测');
                    return '<a href="#" class="approve-transfer-link" data-id="' + record.get('id') + '" style="color: green; text-decoration: underline;">同意转借</a> / <a href="#" class="reject-transfer-link" data-id="' + record.get('id') + '" style="color: red; text-decoration: underline;">拒绝</a>';
                }
            } else if (role === 2) {
                if (stateDetail === '已安检待借用') {
                    return '<a href="#" class="borrow-device-link" data-id="' + record.get('id') + '" style="color: blue; text-decoration: underline;">借用</a>';
                } else if (stateDetail === '借用中') {
                    var borrowerId = record.get('deviceyh') ? record.get('deviceyh').id : null;
                    var currentUserId = SYS_USER ? SYS_USER.id : null;
                    if (borrowerId === currentUserId) {
                        return '<a href="#" class="repair-device-link" data-id="' + record.get('id') + '" style="color: orange; text-decoration: underline;">申请报修</a> / <a href="#" class="return-device-link" data-id="' + record.get('id') + '" style="color: blue; text-decoration: underline;">退回</a> / <a href="#" class="transfer-device-link" data-id="' + record.get('id') + '" style="color: green; text-decoration: underline;">转借</a>';
                    }
                } else if (stateDetail === '转借中待转借人通过') {
                    var transferTargetId = record.get('transferTargetId');
                    var currentUserId = SYS_USER ? SYS_USER.id : null;
                    console.log('转借状态检测 - transferTargetId:', transferTargetId, 'currentUserId:', currentUserId, '类型:', typeof transferTargetId, typeof currentUserId);
                    if (transferTargetId == currentUserId) {
                        return '<a href="#" class="accept-transfer-link" data-id="' + record.get('id') + '" style="color: green; text-decoration: underline;">同意转借</a> / <a href="#" class="reject-transfer-by-user-link" data-id="' + record.get('id') + '" style="color: red; text-decoration: underline;">拒绝</a>';
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