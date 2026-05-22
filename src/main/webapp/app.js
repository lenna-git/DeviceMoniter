
var SYS_USER = null;
SYS_USER=  Ext.decode(sessionStorage.getItem('SYS_USER'));

// WebSocket 连接用于设备状态实时更新
var deviceStatusWebSocket = null;
var deviceStatusCallbacks = [];

function connectDeviceStatusWebSocket() {
    var protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    var wsUrl = protocol + '//' + window.location.host + '/ws/device-status';
    
    console.log('尝试连接WebSocket:', wsUrl);
    
    deviceStatusWebSocket = new WebSocket(wsUrl);
    
    deviceStatusWebSocket.onopen = function(event) {
        console.log('设备状态WebSocket连接已建立');
        console.log('当前连接状态:', deviceStatusWebSocket.readyState);
    };
    
    deviceStatusWebSocket.onmessage = function(event) {
        console.log('收到设备状态更新消息:', event.data);
        try {
            var message = JSON.parse(event.data);
            console.log('解析后的消息:', message);
            if (message.type === 'DEVICE_STATUS_UPDATE') {
                console.log('设备状态更新，设备ID:', message.deviceId);
                console.log('注册的回调函数数量:', deviceStatusCallbacks.length);
                // 通知所有注册的回调函数
                deviceStatusCallbacks.forEach(function(callback) {
                    console.log('调用回调函数');
                    callback(message.deviceId);
                });
            }
        } catch (e) {
            console.error('解析WebSocket消息失败:', e);
        }
    };
    
    deviceStatusWebSocket.onerror = function(error) {
        console.error('WebSocket错误:', error);
        console.error('错误详情:', error.message);
    };
    
    deviceStatusWebSocket.onclose = function(event) {
        console.log('设备状态WebSocket连接已关闭，代码:', event.code, '原因:', event.reason);
        console.log('正在尝试重新连接...');
        // 5秒后重新连接
        setTimeout(connectDeviceStatusWebSocket, 5000);
    };
}

// 注册设备状态更新回调
function registerDeviceStatusCallback(callback) {
    if (deviceStatusCallbacks.indexOf(callback) === -1) {
        deviceStatusCallbacks.push(callback);
    }
}

// 注销设备状态更新回调
function unregisterDeviceStatusCallback(callback) {
    var index = deviceStatusCallbacks.indexOf(callback);
    if (index !== -1) {
        deviceStatusCallbacks.splice(index, 1);
    }
}

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
        // 启动WebSocket连接
        connectDeviceStatusWebSocket();
    }
});
