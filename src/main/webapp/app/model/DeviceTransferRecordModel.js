Ext.define('AM.model.DeviceTransferRecordModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id', type: 'int' },
        { name: 'transferDate', type: 'string' },
        { name: 'approvalDate', type: 'string' },
        { name: 'adminApprovalDate', type: 'string' },
        { name: 'status', type: 'int' },
        { name: 'detail', type: 'string' },
        { name: 'device', type: 'auto' },
        { name: 'fromUser', type: 'auto' },
        { name: 'toUser', type: 'auto' },
        { name: 'adminApprovalUser', type: 'auto' },
        
        { name: 'deviceNo', type: 'string', convert: function(v, record) {
            var device = record.get('device');
            return device ? device.deviceno : '';
        }},
        { name: 'deviceSn', type: 'string', convert: function(v, record) {
            var device = record.get('device');
            return device ? device.devicesn : '';
        }},
        { name: 'cpuName', type: 'string', convert: function(v, record) {
            var device = record.get('device');
            return device && device.devCpu ? device.devCpu.cpuname : '';
        }},
        { name: 'typeName', type: 'string', convert: function(v, record) {
            var device = record.get('device');
            return device && device.devType ? device.devType.typename : '';
        }},
        { name: 'deviceXh', type: 'string', convert: function(v, record) {
            var device = record.get('device');
            return device ? device.devicexh : '';
        }},
        { name: 'manufacturerName', type: 'string', convert: function(v, record) {
            var device = record.get('device');
            return device && device.devManufacturer ? device.devManufacturer.manufacturername : '';
        }},
        { name: 'fromUsername', type: 'string', convert: function(v, record) {
            var fromUser = record.get('fromUser');
            return fromUser ? fromUser.sysusername : '';
        }},
        { name: 'toUsername', type: 'string', convert: function(v, record) {
            var toUser = record.get('toUser');
            return toUser ? toUser.sysusername : '';
        }},
        { name: 'adminApprovalUsername', type: 'string', convert: function(v, record) {
            var admin = record.get('adminApprovalUser');
            return admin ? admin.sysusername : '';
        }},
        { name: 'statusText', type: 'string', convert: function(v, record) {
            var status = record.get('status');
            if (status == null) return '';
            switch (status) {
                case 1: return '申请中';
                case 2: return '新借用人已同意';
                case 3: return '管理员已同意';
                case 4: return '已拒绝';
                default: return '未知状态';
            }
        }}
    ],
});
