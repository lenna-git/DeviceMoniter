Ext.define('AM.store.DeviceTransferRecordStore', {
    extend: 'Ext.data.Store',
    model: 'AM.model.DeviceTransferRecordModel',
//     storeId: 'DeviceTransferRecordStore',
//     autoLoad: true,
//     pageSize: 20,
//     proxy: {
//         type: 'ajax',
//         url: 'transfer/list',
//         reader: {
//             type: 'json',
//             rootProperty: 'data',
//             totalProperty: 'total'
//         },
//         extraParams: {
//             keyword: '',
//         }
//     }
// });

    autoLoad:true,
    pageSize:20,
    remoteSort: false,
    remoteFilter: false,

    proxy:{
        type:'ajax',
        url:'transfer/list',
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
            console.log('DeviceTransferRecord loaded:', success, records.length);
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