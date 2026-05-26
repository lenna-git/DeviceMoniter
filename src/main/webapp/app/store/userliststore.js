

Ext.define('AM.store.userliststore',{
    extend:'Ext.data.Store',
    model:'AM.model.userlist',
    autoLoad:true,
    pageSize: 20,
    remoteSort: false,
    remoteFilter: false,

    proxy:{
        type:'ajax',
        url:'sysuseraction/allusers',
        pageParam: 'page',
        limitParam: 'limit',
        startParam: undefined,
        reader:{
            type:'json',
            root:'data',
            totalProperty:'total'
        },
        simpleSortMode: true
    },

    listeners: {
        beforeload: function(store, operation) {
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
        }
    }

})