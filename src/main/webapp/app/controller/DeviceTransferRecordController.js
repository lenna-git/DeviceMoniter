Ext.define('AM.controller.DeviceTransferRecordController', {
    extend: 'Ext.app.Controller',
    refs: [{
        ref: 'grid',
        selector: 'viewport centerpage DeviceTransferRecordView DeviceTransferRecordGrid'
    }, {
        ref: 'searchField',
        selector: 'viewport centerpage DeviceTransferRecordView textfield[name=searchKeyword]'
    }],
    init: function() {
        this.control({
            'viewport centerpage DeviceTransferRecordView DeviceTransferRecordGrid': {
                render: this.onGridRender,
            },
            'viewport centerpage DeviceTransferRecordView button[action=select]': {
                click: this.onSearch
            },
            'viewport centerpage DeviceTransferRecordView button[action=export]': {
                click: this.onExport
            },
        });
    },
    models: ['DeviceTransferRecordModel'],
    stores: ['DeviceTransferRecordStore'],
    onGridRender: function(grid) {
        var store = this.getGrid().getStore();
        if (store) {
            store.load();
        }
    },
    onSearch: function() {
        var keyword = this.getSearchField().getValue();
        var store = this.getGrid().getStore();
        if (store) {
            store.getProxy().extraParams.keyword = keyword;
            store.reload();
        }
    },
    onExport: function() {
        var keyword = this.getSearchField().getValue();
        var url = 'transfer/exportExcel';
        if (keyword && keyword.trim()) {
            url += '?keyword=' + encodeURIComponent(keyword.trim());
        }
        window.location.href = url;
    },
});
