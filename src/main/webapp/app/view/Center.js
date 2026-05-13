Ext.define('AM.view.Center',{
    extend:'Ext.container.Container',
    alias:'widget.centerpage',
    layout:{
        type:'card',
    },
    id:'centerpage',
    activeItem:0,
    renderTo: Ext.getBody(),

    requires:[
        'AM.view.user.userlist',
        'AM.view.device.devicelist',
        'AM.view.user.DeviceRecordView',
        'AM.view.user.xinjianyonghujiluwindow',
        // 'AM.view.user.userwindow'
        //右側有哪些頁面，都要放在這裏
    ],
    items:[
        {
            xtype:'devicelist',
            id:'main-device',
            title: 'card1:devices',
        },
        {

            xtype:'panel',
            id:'pagetwo',
            title: 'card2:',
        },
        {
            width:'100%',
            height:'100%',
            xtype:'userlist1',
            id:'main-user',
            title: '用户信息',
        },{

            xtype:'DeviceRecordView',
            id:'main-DeviceRecord',
            title: '借用记录:',
            // titleAlign: 'center'
        },{

            xtype:'panel',
            id:'pagefive',
            title: 'card5:',
        }
    ]
})