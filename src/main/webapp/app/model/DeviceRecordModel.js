Ext.define('AM.model.DeviceRecordModel',{
    extend:'Ext.data.Model',
    fields:[
        {name:'userId',type:'Long'},
        {name:'deviceId',type:'Long'},
        {name:'detail',type:'String'},
        {name:'id',type:'Long'},
        {name:'borrorDate',type:'String'},
        {name:'returnDate',type:'String'},
        {name:'sysUser.id',type:'Long'},
        {name:'sysUser.sysusername',type:'String'},
        {name:'device.id',type:'Long'},
        {name:'device.devicexp',type:'String'},
        {name:'device.devicetype',type:'String'},
        {name:'device.devicexh',type:'String'},
        {name:'device.deviceno',type:'String'},
        {name:'device.devicecs',type:'String'},
    ],
    // hasOne: [{
    //     model: 'ZHBB.model.TeleBaseInfo',
    //     associationKey: 'teleBaseInfo',
    //     getterName: 'getTeleBaseInfo'
    // }, ]
})