Ext.define('AM.view.user.sysuserrecordwindow' ,{
    extend: 'Ext.window.Window',
    alias:'widget.sysuserrecordwindow',
    border:true,
    width:700,
    height:400,
    title: '借用记录',
    items:[{
        xtype: 'sysrecordlistgrid',
        width: '100%',
    }],

});

Ext.define('AM.view.user.sysrecordlistgrid',{
    extend:'Ext.grid.Panel',
    alias:'widget.sysrecordlistgrid',
    store:'userliststore',//拿数据
    columns:[{
        text:'用户id',
        align:'center',
        style:'font-size:16px',
        dataIndex:'userId',
        flex:1,
    },{
        text:'设备编号',
        align:'center',
        style:'font-size:16px',
        dataIndex:'deviceId',
        flex:1,
    },{
        text:'设备详情',
        align:'center',
        style:'font-size:16px',
        dataIndex:'detail',
        flex:1,
    },{
        text:'数据库id',
        align:'center',
        style:'font-size:16px',
        dataIndex:'id',
        flex:1,
    },{
        text:'借用日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'borrorDate',
        flex:1,
    },{
        text:'归还日期',
        align:'center',
        style:'font-size:16px',
        dataIndex:'returnDate',
        flex:1,
    }],
})
