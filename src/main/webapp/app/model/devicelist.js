

Ext.define('AM.model.devicelist',{
    extend:'Ext.data.Model',
    fields:[
        {name:'devCpu.cpuname',type:'String'},//芯片
        {name:'devType.typename',type:'String'},//类型
        {name:'devicexh',type:'String'},//型号
        {name:'devManufacturer.manufacturername',type:'String'},//厂商
        {name:'devicesn',type:'String'},//序列号
        {name:'deviceno',type:'String'},//编号
        {name:'devicescdata',type:'String'},//送测日期
        {name:'deviceajdata',type:'String'},//安检日期
        {name:'deviceghdata',type:'String'},//归还厂商日期
        {name:'deviceyh',type:'String'},//借用人
        {name:'devicestate.stateDetail',type:'String'},//状态

        {name:'devCpu', type: 'auto'},
        {name:'devType', type: 'auto'},
        {name:'devManufacturer', type: 'auto'},
        {name:'devicestate', type: 'auto'},//设备状态关联对象

    ]
})