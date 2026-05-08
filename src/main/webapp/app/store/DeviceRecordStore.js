Ext.define('AM.store.DeviceRecordStore',{
    extend:'Ext.data.Store',
    model:'AM.model.DeviceRecordModel',
    autoLoad:true,
    proxy:{
        type:'ajax',
        api:{
            read:'devicerecord/alldevicerecords',
            // create:'useraction/createuser',
        },
        reader:{
            type:'json',
            rootProperty:'data'
        },
        extraParams:{
            userId: '',
            detail:'',
        }
    },

})