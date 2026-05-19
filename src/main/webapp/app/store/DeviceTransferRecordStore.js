Ext.define('AM.store.DeviceTransferRecordStore', {
    extend: 'Ext.data.Store',
    model: 'AM.model.DeviceTransferRecordModel',
    storeId: 'DeviceTransferRecordStore',
    autoLoad: true,
    proxy: {
        type: 'ajax',
        url: 'transfer/list',
        reader: {
            type: 'json',
            root: 'data'
        }
    }
});
