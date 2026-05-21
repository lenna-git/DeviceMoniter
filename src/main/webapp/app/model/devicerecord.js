Ext.define('AM.model.devicerecord',{
    extend:'Ext.data.Model',
    fields:[
        {name:'id',type:'int'},
        {name:'borrorDate',type:'string'},
        {name:'detail',type:'string'},
        {name:'returnDate',type:'string'},
        {name:'returnApprovalDate',type:'string'},
        {name:'borrowUser',type:'auto'},
        {name:'sysUser',type:'auto'},
        {name:'returnApprovalUser',type:'auto'},
        {name:'device',type:'auto'},
        {name:'borrowerUsername',type:'string', convert:function(v, record){
            var borrowUser = record.get('borrowUser');
            return borrowUser ? borrowUser.sysusername : '';
        }},
        {name:'approvalUsername',type:'string', convert:function(v, record){
            var sysUser = record.get('sysUser');
            return sysUser ? sysUser.sysusername : '';
        }},
        {name:'returnApprovalUsername',type:'string', convert:function(v, record){
            var returnApprovalUser = record.get('returnApprovalUser');
            return returnApprovalUser ? returnApprovalUser.sysusername : '';
        }},
        {name:'deviceno',type:'string', convert:function(v, record){
            var device = record.get('device');
            return device ? device.deviceno : '';
        }},
        {name:'devicexh',type:'string', convert:function(v, record){
            var device = record.get('device');
            return device ? device.devicexh : '';
        }},
        {name:'devicesn',type:'string', convert:function(v, record){
            var device = record.get('device');
            return device ? device.devicesn : '';
        }},
        {name:'cpuname',type:'string', convert:function(v, record){
            var device = record.get('device');
            return device && device.devCpu ? device.devCpu.cpuname : '';
        }},
        {name:'typename',type:'string', convert:function(v, record){
            var device = record.get('device');
            return device && device.devType ? device.devType.typename : '';
        }},
        {name:'manufacturername',type:'string', convert:function(v, record){
            var device = record.get('device');
            return device && device.devManufacturer ? device.devManufacturer.manufacturername : '';
        }}
    ],
})
