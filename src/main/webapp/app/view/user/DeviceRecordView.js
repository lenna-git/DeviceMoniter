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
    columns:[
    {
        text:'序号',
        align:'center',
        style:'font-size:16px',
        width:60,
        renderer: function(value, metaData, record, rowIndex) {
            return rowIndex + 1;
        }
    },{
        text:'ID',
        align:'center',
        style:'font-size:16px',
        dataIndex:'id',
        flex:1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'设备编号',
        align:'center',
        style:'font-size:16px',
        dataIndex:'deviceno',
        flex:2,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'序列号',
        align:'center',
        style:'font-size:16px',
        dataIndex:'devicesn',
        flex:2,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'芯片',
        align:'center',
        style:'font-size:16px',
        dataIndex:'cpuname',
        flex:2,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'型号',
        align:'center',
        style:'font-size:16px',
        dataIndex:'devicexh',
        flex:2,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'类型',
        align:'center',
        style:'font-size:16px',
        dataIndex:'typename',
        flex:1,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'厂商',
        align:'center',
        style:'font-size:16px',
        dataIndex:'manufacturername',
        flex:2,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'借用人',
        align:'center',
        style:'font-size:16px',
        dataIndex:'borrowerUsername',
        flex:2,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'借用日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'borrorDate',
        flex:2,
        renderer: function(value, metaData) {
            var displayValue = value;
            if (value && typeof value === 'string') {
                displayValue = value.replace('T', ' ');
            }
            if (!displayValue) displayValue = '';
            metaData.tdAttr = 'title="' + displayValue + '" data-qtip="' + displayValue + '"';
            return displayValue;
        }
    },{
        text:'批准人',
        align:'center',
        style:'font-size:16px',
        dataIndex:'approvalUsername',
        flex:2,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'批准借用日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'approvalDate',
        flex:2,
        renderer: function(value, metaData) {
            var displayValue = value;
            if (value && typeof value === 'string') {
                displayValue = value.replace('T', ' ');
            }
            if (!displayValue) displayValue = '';
            metaData.tdAttr = 'title="' + displayValue + '" data-qtip="' + displayValue + '"';
            return displayValue;
        }
    },{
        text:'归还日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'returnDate',
        flex:2,
        renderer: function(value, metaData) {
            var displayValue = value;
            if (value && typeof value === 'string') {
                displayValue = value.replace('T', ' ');
            }
            if (!displayValue) displayValue = '';
            metaData.tdAttr = 'title="' + displayValue + '" data-qtip="' + displayValue + '"';
            return displayValue;
        }
    },{
        text:'批准归还人',
        align:'center',
        style:'font-size:16px',
        dataIndex:'returnApprovalUsername',
        flex:2,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    },{
        text:'批准归还日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'returnApprovalDate',
        flex:2,
        renderer: function(value, metaData) {
            var displayValue = value;
            if (value && typeof value === 'string') {
                displayValue = value.replace('T', ' ');
            }
            if (!displayValue) displayValue = '';
            metaData.tdAttr = 'title="' + displayValue + '" data-qtip="' + displayValue + '"';
            return displayValue;
        }
    },{
        text:'详情',
        align:'center',
        style:'font-size:16px',
        dataIndex:'detail',
        flex:2,
        renderer: function(value, metaData) {
            metaData.tdAttr = 'data-qtip="' + (value ? Ext.String.htmlEncode(value) : '') + '"';
            return value;
        }
    }],
    bbar: {
        xtype: 'pagingtoolbar',
        store: 'devicerecordstore',
        displayInfo: true,
        displayMsg: '显示第 {0} - {1} 条，共 {2} 条',
        emptyMsg: '没有数据'
    }

})
