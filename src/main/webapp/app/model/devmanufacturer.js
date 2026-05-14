Ext.define('AM.model.devmanufacturer',{
    extend:'Ext.data.Model',
    fields:[
        {name:'id',type:'int'},
        {name:'manufacturername',type:'string'},
        {name:'description',type:'string'}
    ],
    idProperty: 'id'
})