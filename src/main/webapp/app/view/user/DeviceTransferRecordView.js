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
    columns: [
        {
        text: '设备编号',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'deviceNo',
        flex: 1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },
        {
        text: '序列号',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'deviceSn',
        flex: 1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },
        {
        text: '芯片',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'cpuName',
        flex: 1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    }, {
        text: '类型',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'typeName',
        flex: 1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    }, {
        text: '型号',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'deviceXh',
        flex: 1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    }, {
        text: '厂商',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'manufacturerName',
        flex: 1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    }, {
        text: '原借用人',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'fromUsername',
        flex: 1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },
        {
        text: '转借申请日期',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'transferDate',
        flex: 1,
        renderer: function(value, metaData) {
            var displayValue = value;
            if (value && typeof value === 'string') {
                displayValue = value.replace('T', ' ');
            }
            if (!displayValue) displayValue = '';
            metaData.tdAttr = 'title="' + displayValue + '" data-qtip="' + displayValue + '"';
            return displayValue;
        }
    },
        {
        text: '新借用人',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'toUsername',
        flex: 1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    }, {
        text: '新借用人同意日期',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'approvalDate',
        flex: 1,
        renderer: function(value, metaData) {
            var displayValue = value;
            if (value && typeof value === 'string') {
                displayValue = value.replace('T', ' ');
            }
            if (!displayValue) displayValue = '';
            metaData.tdAttr = 'title="' + displayValue + '" data-qtip="' + displayValue + '"';
            return displayValue;
        }
    }, {
        text: '批准管理员',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'adminApprovalUsername',
        flex: 1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    }, {
        text: '批准日期',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'adminApprovalDate',
        flex: 1,
        renderer: function(value, metaData) {
            var displayValue = value;
            if (value && typeof value === 'string') {
                displayValue = value.replace('T', ' ');
            }
            if (!displayValue) displayValue = '';
            metaData.tdAttr = 'title="' + displayValue + '" data-qtip="' + displayValue + '"';
            return displayValue;
        }
    }, {
        text: '状态',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'statusText',
        flex: 1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    }, {
        text: '详情',
        align: 'center',
        style: 'font-size:16px',
        dataIndex: 'detail',
        flex: 1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    }
    ],
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'DeviceTransferRecordStore',
        displayInfo: true,
        displayMsg: '显示第 {0} - {1} 条，共 {2} 条',
        emptyMsg: '没有数据'
    }
});
