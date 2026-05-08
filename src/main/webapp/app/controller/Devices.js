Ext.define('AM.controller.Devices', {
    extend: 'Ext.app.Controller',

    init: function() {
        this.control({
            //找对应的按钮
            'viewport > panel': {
                render: this.onPanelRendered
            },
            'viewport > panel > centerpage > devicelist > devicelistgrid':{
                itemdblclick: this.onDblClick,
                cellclick:this.ondevicegridcellclick,
            },
            'viewport > panel > centerpage > devicelist > panel > button[action=xz]':{
                click: this.onxzbuttonclick
            },
            'viewport > panel > centerpage > devicelist > panel > button[action=sc]':{
                click: this.onscbuttonclick
            },
            'viewport > panel > centerpage > devicelist > panel > button[action=update]':{
                click: this.onupdatebuttonclick
            },
            'viewport > panel > centerpage > devicelist > panel > button[action=test]':{
                click: this.ontestbuttonclick
            },
            'viewport > panel > centerpage > devicelist > panel > button[action=devicesearch]':{
                click: this.ondevcxbuttonclick
            },

        });
    },
    models:['devicelist'],
    stores:['deviceliststore'],
    refs:[{
        selector: 'viewport > panel > centerpage > devicelist > devicelistgrid',
        ref:'testgrid'
    },{
        selector: 'viewport > panel > centerpage > devicelist > panel > textfield[name=queryxh]',
        ref: 'devicequeryxhtextfield'
    },{
        selector: 'viewport > panel > centerpage > devicelist > panel > textfield[name=querycs]',
        ref: 'devicequerycstextfield'
    },{
        selector: 'devicexzwindow > textfield[name=devicexp]',
        ref: 'devicexptextfield'
    },{
        selector: 'devicexzwindow > textfield[name=devicetype]',
        ref: 'devicetypetextfield'
    },{
        selector: 'devicexzwindow > textfield[name=devicexh]',
        ref: 'devicexhtextfield'
    },{
        selector: 'devicexzwindow > textfield[name=devicecs]',
        ref: 'devicecstextfield'
    },{
        selector: 'devicexzwindow > textfield[name=devicesn]',
        ref: 'devicesntextfield'
    },{
        selector: 'devicexzwindow > textfield[name=deviceno]',
        ref: 'devicenotextfield'
    },{
        selector: 'devicexzwindow > textfield[name=devicescdata]',
        ref: 'devicescdatatextfield'
    },{
        selector: 'devicexzwindow > textfield[name=deviceajdata]',
        ref: 'deviceajdatatextfield'
    },{
        selector: 'devicexzwindow > textfield[name=deviceghdata]',
        ref: 'deviceghdatatextfield'
    },{
        selector: 'devicexzwindow > textfield[name=deviceyh]',
        ref: 'deviceyhtextfield'
    },{
        selector: 'devicexzwindow > textfield[name=devicestate]',
        ref: 'devicestatetextfield'
    },{
        selector: 'devicexzwindow > textfield[name=deviceop]',
        ref: 'deviceoptextfield'
    }],
    onPanelRendered: function() {
        console.log('The panel was rrrrendered');
    },
    ondevcxbuttonclick: function (){
        var searchxh = this.getDevicequeryxhtextfield().getValue();
        var searchcs = this.getDevicequerycstextfield().getValue();
        console.log(searchxh);
        console.log(searchcs);
        var store  = this.getTestgrid().getStore();
        store.getProxy().extraParams.devicexh = searchxh;
        store.getProxy().extraParams.devicecs = searchcs;
        //store.getProxy().extraParams.devicexh='1';
        store.reload();

    },

    onDblClick: function(grid,record){
        var devicexzwindow = Ext.widget({
            xtype: 'devicexzwindow'
        });
        var devicexp = record.get('devicexp');
        var devicetype = record.get('devicetype');
        var devicexh = record.get('devicexh');
        var devicecs = record.get('devicecs');
        var devicesn = record.get('devicesn');
        var deviceno = record.get('deviceno');
        var devicescdata = record.get('devicescdata');
        var deviceajdata = record.get('deviceajdata');
        var deviceghdata = record.get('deviceghdata');
        var deviceyh = record.get('deviceyh');
        var devicestate = record.get('devicestate');
        var deviceop = record.get('deviceop');
        // var devicename = record.get('devicename');
        // var devicesn = record.get('devicesn');
        // var devicecs = record.get('devicecs');
        var deviceid = record.get('id');
        devicexzwindow.down('textfield[name=devicexp]').setValue(devicexp);
        devicexzwindow.down('textfield[name=devicetype]').setValue(devicetype);
        devicexzwindow.down('textfield[name=devicexh]').setValue(devicexh);
        devicexzwindow.down('textfield[name=devicecs]').setValue(devicecs);
        devicexzwindow.down('textfield[name=devicesn]').setValue(devicesn);
        devicexzwindow.down('textfield[name=deviceno]').setValue(deviceno);
        devicexzwindow.down('textfield[name=devicescdata]').setValue(devicescdata);
        devicexzwindow.down('textfield[name=deviceajdata]').setValue(deviceajdata);
        devicexzwindow.down('textfield[name=deviceghdata]').setValue(deviceghdata);
        devicexzwindow.down('textfield[name=deviceyh]').setValue(deviceyh);
        devicexzwindow.down('textfield[name=devicestate]').setValue(devicestate);
        devicexzwindow.down('textfield[name=deviceop]').setValue(deviceop);
        devicexzwindow.down('textfield[name=deviceid]').setValue(deviceid);
        devicexzwindow.show();
    },

    onxzbuttonclick:function(){
        var devicexzwindow = Ext.widget({
            xtype: 'devicexzwindow'
        });
        var grid = this.getTestgrid;
        var store = grid.getStore;
        devicexzwindow.show();
    },

    onscbuttonclick:function (){
        console.log('sc successful');

        var smo = this.getTestgrid();
        var store = smo.getStore();
        var sm = this.getTestgrid().getSelectionModel();
        var sr = sm.getSelection();
        var ysstore = Ext.data.StoreMgr.lookup('deviceliststore');
        var ida = sr[0].get('id');
        var scinfo = String(ida);
        console.log(ida);
        if (sr.length>0){
            Ext.MessageBox.confirm(
                '提示',
                '您确定要删除选中记录吗？',
                function(button){
                    if(button=='yes'){
                        Ext.Ajax.request({
                            url: 'deviceaction/deldevices/'+ida,
                            //url: 'deviceaction/deldevices/',
                            method: 'DELETE',
                            // params:{
                            //     id: ida
                            // },
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
                        ysstore.reload();
                    }
                }
            )
        }


    },
    onupdatebuttonclick:function (){
        console.log('update successful');
        var rec ={
            devicexp:'updatename22221111',
            devicetype:'updatesn22221111',
            devicexh:'updatecs22221111',
            devicecs:'updatename22221111',
            devicesn:'updatesn22221111',
            deviceno:'updatecs22221111',
            devicescdata:'updatename22221111',
            deviceajdata:'updatesn22221111',
            deviceghdata:'updatecs22221111',
            deviceyh:'updatename22221111',
            devicestate:'updatesn22221111',
            deviceop:'updatecs22221111',
        }
        //将id为2的记录修改为上面rec指定的值
        Ext.Ajax.request({
            url:'deviceaction/updatedevicebyid/'+"3",
            method:'PUT',
            jsonData: rec,
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
    ontestbuttonclick:function (){
        console.log('test successful');
        console.log('name:'+SYS_USER.sysusername+'    password:'+SYS_USER.sysuserpassword)

    },


    ondevicegridcellclick:function (view, cell, colIdx, record, row, rowIdx, e){
        console.log('ondevicegridcellclick');
        console.log('cellindex:'+colIdx); //列号
        console.log('recid:'+record.get('id'));
        console.log('rowIndex:'+rowIdx);//行号
        var role = SYS_USER.sysuserrole;
        var sm = this.getTestgrid().getSelectionModel();
        var sr = sm.getSelection();
        var ida = sr[0].get('id');
        var now = new Date();
        var borrowTime = now.toISOString();
        if(role===1)
            return;//角色为1，是管理员，没有借阅权限，点此按钮无反应
        else if(role===2){
            console.log('allowed');//角色为2，是用户，有借阅权限，点此按钮对图书进行借阅

        if(colIdx===12){
            console.log('开始借阅。。。');//列号为12，才能出发借阅操作，点其他列无反应
            //接下来写借阅代码
            Ext.Ajax.request({
                url:'devicerecord/createDeviceRecord',
                method:'post',
                jsonData: {
                    deviceid:ida,
                    borrorDate:borrowTime,
                    returnDate:null,
                    detail:null,
                },//跟rec生成json字符串一样
                // jsonData:rec,
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
        }
        }
    },

});