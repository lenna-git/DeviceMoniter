Ext.define('AM.view.user.userwindow', {
        extend: 'Ext.window.Window',
        alias: 'widget.userwindow',
        border: true,
        modal:true,
        autoDestroy: true,
        width: 400,
        height: 350,
        title: '新增用户',
    layout:{
        type:'vbox'
    },

    items: [
        {
            xtype: 'textfield',
            name: 'id',
            hidden:true,
            fieldLabel: 'id'
        },{
            xtype: 'textfield',
            name: 'sysusername',
            fieldLabel: '用户名',
            allowBlank: false
        },{
            xtype: 'textfield',
            name: 'sysuserpassword',
            fieldLabel: '密码',
            inputType: 'password',
            allowBlank: false
        },{
            xtype: 'combo',
            name: 'sysuserrole',
            fieldLabel: '角色',
            store: Ext.create('Ext.data.Store', {
                fields: ['value', 'text'],
                data : [
                    {"value": 1, "text": "管理员"},
                    {"value": 2, "text": "普通用户"}
                ]
            }),
            queryMode: 'local',
            displayField: 'text',
            valueField: 'value',
            allowBlank: false,
            value: 2
        },
        {
            xtype: 'panel',
            layout:{
                type:'hbox',
                pack:'end'
            },
            items: [
                {
                    xtype: 'button',
                    text: '确定',
                    action: 'save',

                },
                {
                    xtype: 'button',
                    text: '取消',
                    action: 'cancel',
                }
            ]
        }
    ]
});

