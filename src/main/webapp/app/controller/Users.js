Ext.define('AM.controller.Users', {
    extend: 'Ext.app.Controller',

    init: function() {
        this.control({
            'viewport > panel': {
                render: this.onPanelRendered
            },
            'viewport > panel > centerpage > userlist1 > testlistgrid':{
                cellclick:this.onsysusergridcellclick,
            },
            'viewport > panel > centerpage > userlist1 toolbar button[action=xj]': {
                click: this.onxjbuttioncick
            },
            'viewport > panel > centerpage > userlist1 toolbar button[action=sc]': {
                click: this.onscbuttioncick
            },
            'viewport > panel > centerpage > userlist1 toolbar button[action=update]': {
                click: this.onupdatebuttioncick
            },
            'viewport > panel > centerpage > userlist1 toolbar button[action=resetPassword]': {
                click: this.onResetPasswordClick
            },
        });
    },
    models:['userlist'],
    stores:['userliststore'],
    refs:[{
        selector: 'viewport > panel > centerpage > userlist1 > testlistgrid',
        ref:'testgrid'
    }],


    onPanelRendered: function() {
        // console.log('The userlistpanel was rendered');
    },
    onResetPasswordClick: function() {
        var grid = this.getTestgrid();
        var selection = grid.getSelectionModel().getSelection();

        if (selection.length === 0) {
            Ext.Msg.alert('提示', '请先选择要重置密码的用户');
            return;
        }

        var record = selection[0];
        var userId = record.get('id');
        var username = record.get('sysusername');

        if (SYS_USER && SYS_USER.id === userId) {
            Ext.Msg.alert('提示', '不能重置当前登录用户的密码');
            return;
        }

        Ext.Msg.confirm('确认重置密码', '确定要将用户 "' + username + '" 的密码重置为默认密码吗？', function(btn) {
            if (btn === 'yes') {
                Ext.Ajax.request({
                    url: 'sysuseraction/adminResetPassword',
                    method: 'POST',
                    params: {
                        userId: userId
                    },
                    success: function(response, opts) {
                        var result = Ext.decode(response.responseText);
                        if (result.success) {
                            Ext.Msg.alert('提示', result.message);
                        } else {
                            Ext.Msg.alert('提示', result.message);
                        }
                    },
                    failure: function(response, opts) {
                        Ext.Msg.alert('提示', '重置密码失败');
                    }
                });
            }
        });
    },
    onxjbuttioncick:function (){
        console.log('userlistpanel onxjbuttioncick ');
        var userwindow = Ext.widget({
            xtype: 'userwindow'
        });
        console.log('userwindow created:', userwindow);
        userwindow.show();
    },
    onscbuttioncick: function() {
        console.log('userlistpanel onscbuttioncick');
        var grid = this.getTestgrid();
        var selection = grid.getSelectionModel().getSelection();

        if (selection.length === 0) {
            Ext.Msg.alert('提示', '请先选择要删除的用户');
            return;
        }

        var record = selection[0];
        var userId = record.get('id');

        if (SYS_USER && SYS_USER.id === userId) {
            Ext.Msg.alert('提示', '不能删除当前登录用户');
            return;
        }

        Ext.Msg.confirm('确认删除', '确定要删除该用户吗？', function(btn) {
            if (btn === 'yes') {
                Ext.Ajax.request({
                    url: 'sysuseraction/deleteuser',
                    method: 'delete',
                    params: {
                        id: userId
                    },
                    success: function(response, opts) {
                        var result = Ext.decode(response.responseText);
                        if (result.success) {
                            Ext.Msg.alert('提示', result.message);
                            grid.getStore().reload();
                        } else {
                            Ext.Msg.alert('提示', result.message);
                        }
                    },
                    failure: function(response, opts) {
                        Ext.Msg.alert('提示', '删除失败');
                    }
                });
            }
        });
    },
    onupdatebuttioncick: function() {
        console.log('userlistpanel onupdatebuttioncick');
        var grid = this.getTestgrid();
        var selection = grid.getSelectionModel().getSelection();

        if (selection.length === 0) {
            Ext.Msg.alert('提示', '请先选择要修改的用户');
            return;
        }

        var record = selection[0];
        var userId = record.get('id');

        if (SYS_USER && SYS_USER.id === userId) {
            Ext.Msg.alert('提示', '不能修改当前登录用户');
            return;
        }

        var userwindow = Ext.widget({
            xtype: 'userwindow',
            title: '修改用户'
        });

        var form = userwindow.down('form') || userwindow;
        form.down('textfield[name=id]').setValue(record.get('id'));
        form.down('textfield[name=sysusername]').setValue(record.get('sysusername'));
        form.down('textfield[name=sysuserpassword]').setValue(record.get('sysuserpassword'));
        form.down('combo[name=sysuserrole]').setValue(record.get('sysuserrole'));

        userwindow.show();
    }

});