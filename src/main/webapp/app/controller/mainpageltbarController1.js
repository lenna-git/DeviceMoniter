Ext.define('AM.controller.mainpageltbarController1', {
    extend: 'Ext.app.Controller',
    init: function() {
        this.control({
            'viewport mainpageltbar1': {
                select: this.onLbar,
                viewready: this.onViewready
            }
        });
    },

    refs : [ {
        selector : 'viewport  centerpage ',
        ref : 'center'
    },],
    onLbar: function (selModel, record) {
        // console.log('onLbar');
        if (record.get('leaf')) {
            this.getCenter().layout.setActiveItem(record.getId());
        }
    },
    onViewready: function (ltbar) {
        // console.log('onViewready');
        var selModel = ltbar.getSelectionModel();
        var rootNode = ltbar.getRootNode();
        selModel.select(rootNode.findChild('id', 'main-device', true));
    }
});
