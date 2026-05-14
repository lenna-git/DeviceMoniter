Ext.define('AM.view.user.userwindow', {
    extend: 'Ext.window.Window',
    alias: 'widget.userwindow',
    border: false,
    modal: true,
    autoDestroy: true,
    width: 420,
    height: 320,
    title: '新增用户',
    resizable: false,
    layout: 'fit',
    bodyPadding: 15,

    items: [
        {
            xtype: 'form',
            layout: 'anchor',
            defaults: {
                anchor: '100%',
                labelWidth: 70,
                margin: '0 0 15 0'
            },
            items: [
                {
                    xtype: 'textfield',
                    name: 'id',
                    hidden: true,
                    fieldLabel: 'id'
                },
                {
                    xtype: 'textfield',
                    name: 'sysusername',
                    fieldLabel: '用户名',
                    allowBlank: false,
                    emptyText: '请输入用户名',
                    msgTarget: 'side'
                },
                {
                    xtype: 'textfield',
                    name: 'sysuserpassword',
                    fieldLabel: '密码',
                    inputType: 'password',
                    allowBlank: false,
                    emptyText: '请输入密码',
                    msgTarget: 'side'
                },
                {
                    xtype: 'combo',
                    name: 'sysuserrole',
                    fieldLabel: '角色',
                    store: Ext.create('Ext.data.Store', {
                        fields: ['value', 'text'],
                        data: [
                            {"value": 1, "text": "管理员"},
                            {"value": 2, "text": "普通用户"}
                        ]
                    }),
                    queryMode: 'local',
                    displayField: 'text',
                    valueField: 'value',
                    allowBlank: false,
                    value: 2,
                    msgTarget: 'side'
                }
            ],
            buttons: [
                {
                    xtype: 'button',
                    text: '确定',
                    action: 'save',
                    width: 80,
                    padding: '5 20',
                    style: 'margin-right: 10px;'
                },
                {
                    xtype: 'button',
                    text: '取消',
                    action: 'cancel',
                    width: 80,
                    padding: '5 20'
                }
            ],
            buttonAlign: 'right'
        }
    ]
});