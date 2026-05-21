Ext.define('AM.store.devicerecordstore',{
    extend:'Ext.data.Store',
    model:'AM.model.devicerecord',
    autoLoad:true,
    pageSize:20,
    remoteSort: false,
    remoteFilter: false,

    proxy:{
        type:'ajax',
        url:'devicerecord/alldevicerecords',
        pageParam: 'page',
        limitParam: 'limit',
        startParam: undefined,
        reader:{
            type:'json',
            root:'data',
            totalProperty:'total'
        },
        simpleSortMode: true,
        extraParams:{
            keyword: '',
        }
    },
    listeners: {
        load: function(store, records, success, operation) {
            console.log('devicerecordstore loaded:', success, records.length);
            console.log('Records:', records);
            if (!success) {
                console.log('Load failed:', operation.getError());
            }
        },
        beforeload: function(store, operation) {
            console.log('Before load:', operation);
        },
        exception: function(proxy, response, operation) {
            console.log('Store exception:', response.status, response.responseText);
        }
    }
})
