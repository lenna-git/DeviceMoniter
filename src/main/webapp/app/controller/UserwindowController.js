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

        var form = win.down('form');
        console.log('form:', form);

        var idField = form ? form.down('textfield[name=id]') : win.down('textfield[name=id]');
        var sysusernameField = form ? form.down('textfield[name=sysusername]') : win.down('textfield[name=sysusername]');
        var sysuserpasswordField = form ? form.down('textfield[name=sysuserpassword]') : win.down('textfield[name=sysuserpassword]');
        var sysuserroleField = form ? form.down('combo[name=sysuserrole]') : win.down('combo[name=sysuserrole]');

        console.log('idField:', idField);
        console.log('sysusernameField:', sysusernameField);
        console.log('sysuserpasswordField:', sysuserpasswordField);
        console.log('sysuserroleField:', sysuserroleField);

        if (!sysusernameField || !sysuserroleField) {
            Ext.Msg.alert('错误', '无法找到表单字段');
            return;
        }

        var idVal = idField ? idField.getValue() : null;
        var sysusernameVal = sysusernameField.getValue();
        var sysuserpasswordVal = sysuserpasswordField.getValue();
        var sysuserroleVal = sysuserroleField.getValue();

        console.log('idVal:', idVal);
        console.log('sysusernameVal:', sysusernameVal);
        console.log('sysuserpasswordVal:', sysuserpasswordVal);
        console.log('sysuserroleVal:', sysuserroleVal);

        var isUpdate = !!idVal;

        if (!sysusernameVal) {
            Ext.Msg.alert('提示', '用户名不能为空');
            return;
        }

        if (!isUpdate && !sysuserpasswordVal) {
            Ext.Msg.alert('提示', '密码不能为空');
            return;
        }

        var rec = {
            sysusername: sysusernameVal,
            sysuserrole: sysuserroleVal
        };

        if (!isUpdate) {
            rec.sysuserpassword = sysuserpasswordVal;
        }

        var url = 'sysuseraction/createuser';
        var method = 'post';

        if (isUpdate) {
            console.log('Updating user with id:', idVal);
            url = 'sysuseraction/updateuser';
            method = 'put';
            rec.id = idVal;
        }

        console.log('Sending request to:', url);
        console.log('Method:', method);
        console.log('Data:', rec);

        Ext.Ajax.request({
            url: url,
            method: method,
            jsonData: rec,
            headers: {
                'Content-Type': 'application/json'
            },
            success: function(response, opts) {
                console.log('Response received:', response.responseText);
                var result = Ext.decode(response.responseText);
                console.log('Result:', result);
                if (result.success) {
                    Ext.Msg.alert('提示', result.message);
                    win.close();
                    var grid = Ext.ComponentQuery.query('viewport centerpage userlist1 testlistgrid')[0];
                    if (grid) {
                        grid.getStore().reload();
                    }
                } else {
                    Ext.Msg.alert('提示', result.message);
                }
            },
            failure: function(response, opts) {
                console.log('Request failed:', response.status, response.statusText);
                Ext.Msg.alert('提示', '保存失败: ' + response.statusText);
            }
        });
    }

});