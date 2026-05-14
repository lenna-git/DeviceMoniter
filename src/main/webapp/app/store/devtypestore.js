Ext.define('AM.store.devtypestore',{
    extend:'Ext.data.Store',
    model:'AM.model.devtype',
    autoLoad:true,

    proxy:{
        type:'ajax',
        url:'devtypeaction/alltypes',
        reader:{
            type:'json'
        }
    }
})