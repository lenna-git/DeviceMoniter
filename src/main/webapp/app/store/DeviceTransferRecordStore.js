Ext.define('AM.store.DeviceTransferRecordStore', {
    extend: 'Ext.data.Store',
    model: 'AM.model.DeviceTransferRecordModel',

    autoLoad:true,
    pageSize: 20,
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
        beforeload: function(store, operation) {
            console.log('Before load:', operation);
            var me = store;
            Ext.Ajax.request({
                url: 'systemconfig/pageSize',
                method: 'GET',
                async: false,
                success: function(response) {
                    var result = Ext.decode(response.responseText);
                    if (result.success && result.pageSize) {
                        me.pageSize = result.pageSize;
                    }
                },
                failure: function() {
                    me.pageSize = 20;
                }
            });
        },
        load: function(store, records, success, operation) {
            console.log('DeviceTransferRecord loaded:', success, records.length);
            console.log('Records:', records);
            if (!success) {
                console.log('Load failed:', operation.getError());
            }
        },
        exception: function(proxy, response, operation) {
            console.log('Store exception:', response.status, response.responseText);
        }
    }
})