

Ext.define('AM.store.deviceliststore',{
    extend:'Ext.data.Store',
    model:'AM.model.devicelist',
    autoLoad:true,

    proxy:{
        type:'ajax',
        api:{
            read:'deviceaction/alldevices',
            create:'deviceaction/createdevice'
            // read:'useraction/allusers',//store获取数据，向后台发送下面的请求
            // create:'useraction/createuser',

        }
    },

})