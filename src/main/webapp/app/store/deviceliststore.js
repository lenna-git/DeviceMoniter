

Ext.define('AM.store.deviceliststore',{
    extend:'Ext.data.Store',
    model:'AM.model.devicelist',
    autoLoad:true,
    pageSize: 20,
    remoteSort: false,
    remoteFilter: false,

    proxy:{
        type:'ajax',
        url:'deviceaction/alldevices',
        pageParam: 'page',
        limitParam: 'limit',
        startParam: undefined,
        reader:{
            type:'json',
            root:'data',
            totalProperty:'total'
        },
        simpleSortMode: true
    }

})