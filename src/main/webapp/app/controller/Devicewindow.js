Ext.define('AM.controller.Devicewindow', {
    extend: 'Ext.app.Controller',
    init: function () {
        this.control({

            'devicexzwindow panel button[action=device-save]': {
                click: this.onBaocun
            },

            'devicexzwindow panel button[action=device-quxiao]': {
                click: this.onQuxiao
            },

        });
    },
    models:['devicelist'],
    stores:['deviceliststore'],
    refs: [{
        selector: 'viewport > panel > centerpage > devicelist > devicelistgrid',
        ref:'testgrid'
    },{
        selector: 'devicexzwindow > textfield[name=deviceid]',
        ref: 'deviceidtextfield'
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
    },{
        selector: 'devicexzwindow',
        ref: 'devicexzwindow'
    }],

    //保存用户信息到数据库。
    onBaocun: function () {
        var deviceidtextfield =this.getDeviceidtextfield();
        var deviceid = deviceidtextfield.getValue();

        var devicexptextfield =this.getDevicexptextfield();
        var devicexp = devicexptextfield.getValue();

        var devicetypetextfield =this.getDevicetypetextfield();
        var devicetype = devicetypetextfield.getValue();

        var devicexhtextfield =this.getDevicexhtextfield();
        var devicexh = devicexhtextfield.getValue();

        var devicecstextfield =this.getDevicecstextfield();
        var devicecs = devicecstextfield.getValue();

        var devicesntextfield =this.getDevicesntextfield();
        var devicesn = devicesntextfield.getValue();

        var devicenotextfield =this.getDevicenotextfield();
        var deviceno = devicenotextfield.getValue();

        var devicescdatatextfield =this.getDevicescdatatextfield();
        var devicescdata = devicescdatatextfield.getValue();

        var deviceajdatatextfield =this.getDeviceajdatatextfield();
        var deviceajdata = deviceajdatatextfield.getValue();

        var deviceghdatatextfield =this.getDeviceghdatatextfield();
        var deviceghdata = deviceghdatatextfield.getValue();

        var deviceyhtextfield =this.getDeviceyhtextfield();
        var deviceyh = deviceyhtextfield.getValue();

        var devicestatetextfield =this.getDevicestatetextfield();
        var devicestate = devicestatetextfield.getValue();

        var deviceoptextfield =this.getDeviceoptextfield();
        var deviceop = deviceoptextfield.getValue();
        var store = Ext.data.StoreMgr.lookup('deviceliststore');
        if(deviceid){
            var sm = this.getTestgrid().getSelectionModel();
            var sr = sm.getSelection();

            var ida = sr[0].get('id');
            var scinfo = String(ida);
            console.log(ida);
            //将id为2的记录修改为上面rec指定的值
            Ext.Ajax.request({
                url:'deviceaction/updatedevicebyid/'+ida,
                method:'PUT',
                jsonData: {
                    devicexp:devicexp,
                    devicetype:devicetype,
                    devicexh:devicexh,
                    devicecs:devicecs,
                    devicesn:devicesn,
                    deviceno:deviceno,
                    devicescdata:devicescdata,
                    deviceajdata:deviceajdata,
                    deviceghdata:deviceghdata,
                    deviceyh:deviceyh,
                    devicestate:devicestate,
                    deviceop:deviceop,
                },
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
            this.getDevicexzwindow().close();
            store.reload();
        }else {
            Ext.Ajax.request({
                url:'deviceaction/createdevice',
                method:'post',
                jsonData: {
                    devicexp:devicexp,
                    devicetype:devicetype,
                    devicexh:devicexh,
                    devicecs:devicecs,
                    devicesn:devicesn,
                    deviceno:deviceno,
                    devicescdata:devicescdata,
                    deviceajdata:deviceajdata,
                    deviceghdata:deviceghdata,
                    deviceyh:deviceyh,
                    devicestate:devicestate,
                    deviceop:deviceop,
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
            this.getDevicexzwindow().close();
            store.reload();
        }



    },
    onQuxiao: function () {
        this.getDevicexzwindow().close();
    },


});