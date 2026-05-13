Ext.define('AM.controller.UserwindowController', {
    extend: 'Ext.app.Controller',

    init: function() {
        console.log('UserwindowController initialized');
        this.control({
            'userwindow button[action=cancel]': {
                click: this.onCancel
            }
        });
    },

    onCancel: function(button) {
        console.log('cancel');
        var win = button.up('window');
        if (win) {
            win.close();
        }
    }

});