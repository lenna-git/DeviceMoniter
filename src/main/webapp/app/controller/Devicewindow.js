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
        selector: 'devicexzwindow > combo[name=devcpu_id]',
        ref: 'devcpuselector'
    },{
        selector: 'devicexzwindow > combo[name=devtype_id]',
        ref: 'devtypeselector'
    },{
        selector: 'devicexzwindow > textfield[name=devicexh]',
        ref: 'devicexhtextfield'
    },{
        selector: 'devicexzwindow > combo[name=devmanufacturer_id]',
        ref: 'devmanufacturerselector'
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
        selector: 'devicexzwindow > combo[name=deviceyh.id]',
        ref: 'deviceyhcombo'
    },{
        selector: 'devicexzwindow > textfield[name=devicestate]',
        ref: 'devicestatetextfield'
    },{
        selector: 'devicexzwindow',
        ref: 'devicexzwindow'
    }],

    //保存用户信息到数据库。
    onBaocun: function () {
        var deviceidtextfield =this.getDeviceidtextfield();
        var deviceid = deviceidtextfield.getValue();

        var devcpuselector = this.getDevcpuselector();
        var devcpuId = devcpuselector.getValue();

        var devtypeselector = this.getDevtypeselector();
        var devtypeId = devtypeselector.getValue();

        var devicexhtextfield =this.getDevicexhtextfield();
        var devicexh = devicexhtextfield.getValue();

        var devmanufacturerselector = this.getDevmanufacturerselector();
        var devmanufacturerId = devmanufacturerselector.getValue();

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

        var deviceyhcombo = this.getDeviceyhcombo();
        var deviceyhId = deviceyhcombo.getValue();

        // var devicestatetextfield =this.getDevicestatetextfield();
        // var devicestate = devicestatetextfield.getValue();
        //
        // var deviceoptextfield =this.getDeviceoptextfield();
        // var deviceop = deviceoptextfield.getValue();
        var store = Ext.data.StoreMgr.lookup('deviceliststore');
        
        var excludeId = deviceid ? deviceid : null;
        var me = this;
        Ext.Ajax.request({
            url: 'devicerecord/checkDeviceSnAndNo',
            method: 'POST',
            params: {
                devicesn: devicesn,
                deviceno: deviceno,
                excludeId: excludeId
            },
            success: function(response) {
                var result = Ext.decode(response.responseText);
                if (!result.success) {
                    Ext.Msg.alert('提示', result.message);
                    return;
                }
                
                if(deviceid){
            var sm = me.getTestgrid().getSelectionModel();
            var sr = sm.getSelection();

            var ida = sr[0].get('id');
            var scinfo = String(ida);
            console.log(ida);
            //将id为2的记录修改为上面rec指定的值
            Ext.Ajax.request({
                url:'deviceaction/updatedevicebyid/'+ida,
                method:'PUT',
                jsonData: {
                    devCpu: { id: devcpuId },
                    devType: { id: devtypeId },
                    devManufacturer: { id: devmanufacturerId },
                    devicexh:devicexh,
                    devicesn:devicesn,
                    deviceno:deviceno,
                    // devicescdata:devicescdata,
                    // deviceajdata:deviceajdata,
                    // deviceghdata:deviceghdata,
                    deviceyh: deviceyhId ? { id: deviceyhId } : null,
                    // devicestate:devicestate,
                    // deviceop:deviceop,
                },
                headers: {
                    'Content-Type': 'application/json'
                },
                success:function(response,opts){
                    var obj = Ext.decode(response.responseText);
                    if(obj.success){
                        Ext.Msg.alert('结果显示',obj.message);
                    } else {
                        Ext.Msg.alert('提示', obj.message);
                    }
                    me.getDevicexzwindow().close();
                    store.reload();
                },
                failure:function(response,opts){
                    Ext.Msg.alert('保存错误', '保存失败');
                    me.getDevicexzwindow().close();
                    store.reload();
                },
                scope: me
            })
        }else {
            Ext.Ajax.request({
                url:'deviceaction/createdevice',
                method:'post',
                jsonData: {
                    devCpu: { id: devcpuId },
                    devType: { id: devtypeId },
                    devManufacturer: { id: devmanufacturerId },
                    devicexh:devicexh,
                    devicesn:devicesn,
                    deviceno:deviceno,
                    deviceyh: deviceyhId ? { id: deviceyhId } : null,
                },//跟rec生成json字符串一样
                // jsonData:rec,
                headers: {
                    'Content-Type': 'application/json'
                },
                success:function(response,opts){
                    var obj = Ext.decode(response.responseText);
                    if(obj.success){
                        Ext.Msg.alert('结果显示',obj.message);
                    } else {
                        Ext.Msg.alert('提示', obj.message);
                    }
                    me.getDevicexzwindow().close();
                    store.reload();
                },
                failure:function(response,opts){
                    var obj = Ext.decode(response.responseText);
                    Ext.Msg.alert('保存错误','错误原因：'+obj.message+"-------"+obj.msg);
                    me.getDevicexzwindow().close();
                    store.reload();
                },
                scope: me
            })
        }
            },
            failure: function() {
                Ext.Msg.alert('提示', '验证序列号和编号失败');
            }
        });
    },
    onQuxiao: function () {
        this.getDevicexzwindow().close();
    },


});