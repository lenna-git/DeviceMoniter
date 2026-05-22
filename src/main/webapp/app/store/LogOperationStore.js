Ext.define('AM.store.LogOperationStore', {
    extend: 'Ext.data.Store',
    model: 'AM.model.LogOperationModel',
    pageSize: 20,
    remoteSort: false,
    remoteFilter: false,
    proxy: {
        type: 'ajax',
        url: '/logoperation/search',
        // reader: {
        //     type: 'json',
        //     rootProperty: 'data',
        //     totalProperty: 'total'
        // },
        // actionMethods: {
        //     read: 'GET'
        // },
        pageParam: 'page',
        limitParam: 'limit',
        startParam: undefined,
        reader:{
            type:'json',
            root:'data',
            totalProperty:'total'
        },
        extraParams: {
            operatorName: '',
            operationType: '',
            operationModule: '',
            operationResult: '',
            startTime: '',
            endTime: ''
        },
        listeners: {
            exception: function(proxy, response, operation) {
                console.log('Proxy 异常:', response);
            }
        }
    },
    listeners: {
        beforeload: function(store, operation) {
            console.log('Before load - params:', operation.params);
        }
    },
    autoLoad: false,
    sorters: [{
        property: 'operationTime',
        direction: 'DESC'
    }]
});