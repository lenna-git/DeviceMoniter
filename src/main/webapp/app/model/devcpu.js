Ext.define('AM.model.devcpu',{
    extend:'Ext.data.Model',
    fields:[
        {name:'id',type:'int'},
        {name:'cpuname',type:'string'},
        {name:'description',type:'string'}
    ],
    idProperty: 'id'
})