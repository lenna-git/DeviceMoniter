

Ext.define('AM.store.userliststore',{
    extend:'Ext.data.Store',
    model:'AM.model.userlist',
    autoLoad:true,

    proxy:{
        type:'ajax',
        api:{
            read:'sysuseraction/allusers',
            create:'sysuseraction/createuser',//暂时未用到
        }
    },

})