Ext.define('AM.view.mainpageltbar1', {
    extend: 'Ext.tree.Panel',
    alias : 'widget.mainpageltbar1',
    lines : false,
    store : Ext.create('Ext.data.TreeStore', {
        root : {
            expanded : true
        },
        proxy : {
            type : 'ajax',
            url : 'app/view/leftlist.json'
        }
    })
});