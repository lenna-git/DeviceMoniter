Ext.define('AM.store.DeviceTransferRecordStore', {
    extend: 'Ext.data.Store',
    model: 'AM.model.DeviceTransferRecordModel',
    storeId: 'DeviceTransferRecordStore',
    autoLoad: true,
    pageSize: 20,
    proxy: {
        type: 'ajax',
        url: 'transfer/list',
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty: 'total'
        },
        extraParams: {
            keyword: '',
        }
    }
});
