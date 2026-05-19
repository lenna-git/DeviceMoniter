Ext.define('AM.view.user.ChangePasswordView', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.ChangePasswordView',
    border: true,
    layout: 'vbox',
    items: [{
        xtype: 'form',
        width: 400,
        margin: '50 auto 0 auto',
        padding: '20',
        border: true,
        items: [{
            xtype: 'textfield',
            name: 'oldPassword',
            fieldLabel: '原密码',
            inputType: 'password',
            allowBlank: false,
            blankText: '请输入原密码',
            width: 350
        }, {
            xtype: 'textfield',
            name: 'newPassword',
            fieldLabel: '新密码',
            inputType: 'password',
            allowBlank: false,
            blankText: '请输入新密码',
            width: 350
        }, {
            xtype: 'textfield',
            name: 'confirmPassword',
            fieldLabel: '确认新密码',
            inputType: 'password',
            allowBlank: false,
            blankText: '请确认新密码',
            width: 350,
            validator: function(value) {
                var form = this.up('form');
                var newPassword = form.down('textfield[name=newPassword]').getValue();
                if (value !== newPassword) {
                    return '两次输入的密码不一致';
                }
                return true;
            }
        }, {
            xtype: 'container',
            layout: 'hbox',
            margin: '20 0 0 0',
            items: [{
                xtype: 'button',
                text: '保存',
                action: 'save',
                width: 100,
                margin: '0 10 0 0'
            }, {
                xtype: 'button',
                text: '重置',
                action: 'reset',
                width: 100
            }]
        }]
    }]
});
