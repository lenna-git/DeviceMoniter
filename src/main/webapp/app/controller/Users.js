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
            'viewport > panel > centerpage > userlist1 > button[action=sc]': {
                click: this.onscbuttioncick
            },
            'viewport > panel > centerpage > userlist1 > button[action=update]': {
                click: this.onupdatebuttioncick
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
    onscbuttioncick: function() {
        console.log('userlistpanel onscbuttioncick');
        var grid = this.getTestgrid();
        var selection = grid.getSelectionModel().getSelection();
        
        if (selection.length === 0) {
            Ext.Msg.alert('提示', '请先选择要删除的用户');
            return;
        }
        
        var record = selection[0];
        var userId = record.get('id');
        
        Ext.Msg.confirm('确认删除', '确定要删除该用户吗？', function(btn) {
            if (btn === 'yes') {
                Ext.Ajax.request({
                    url: 'sysuseraction/deleteuser',
                    method: 'delete',
                    params: {
                        id: userId
                    },
                    success: function(response, opts) {
                        var result = Ext.decode(response.responseText);
                        if (result.success) {
                            Ext.Msg.alert('提示', result.message);
                            grid.getStore().reload();
                        } else {
                            Ext.Msg.alert('提示', result.message);
                        }
                    },
                    failure: function(response, opts) {
                        Ext.Msg.alert('提示', '删除失败');
                    }
                });
            }
        });
    },
    onupdatebuttioncick: function() {
        console.log('userlistpanel onupdatebuttioncick');
        var grid = this.getTestgrid();
        var selection = grid.getSelectionModel().getSelection();
        
        if (selection.length === 0) {
            Ext.Msg.alert('提示', '请先选择要修改的用户');
            return;
        }
        
        var record = selection[0];
        
        var userwindow = Ext.widget({
            xtype: 'userwindow',
            title: '修改用户'
        });
        
        var form = userwindow.down('form') || userwindow;
        form.down('textfield[name=id]').setValue(record.get('id'));
        form.down('textfield[name=sysusername]').setValue(record.get('sysusername'));
        form.down('textfield[name=sysuserpassword]').setValue(record.get('sysuserpassword'));
        form.down('combo[name=sysuserrole]').setValue(record.get('sysuserrole'));
        
        userwindow.show();
    }

});