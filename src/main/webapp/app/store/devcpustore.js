Ext.define('AM.store.devcpustore',{
    extend:'Ext.data.Store',
    model:'AM.model.devcpu',
    autoLoad:true,

    proxy:{
        type:'ajax',
        url:'devcpuaction/allcpus',
        reader:{
            type:'json'
        }
    }
})