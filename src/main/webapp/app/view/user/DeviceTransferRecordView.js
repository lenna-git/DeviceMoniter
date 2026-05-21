Ext.define('AM.view.user.DeviceTransferRecordView', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.DeviceTransferRecordView',
    requires: ['AM.model.DeviceTransferRecordModel', 'AM.store.DeviceTransferRecordStore'],
    border: true,
    layout: 'border',
    items: [{
        xtype: 'toolbar',
        region: 'north',
        items: [{
            margin: '0 10 0 10',
            xtype: 'textfield',
            name: 'searchKeyword',
            width: 300,
            height: 30,
            emptyText: '请输入设备编号/借用人/转借人查询',
            fieldLabel: '查询',
            labelWidth: 40,
        }, {
            xtype: 'button',
            action: 'select',
            text: '查询',
            width: 60,
        }, {
            xtype: 'button',
            action: 'export',
            text: '导出Excel',
            width: 80,
        }]
    }, {
        border: false,
        xtype: 'DeviceTransferRecordGrid',
        region: 'center',
    }],
});

Ext.define('AM.view.user.DeviceTransferRecordGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.DeviceTransferRecordGrid',
    store: 'DeviceTransferRecordStore',
    autoScroll:true,
    forceFit:true,
    viewConfig: {
        loadMask: true
    },
    columns: [{
        text: '设备编号',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'device.deviceno',
        flex: 1,
    }, {
        text: '序列号',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'device.devicesn',
        flex: 1,
    }, {
        text: '芯片',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'device.devCpu.cpuname',
        flex: 1,
    }, {
        text: '类型',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'device.devType.typename',
        flex: 1,
    }, {
        text: '型号',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'device.devicexh',
        flex: 1,
    }, {
        text: '厂商',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'device.devManufacturer.manufacturername',
        flex: 1,
    }, {
        text: '原借用人',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'fromUser',
        flex: 1,
        renderer: function(value) {
            return value ? value.sysusername : '';
        }
    }, {
        text: '转借申请日期',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'transferDate',
        flex: 1,
    }, {
        text: '新借用人',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'toUser',
        flex: 1,
        renderer: function(value) {
            return value ? value.sysusername : '';
        }
    }, {
        text: '新借用人同意日期',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'approvalDate',
        flex: 1,
    }, {
        text: '批准管理员',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'adminApprovalUser',
        flex: 1,
        renderer: function(value) {
            return value ? value.sysusername : '';
        }
    }, {
        text: '批准日期',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'adminApprovalDate',
        flex: 1,
    }, {
        text: '状态',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'status',
        flex: 1,
        renderer: function(value) {
            if (value == null) return '';
            switch (value) {
                case 1: return '申请中';
                case 2: return '新借用人已同意';
                case 3: return '管理员已同意';
                case 4: return '已拒绝';
                default: return '未知状态';
            }
        }
    }, {
        text: '详情',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'detail',
        flex: 1,
    }],
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'DeviceTransferRecordStore',
        displayInfo: true,
        displayMsg: '显示第 {0} - {1} 条，共 {2} 条',
        emptyMsg: '没有数据'
    }
});
