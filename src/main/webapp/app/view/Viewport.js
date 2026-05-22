Ext.define('AM.view.Viewport',{
    extend:'Ext.container.Viewport',

    requires:[
        'AM.view.user.userlist',
        'AM.view.device.devicelist',
        'AM.view.device.devicexzwindow',
        'AM.view.Center',
        'AM.view.mainpageltbar1',
        'AM.view.user.DeviceRecordView',
        'AM.view.user.DeviceTransferRecordView',
        'AM.view.user.userwindow',
        'AM.view.user.ChangePasswordView',
        'AM.view.user.LogOperationView',
    ],
    layout:'vbox',
    items:[
        {
            // region: 'north',
            xtype: 'toolbar',   //上面的工具栏
            width:'100%',
            height: 40,
            // flex:1,
            items: [
                // 这里可以添加左侧的其他元素
                '->', // 使用 '->' 将后续元素推到右侧
                {
                    xtype: 'label',
                    text: '当前用户: ' + (SYS_USER ? SYS_USER.sysusername : '暂未获取用户名'),
                },
                '-',  // 竖线
                {
                    xtype: 'button',
                    text: '退出登录',
                    action:'logout',
                }
            ]
        },
        {
            xtype: 'panel',
            width:'100%',
            flex:1,          //大小比例
            layout:'hbox',   //水平结构
            items: [
                {
                    xtype:'mainpageltbar1',
                    width: 160,
                    height: '100%',
                    border:true,
                },
                {
                    xtype:'centerpage',
                    // xtype:'panel',
                    flex:1,
                    height: '100%',
                    border:true,
                },
            ]
        }
    ]
})