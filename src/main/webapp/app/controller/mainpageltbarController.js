Ext.define('AM.controller.mainpageltbarController', {
    extend: 'Ext.app.Controller',
    init: function() {
        this.control({
            'viewport  mainpageltbar button[action=showuser]': {
                click: this.onshowuserbuttonclick,
            },
            'viewport  mainpageltbar button[action=showdevices]': {
                click: this.onshowdevicesbuttonclick,
            },
        });
    },

    refs : [ {
        selector : 'viewport  centerpage ',
        ref : 'centerpage'
    },],
    onshowuserbuttonclick:function (){
        console.log('onshowuserbuttonclick');
        this.getCenterpage().layout.setActiveItem('main-user');

    },
    onshowdevicesbuttonclick:function (){
        console.log('onshowdevicesbuttonclick');
        this.getCenterpage().layout.setActiveItem('main-device');
    },
});
