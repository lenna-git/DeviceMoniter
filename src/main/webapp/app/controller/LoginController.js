Ext.define('AM.controller.LoginController', {
    extend: 'Ext.app.Controller',
    // views: ['LoginView'],
    init: function() {
        this.control({
            'viewport toolbar button[action=logout]': {
                click: this.onLogoutbuttonclick,
            }
        });
    },


    onLogoutbuttonclick:function (){
        console.log('onLogoutbuttonclick');
        //删除id为120的记录
        Ext.Msg.confirm('确认退出', '您确定要退出登录吗？', function(btn) {
            if (btn === 'yes') {
                // 清除本地存储的用户信息
                sessionStorage.clear();
                Ext.Ajax.request({
                    url:'sysuseraction/logout',
                    method:'GET',
                    success:function(response,opts){
                        var obj = Ext.decode(response.responseText);
                        if(obj.success){
                            Ext.Msg.alert('结果显示',obj.message);
                            window.location.href = 'login.html';
                        }
                    },
                    failure:function(response,opts){
                        var obj = Ext.decode(response.responseText);
                        Ext.Msg.alert('保存错误','错误原因：'+obj.message);
                    }
                })
            }
        });
    },
});
