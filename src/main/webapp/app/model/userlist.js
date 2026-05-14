

Ext.define('AM.model.userlist',{
    extend:'Ext.data.Model',
    idProperty: 'id',
    fields:[
        {name:'id',type:'Long'},
        {name:'sysusername',type:'String'},
        {name:'sysuserrole',type:'Long'},
        {name:'sysuserpassword',type:'String'}
    ]
})