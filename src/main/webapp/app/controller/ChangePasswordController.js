Ext.define('AM.controller.ChangePasswordController', {
    extend: 'Ext.app.Controller',
    
    init: function() {
        this.control({
            'viewport centerpage ChangePasswordView button[action=save]': {
                click: this.onSaveClick
            },
            'viewport centerpage ChangePasswordView button[action=reset]': {
                click: this.onResetClick
            }
        });
    },
    
    refs: [{
        selector: 'viewport centerpage ChangePasswordView',
        ref: 'changePasswordView'
    }],
    
    onSaveClick: function() {
        // 检查用户是否已登录
        if (!SYS_USER || !SYS_USER.id) {
            Ext.Msg.alert('提示', '请先登录');
            return;
        }
        
        var view = this.getChangePasswordView();
        var form = view.down('form');
        
        if (!form.isValid()) {
            return;
        }
        
        var values = form.getValues();
        
        Ext.Ajax.request({
            url: 'sysuseraction/changePassword',
            method: 'POST',
            params: {
                userId: SYS_USER.id,
                oldPassword: values.oldPassword,
                newPassword: values.newPassword
            },
            success: function(response) {
                    var result = Ext.decode(response.responseText);
                    if (result.success) {
                        Ext.Msg.alert('成功', result.message + '，请重新登录', function() {
                            // 退出登录
                            Ext.Ajax.request({
                                url: 'sysuseraction/logout',
                                method: 'GET',
                                success: function() {
                                    sessionStorage.removeItem('SYS_USER');
                                    window.location.href = 'login.html';
                                }
                            });
                        });
                    } else {
                        Ext.Msg.alert('失败', result.message);
                    }
                },
            failure: function() {
                Ext.Msg.alert('错误', '修改密码失败，请稍后重试');
            }
        });
    },
    
    onResetClick: function() {
        var view = this.getChangePasswordView();
        var form = view.down('form');
        form.reset();
    }
});
