Ext.define('AM.model.devtype',{
    extend:'Ext.data.Model',
    fields:[
        {name:'id',type:'int'},
        {name:'typename',type:'string'},
        {name:'description',type:'string'}
    ],
    idProperty: 'id'
})