Ext.define('AM.store.devmanufacturerstore',{
    extend:'Ext.data.Store',
    model:'AM.model.devmanufacturer',
    autoLoad:true,

    proxy:{
        type:'ajax',
        url:'devmanufactureraction/allmanufacturers',
        reader:{
            type:'json'
        }
    }
})