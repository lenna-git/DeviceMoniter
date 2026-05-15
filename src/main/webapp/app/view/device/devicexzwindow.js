Ext.define('AM.view.device.devicexzwindow',{
    extend: 'Ext.window.Window',
    alias:'widget.devicexzwindow',
    border: true,
    width:600,
    height:700,
    layout:{
        type:'vbox',
        align:'left'
    },
    title: '设备新增',
    
    listeners: {
        afterrender: function(win) {
            var cpuCombo = win.down('combo[name=devcpu_id]');
            var typeCombo = win.down('combo[name=devtype_id]');
            var manufacturerCombo = win.down('combo[name=devmanufacturer_id]');
            
            var setDefaultValue = function(combo) {
                var store = combo.getStore();
                if (store.getCount() > 0) {
                    combo.setValue(store.first().getId());
                } else {
                    store.on('load', function() {
                        if (store.getCount() > 0) {
                            combo.setValue(store.first().getId());
                        }
                    }, null, { single: true });
                }
            };
            
            setDefaultValue(cpuCombo);
            setDefaultValue(typeCombo);
            setDefaultValue(manufacturerCombo);
        }
    },

    items:[{
        xtype: 'textfield',
        fieldLabel:'设备id',
        hidden:true,
        name:'deviceid',
    },{
        xtype: 'combo',
        fieldLabel:'芯片',
        name:'devcpu_id',
        allowBlank:false,
        width:300,
        labelWidth: 80,
        margin: '10 0 10 60',
        store: Ext.create('AM.store.devcpustore'),
        displayField: 'cpuname',
        valueField: 'id',
        queryMode: 'local',
        emptyText: '请选择芯片型号'
    },{
        xtype: 'combo',
        fieldLabel:'类型',
        name:'devtype_id',
        allowBlank:false,
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
        store: Ext.create('AM.store.devtypestore'),
        displayField: 'typename',
        valueField: 'id',
        queryMode: 'local',
        emptyText: '请选择设备类型'
    },{
        xtype: 'textfield',
        fieldLabel:'型号',
        name:'devicexh',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'combo',
        fieldLabel:'厂商',
        name:'devmanufacturer_id',
        allowBlank:false,
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
        store: Ext.create('AM.store.devmanufacturerstore'),
        displayField: 'manufacturername',
        valueField: 'id',
        queryMode: 'local',
        emptyText: '请选择厂商'
    },{
        xtype: 'textfield',
        fieldLabel:'序列号',
        name:'devicesn',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'textfield',
        fieldLabel:'编号',
        name:'deviceno',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'textfield',
        fieldLabel:'送测日期',
        name:'devicescdata',
        allowBlank:true,
        width:400,
        labelWidth: 80,
        margin: '0 0 10 60',
        emptyText: '请输入日期时间 (YYYY-MM-DD HH:MM:SS)',
        regex: /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/,
        regexText: '日期时间格式不正确，请输入 YYYY-MM-DD HH:MM:SS'
    },{
        xtype: 'textfield',
        fieldLabel:'安检日期',
        name:'deviceajdata',
        allowBlank:true,
        width:400,
        labelWidth: 80,
        margin: '0 0 10 60',
        emptyText: '请输入日期时间 (YYYY-MM-DD HH:MM:SS)',
        regex: /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/,
        regexText: '日期时间格式不正确，请输入 YYYY-MM-DD HH:MM:SS'
    },{
        xtype: 'textfield',
        fieldLabel:'归还厂商日期',
        name:'deviceghdata',
        allowBlank:true,
        width:400,
        labelWidth: 80,
        margin: '0 0 10 60',
        emptyText: '请输入日期时间 (YYYY-MM-DD HH:MM:SS)',
        regex: /^\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}$/,
        regexText: '日期时间格式不正确，请输入 YYYY-MM-DD HH:MM:SS'
    },{
        xtype: 'textfield',
        fieldLabel:'借用人',
        name:'deviceyh',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'textfield',
        fieldLabel:'状态',
        name:'devicestate',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'textfield',
        fieldLabel:'操作',
        name:'deviceop',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'panel',
        layout:'hbox',
        margin: '0 0 0 450',
        items: [{
            xtype: 'button',
            action:'device-save',
            text:'保存',
            height:'80',
            width:'150',
        },{
            xtype: 'button',
            action:'device-quxiao',
            text:'取消',
            height:'80',
            width:'150',
        }]
    }],


});
