Ext.define('AM.controller.Users', {
    extend: 'Ext.app.Controller',

    init: function() {
        this.control({
            'viewport > panel': {
                render: this.onPanelRendered
            },
            'viewport > panel > centerpage > userlist1 > testlistgrid':{
                cellclick:this.onsysusergridcellclick,
            },
            'viewport > panel > centerpage > userlist1 > button[action=xj]': {
                click: this.onxjbuttioncick
            },
        });
    },
    models:['userlist'],
    stores:['userliststore'],
    refs:[{
        selector: 'viewport > panel > centerpage > userlist1 > testlistgrid',
        ref:'testgrid'
    },{
        selector: 'sysuserrecordwindow',
        ref: 'sysuserrecordwindow'
    }],


    onPanelRendered: function() {
        // console.log('The userlistpanel was rendered');
    },
    onxjbuttioncick:function (){
        console.log('userlistpanel onxjbuttioncick ');
        var userwindow = Ext.widget({
            xtype: 'userwindow'
        });
        console.log('userwindow created:', userwindow);
        userwindow.show();
    },
    onsysusergridcellclick: function (view, cell, colIdx, record, row, rowIdx, e){
        console.log('onsysusergridcellclick');
        // var role = SYS_USER.sysuserrole;
        var role = record.get('sysuserrole');//获取点击对应行的用户角色
        console.log(role);
        if(role===1){
            //管理员
            console.log('管理员');
            //return;//角色是1，是管理员，没有对应的借用记录
        }else {
            //普通用户
            console.log('普通用户');//角色是2，是普通用户，点击查看按钮，可查看到对应的借用记录
            //弹窗
            var sysuserrecordwindow = Ext.widget({
                xtype:'sysuserrecordwindow'
            });
            //var grid = this.getTestgrid;
            // var sysusername = record.get('sysusername');
            // var store = this.getTestgrid().getStore();
            // store.getProxy().extraParams.sysusername = sysusername;
            if(colIdx===2){
                sysuserrecordwindow.show();
            }


        }
    },



});