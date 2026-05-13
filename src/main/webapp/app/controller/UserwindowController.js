Ext.define('AM.controller.UserwindowController', {
    extend: 'Ext.app.Controller',

    init: function() {
        console.log('UserwindowController initialized');
        this.control({
            'userwindow button[action=cancel]': {
                click: this.onCancel
            },
            'userwindow button[action=save]': {
                click: this.onSave
            }
        });
    },

    onCancel: function(button) {
        console.log('cancel');
        var win = button.up('window');
        if (win) {
            win.close();
        }
    },

    onSave: function(button) {
        console.log('save button clicked');
        var win = button.up('window');
        
        var sysusernameField = win.down('textfield[name=sysusername]');
        var sysuserpasswordField = win.down('textfield[name=sysuserpassword]');
        var sysuserroleField = win.down('combo[name=sysuserrole]');

        if (!sysusernameField || !sysuserpasswordField || !sysuserroleField) {
            Ext.Msg.alert('错误', '无法找到表单字段');
            return;
        }

        var sysusernameVal = sysusernameField.getValue();
        var sysuserpasswordVal = sysuserpasswordField.getValue();
        var sysuserroleVal = sysuserroleField.getValue();

        if (!sysusernameVal || !sysuserpasswordVal) {
            Ext.Msg.alert('提示', '用户名和密码不能为空');
            return;
        }

        var rec = {
            sysusername: sysusernameVal,
            sysuserpassword: sysuserpasswordVal,
            sysuserrole: sysuserroleVal
        };

        Ext.Ajax.request({
            url: 'sysuseraction/createuser',
            method: 'post',
            jsonData: rec,
            headers: {
                'Content-Type': 'application/json'
            },
            success: function(response, opts) {
                Ext.Msg.alert('提示', '保存成功');
            },
            failure: function(response, opts) {
                Ext.Msg.alert('提示', '保存失败');
            }
        });

        win.close();

        var grid = Ext.ComponentQuery.query('viewport centerpage userlist1 testlistgrid')[0];
        if (grid) {
            grid.getStore().reload();
        }
    }

});