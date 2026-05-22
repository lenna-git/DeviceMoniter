Ext.define('AM.view.user.LogOperationView', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.LogOperationView',
    id: 'LogOperationView',
    layout: 'border',
    title: '日志审计',
    
    requires: [
        'AM.store.LogOperationStore',
        'AM.model.LogOperationModel'
    ],
    
    initComponent: function() {
        var me = this;
        
        // 操作类型下拉框数据
        var operationTypes = [
            ['', '全部'],
            ['用户登录', '用户登录'],
            ['用户登出', '用户登出'],
            ['设备安检', '设备安检'],
            ['设备归还', '设备归还'],
            ['设备维修', '设备维修'],
            ['设备上架', '设备上架'],
            ['设备借用', '设备借用'],
            ['设备转借', '设备转借'],
            ['维修申请', '维修申请'],
            ['维修确认', '维修确认'],
            ['维修完成', '维修完成'],
            ['转借申请', '转借申请'],
            ['转借同意', '转借同意'],
            ['转借批准', '转借批准'],
            ['借用批准', '借用批准']
        ];
        
        // 操作模块下拉框数据
        var operationModules = [
            ['', '全部'],
            ['用户管理', '用户管理'],
            ['设备管理', '设备管理'],
            ['借用管理', '借用管理'],
            ['维修管理', '维修管理'],
            ['转借管理', '转借管理']
        ];
        
        // 操作结果下拉框数据
        var operationResults = [
            ['', '全部'],
            ['SUCCESS', '成功'],
            ['FAIL', '失败']
        ];
        
        // 工具栏
        me.tbar = {
            xtype: 'toolbar',
            overflowHandler: 'menu',
            items: [
                {
                    xtype: 'textfield',
                    fieldLabel: '用户',
                    labelWidth: 40,
                    width: 140,
                    id: 'log-operator-name'
                },
                {
                    xtype: 'combo',
                    fieldLabel: '类型',
                    labelWidth: 40,
                    width: 140,
                    id: 'log-operation-type',
                    store: operationTypes,
                    displayField: 1,
                    valueField: 0,
                    editable: false
                },
                {
                    xtype: 'combo',
                    fieldLabel: '模块',
                    labelWidth: 40,
                    width: 140,
                    id: 'log-operation-module',
                    store: operationModules,
                    displayField: 1,
                    valueField: 0,
                    editable: false
                },
                {
                    xtype: 'combo',
                    fieldLabel: '结果',
                    labelWidth: 40,
                    width: 120,
                    id: 'log-operation-result',
                    store: operationResults,
                    displayField: 1,
                    valueField: 0,
                    editable: false
                },
                {
                    xtype: 'datefield',
                    fieldLabel: '开始',
                    labelWidth: 40,
                    width: 160,
                    id: 'log-start-time',
                    format: 'Y-m-d'
                },
                {
                    xtype: 'datefield',
                    fieldLabel: '结束',
                    labelWidth: 40,
                    width: 160,
                    id: 'log-end-time',
                    format: 'Y-m-d'
                },
                {
                    xtype: 'button',
                    text: '查询',
                    width: 80,
                    handler: function() {
                        me.onSearch();
                    }
                },
                {
                    xtype: 'button',
                    text: '重置',
                    width: 80,
                    handler: function() {
                        me.onReset();
                    }
                }
            ]
        };
        
        // 创建共享的 store
        var logStore = Ext.create('AM.store.LogOperationStore');
        
        // 日志列表
        me.items = [{
            xtype: 'gridpanel',
            region: 'center',
            id: 'log-operation-grid',
            store: logStore,
            columns: [
                {
                    text: '序号',
                    xtype: 'rownumberer',
                    width: 60,
                    align: 'center'
                },
                {
                    text: '操作时间',
                    dataIndex: 'operationTime',
                    width: 180,
                    align: 'center',
                    renderer: function(value) {
                        if (value) {
                            return value.replace('T', ' ');
                        }
                        return '';
                    }
                },
                {
                    text: '操作用户',
                    dataIndex: 'operatorName',
                    width: 120,
                    align: 'center'
                },
                {
                    text: '用户角色',
                    dataIndex: 'operatorRole',
                    width: 100,
                    align: 'center',
                    renderer: function(value) {
                        return value == 1 ? '管理员' : '操作员';
                    }
                },
                {
                    text: '操作模块',
                    dataIndex: 'operationModule',
                    width: 120,
                    align: 'center'
                },
                {
                    text: '操作类型',
                    dataIndex: 'operationType',
                    width: 120,
                    align: 'center'
                },
                {
                    text: '操作描述',
                    dataIndex: 'operationDescription',
                    flex: 1,
                    renderer: function(value, metaData) {
                        if (value && value.length > 30) {
                            metaData.tdAttr = 'data-qtip="' + Ext.String.htmlEncode(value) + '"';
                            return value.substring(0, 30) + '...';
                        }
                        return value;
                    }
                },
                {
                    text: '操作结果',
                    dataIndex: 'operationResult',
                    width: 100,
                    align: 'center',
                    renderer: function(value) {
                        if (value == 'SUCCESS') {
                            return '<span style="color:green;">成功</span>';
                        } else if (value == 'FAIL') {
                            return '<span style="color:red;">失败</span>';
                        }
                        return value;
                    }
                },
                {
                    text: '目标类型',
                    dataIndex: 'targetType',
                    width: 120,
                    align: 'center'
                },
                {
                    text: '目标ID',
                    dataIndex: 'targetId',
                    width: 100,
                    align: 'center'
                },
                {
                    text: '目标名称',
                    dataIndex: 'targetName',
                    width: 150,
                    align: 'center'
                },
                {
                    text: 'IP地址',
                    dataIndex: 'ipAddress',
                    width: 150,
                    align: 'center'
                }
            ],
            bbar: {
                xtype: 'pagingtoolbar',
                store: logStore,
                displayInfo: true,
                displayMsg: '显示第 {0} - {1} 条，共 {2} 条',
                emptyMsg: '没有记录'
            },
            listeners: {
                itemdblclick: function(grid, record) {
                    me.showDetail(record);
                }
            }
        }];
        
        me.callParent(arguments);
    },
    
    listeners: {
        afterrender: function(me) {
            console.log('LogOperationView 渲染完成');
            var grid = me.down('#log-operation-grid');
            if (grid) {
                var store = grid.getStore();
                store.load();
            }
            registerLogOperationCallback(function(logId, operationType) {
                console.log('日志操作更新，自动刷新日志列表，日志ID:', logId, ', 操作类型:', operationType);
                var grid = me.down('#log-operation-grid');
                if (grid) {
                    var store = grid.getStore();
                    if (store) {
                        store.load();
                    }
                }
            });
        }
    },
    
    onSearch: function() {
        var me = this;
        var store = me.down('#log-operation-grid').getStore();
        
        // 清空旧数据
        store.removeAll();
        
        var params = {
            operatorName: Ext.getCmp('log-operator-name').getValue(),
            operationType: Ext.getCmp('log-operation-type').getValue(),
            operationModule: Ext.getCmp('log-operation-module').getValue(),
            operationResult: Ext.getCmp('log-operation-result').getValue(),
            startTime: Ext.getCmp('log-start-time').getValue(),
            endTime: Ext.getCmp('log-end-time').getValue()
        };
        
        store.getProxy().extraParams = params;
        store.loadPage(1);
    },
    
    onReset: function() {
        Ext.getCmp('log-operator-name').setValue('');
        Ext.getCmp('log-operation-type').setValue('');
        Ext.getCmp('log-operation-module').setValue('');
        Ext.getCmp('log-operation-result').setValue('');
        Ext.getCmp('log-start-time').setValue('');
        Ext.getCmp('log-end-time').setValue('');
        
        var store = this.down('#log-operation-grid').getStore();
        // 清空旧数据
        store.removeAll();
        store.getProxy().extraParams = {};
        store.loadPage(1);
    },
    
    showDetail: function(record) {
        Ext.create('Ext.window.Window', {
            title: '日志详情',
            width: 600,
            height: 400,
            modal: true,
            layout: 'fit',
            items: [{
                xtype: 'form',
                padding: 10,
                items: [
                    {
                        xtype: 'displayfield',
                        fieldLabel: '操作时间',
                        value: record.get('operationTime') ? record.get('operationTime').replace('T', ' ') : ''
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '操作用户ID',
                        value: record.get('operatorId') || ''
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '操作用户名',
                        value: record.get('operatorName') || ''
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '用户角色',
                        value: record.get('operatorRole') == 1 ? '管理员' : '操作员'
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '操作模块',
                        value: record.get('operationModule') || ''
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '操作类型',
                        value: record.get('operationType') || ''
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '操作描述',
                        value: record.get('operationDescription') || ''
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '操作结果',
                        value: record.get('operationResult') == 'SUCCESS' ? '成功' : '失败'
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '目标类型',
                        value: record.get('targetType') || ''
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '目标ID',
                        value: record.get('targetId') || ''
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '目标名称',
                        value: record.get('targetName') || ''
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: 'IP地址',
                        value: record.get('ipAddress') || ''
                    },
                    {
                        xtype: 'displayfield',
                        fieldLabel: '错误信息',
                        value: record.get('errorMessage') || '无'
                    }
                ]
            }]
        }).show();
    }
});