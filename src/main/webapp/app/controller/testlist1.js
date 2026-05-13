Ext.define('AM.controller.testlist1', {
    extend: 'Ext.app.Controller',


    init: function() {
        this.control({
            'viewport centerpage userlist1': {
                render: this.onPanelRendered
            },
            'viewport > userlist1 > testlistgrid': {
                itemDblClick: this.onItemDblClick1,
                selectionchange:this.onselectchange,
                onCellDblClick1:this.onCellDblClick1,
            },
            'viewport > userlist1 > button[action=xj]': {
                click: this.onxjbuttioncick
            },
            'viewport > userlist1 > button[action=sc]': {
                click: this.onscbuttioncick
            },
            'viewport > userlist1 > button[action=update]': {
                click: this.onupdatebuttioncick
            },
            'viewport centerpage userlist1 button[action=test]': {
                click: this.ontestbuttioncick
            },
        });
    },

    models:['userlist'],
    stores:['userliststore'],
    refs:[{
        selector: 'viewport > userlist1 > testlistgrid',
        ref:'testgrid'
    }],

    onPanelRendered: function() {
        console.log('testlist1 panel was rendered11112222');
    },

    onItemDblClick1: function() {
        console.log('testlistgrid onItemDblClick');
    },

    onxjbuttioncick:function (){
        console.log('testbuttonclick');
        var grid = this.getTestgrid();
        var store= grid.getStore();


        //往数据库里插入一条记录
        var rec ={
            username:'aaaa',
            email:'111111',
        }
        Ext.Ajax.request({
            url:'useraction/createuser',
            method:'post',
            jsonData:rec,
            headers: {
                'Content-Type': 'application/json'
            },
            sucess:function(response,opts){
                var obj = Ext.decode(response.responseText);
                if(obj.sucess){
                    Ext.Msg.alert('结果显示',obj.message);
                }
            },
            failure:function(response,opts){
                var obj = Ext.decode(response.responseText);
                Ext.Msg.alert('保存错误','错误原因：'+obj.message+"-------"+obj.msg);
            }
        })
    },

    onscbuttioncick:function (){

        Ext.Ajax.request({
            url:'useraction/delusers/'+"120",
            method:'DELETE',
            sucess:function(response,opts){
                var obj = Ext.decode(response.responseText);
                if(obj.sucess){
                    Ext.Msg.alert('结果显示',obj.message);
                }
            },
            failure:function(response,opts){
                var obj = Ext.decode(response.responseText);
                Ext.Msg.alert('保存错误','错误原因：'+obj.message+"-------"+obj.msg);
            }
        })
    },


    onupdatebuttioncick:function (){
        var rec ={
            username:'bbbbb',
            email:'454545',
        }

        //将id为122的记录修改为上面rec指定的值
        Ext.Ajax.request({
            url:'useraction/updateuserbyid/'+"122",
            method:'PUT',
            jsonData:rec,
            headers: {
                'Content-Type': 'application/json'
            },
            sucess:function(response,opts){
                var obj = Ext.decode(response.responseText);
                if(obj.sucess){
                    Ext.Msg.alert('结果显示',obj.message);
                }
            },
            failure:function(response,opts){
                var obj = Ext.decode(response.responseText);
                Ext.Msg.alert('保存错误','错误原因：'+obj.message+"-------"+obj.msg);
            }
        })
    },
    onselectchange:function (grid){
        console.log('onselectchange');
        // debugger;
        var sels = grid.getSelectionModel();
        var a = sels.getSelections()
        var rec = a[0];
        var name = rec.get('name');
        console.log('11111'+name);
    },

    onCellDblClick1:function (){
        console.log('onCellDblClick1');
    },

    ontestbuttioncick:function (){
        console.log('ontestbuttioncick');
        console.log('name:'+SYS_USER.sysusername+'    password:'+SYS_USER.sysuserpassword)
        var c = Ext.ComponentQuery.query('viewport');
        console.log('viewport:'+c[0].getWidth());
        var a = Ext.ComponentQuery.query('viewport > toolbar');
        var b = Ext.ComponentQuery.query('viewport toolbar button[action=logout]');
        console.log('ddkfdjfdkfjdk:'+b[0].getText());
    }
});