/**
 * 新建用户 主面板
 */
Ext.define('AM.view.user.xinjianyonghujiluwindow', {
        extend: 'Ext.window.Window',
        alias: 'widget.xinjianyonghujiluwindow',
        border: true,
        modal:true,
        autoDestroy: true,
        width: 400,
        height: 550,

        items: [{
            xtype: 'xinjianyonghupanel',
            // xtype:'panel',
            width: '100%',
            height: '100%'
        }]
    }

);


Ext.define('AM.view.user.xinjianyonghupanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.xinjianyonghupanel',
    border: false,

    // layout:'vbox',

    items: [
     {
        xtype: 'textfield',
        name: 'id',
        hidden:true,
        fieldLabel: 'id'
    },{
        xtype: 'textfield',
        name: 'userId',
        fieldLabel: '用户id'
    },{
        xtype: 'textfield',
        name: 'device.deviceno',
        fieldLabel: '设备编号'
    },{
        xtype: 'textfield',
        name: 'detail',
        fieldLabel: '设备详情'
    },{
        xtype: 'textfield',
        name: 'borrorDate',
        fieldLabel: '借用日期'
    },{
        xtype: 'textfield',
        name: 'returnDate',
        fieldLabel: '归还日期'
    },
    {
        xtype: 'panel',
        layout:{
            type:'hbox',
            pack:'end'
        },
        items: [
            {
                xtype: 'button',
                text: '确定',
                action: 'qd'
            },
            {
                xtype: 'button',
                text: '取消',
                action: 'qx'
            }
        ]
    }
    ],

});




