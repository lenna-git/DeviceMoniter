Ext.define('AM.view.device.devicexzwindow',{
    extend: 'Ext.window.Window',
    alias:'widget.devicexzwindow',
    border: true,
    width:600,
    height:700,
    layout:{
        type:'vbox'
    },
    title: '设备新增',

    items:[{
        xtype: 'textfield',
        fieldLabel:'设备id',
        hidden:true,
        name:'deviceid',
    },{
        xtype: 'textfield',
        fieldLabel:'芯片',
        name:'devicexp',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '10 0 10 60',
    },{
        xtype: 'textfield',
        fieldLabel:'类型',
        name:'devicetype',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'textfield',
        fieldLabel:'型号',
        name:'devicexh',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'textfield',
        fieldLabel:'厂商',
        name:'devicecs',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
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
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'textfield',
        fieldLabel:'安检日期',
        name:'deviceajdata',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'textfield',
        fieldLabel:'归还厂商日期',
        name:'deviceghdata',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
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
