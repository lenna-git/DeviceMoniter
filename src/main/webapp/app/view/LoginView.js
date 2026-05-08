Ext.define('AM.view.LoginView', {
    extend: 'Ext.form.Panel',
    alias : 'widget.loginView',
    height: 180,
    width: 350,
    id:'LoginView',
    layout: {
        type: 'absolute'
    },
    bodyPadding: 10,
    title: '欢迎登录315设备管理系统',
    initComponent: function() {
        Ext.applyIf(this, {
            style: {
                marginRight: 'auto',
                marginLeft: 'auto',
                marginTop: '200px',
                marginBottom: 'auto'
            },
            items: [
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    x: 50,
                    y: 10,
                    maxWidth: 200,
                    fieldLabel: '账号',
                    labelAlign: 'left',
                    labelWidth: 40,
                    name: 'sysusername'
                },
                {
                    xtype:'tbspacer',
                    height:50,
                },
                {
                    xtype: 'textfield',
                    anchor: '100%',
                    x: 50,
                    y: 60,
                    maxWidth: 200,
                    fieldLabel: '密码',
                    labelAlign: 'left',
                    labelWidth: 40,
                    name: 'sysuserpassword',
                    inputType: 'password'
                },
                {
                    xtype: 'button',
                    handler: function(button, event) {
                        console.log('login---------------');
                        Ext.getCmp('LoginView').getForm().submit({
                            url:'/sysuseraction/login',
                            method: 'get',
                            waitMsg: "正在登录......",

                            success: function(form, action) {
                                var loginResult = action.result;
                                if (loginResult === true) {
                                    // window.location.href="index.html";
                                    Ext.create('AM.view.Viewport');
                                }
                            },
                            failure: function(form, action) {
                                switch (action.failureType) {
                                    case Ext.form.Action.CLIENT_INVALID:
                                        Ext.Msg.alert("登录失败", "提交的表单数据无效,请检查!");
                                        break;
                                    case Ext.form.Action.CONNECT_FAILURE:
                                        Ext.Msg.alert("登录失败", "连接失败！");
                                        break;
                                    case Ext.form.Action.SERVER_INVALID:
                                        Ext.Msg.alert("登录失败","账号或密码错误！");
                                        break;
                                }
                            }
                        });
                    },
                    x: 150,
                    y: 120,
                    id: 'loginButton',
                    width: 60,
                    text: '登录'
                },
                {
                    xtype: 'button',
                    handler: function(button, event) {
                        Ext.getCmp('LoginView').form.reset();
                    },
                    x: 220,
                    y: 120,
                    id: 'resetButton',
                    width: 60,
                    text: '重置'
                }
            ]
        });
        this.callParent(arguments);
    }
});