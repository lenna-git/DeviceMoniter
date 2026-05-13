
var SYS_USER = null;
SYS_USER=  Ext.decode(sessionStorage.getItem('SYS_USER'));
Ext.application({
    name: 'AM',

    appFolder: 'app',
    autoCreateViewport:true,

    controllers: [


        'Users','LoginController','mainpageltbarController',
        'mainpageltbarController1','Devices','DeviceRecordController',
        'xinjianyonghujilucontroller','Devicewindow','UserwindowController'


    ],

    launch: function() {
        // console.log('launch app');
    }
});
