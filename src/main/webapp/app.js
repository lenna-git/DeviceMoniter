
var SYS_USER = null;
SYS_USER=  Ext.decode(sessionStorage.getItem('SYS_USER'));
Ext.application({
    name: 'AM',

    appFolder: 'app',
    autoCreateViewport:true,

    controllers: [
        'Users','LoginController','mainpageltbarController1',
        'Devices','DeviceRecordController',
        'DeviceTransferRecordController',
        'Devicewindow','UserwindowController',
        'ChangePasswordController'
    ],

    stores: [
        'DeviceTransferRecordStore',
        'devicerecordstore'
    ],

    models: [
        'DeviceTransferRecordModel',
        'devicerecord'
    ],

    launch: function() {
        console.log('App launched');
    }
});
