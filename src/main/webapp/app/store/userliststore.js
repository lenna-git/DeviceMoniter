

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
    }

})