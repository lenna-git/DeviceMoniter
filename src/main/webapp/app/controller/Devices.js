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
            'viewport > panel > centerpage > devicelist toolbar button[action=xz]':{
                click: this.onxzbuttonclick
            },
            'viewport > panel > centerpage > devicelist toolbar button[action=sc]':{
                click: this.onscbuttonclick
            },
            'viewport > panel > centerpage > devicelist toolbar button[action=update]':{
                click: this.onupdatebuttonclick
            },
            'viewport > panel > centerpage > devicelist toolbar button[action=devicesearch]':{
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
        selector: 'viewport > panel > centerpage > devicelist toolbar textfield[name=queryxp]',
        ref: 'devicequeryxptextfield'
    },{
        selector: 'viewport > panel > centerpage > devicelist toolbar textfield[name=querylx]',
        ref: 'devicequerylxtextfield'
    },{
        selector: 'viewport > panel > centerpage > devicelist toolbar textfield[name=queryxh]',
        ref: 'devicequeryxhtextfield'
    },{
        selector: 'viewport > panel > centerpage > devicelist toolbar textfield[name=querycs]',
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
        selector: 'devicexzwindow > combo[name=devicestate.id]',
        ref: 'devicestatecombo'
    }],
    onPanelRendered: function() {
        // console.log('The panel was rrrrendered');
    },
    ondevcxbuttonclick: function (){
        var searchxp = this.getDevicequeryxptextfield() ? this.getDevicequeryxptextfield().getValue() : '';
        var searchlx = this.getDevicequerylxtextfield() ? this.getDevicequerylxtextfield().getValue() : '';
        var searchxh = this.getDevicequeryxhtextfield() ? this.getDevicequeryxhtextfield().getValue() : '';
        var searchcs = this.getDevicequerycstextfield() ? this.getDevicequerycstextfield().getValue() : '';
        
        console.log('查询条件 - 芯片:', searchxp, '类型:', searchlx, '型号:', searchxh, '厂商:', searchcs);
        
        var store = this.getTestgrid().getStore();
        var proxy = store.getProxy();
        
        proxy.extraParams = {};
        if (searchxp) {
            proxy.extraParams.devicexp = searchxp;
        }
        if (searchlx) {
            proxy.extraParams.devicetype = searchlx;
        }
        if (searchxh) {
            proxy.extraParams.devicexh = searchxh;
        }
        if (searchcs) {
            proxy.extraParams.devicecs = searchcs;
        }
        
        store.loadPage(1);
    },

    onDblClick: function(grid,record){
        var devicexzwindow = Ext.widget({
            xtype: 'devicexzwindow',
            isEdit: true,
            title: '修改设备'
        });
        var devicexh = record.get('devicexh');
        var devicesn = record.get('devicesn');
        var deviceno = record.get('deviceno');
        var devicescdata = record.get('devicescdata');
        var deviceajdata = record.get('deviceajdata');
        var deviceghdata = record.get('deviceghdata');
        var deviceyh = record.get('deviceyh') ? record.get('deviceyh').sysusername : '';
        var devicestate = record.get('devicestate');
        var deviceid = record.get('id');
        
        devicexzwindow.down('textfield[name=devicexh]').setValue(devicexh);
        devicexzwindow.down('textfield[name=devicesn]').setValue(devicesn);
        devicexzwindow.down('textfield[name=deviceno]').setValue(deviceno);
        devicexzwindow.down('datefield[name=devicescdata]').setValue(devicescdata ? new Date(devicescdata) : null);
        devicexzwindow.down('datefield[name=deviceajdata]').setValue(deviceajdata ? new Date(deviceajdata) : null);
        devicexzwindow.down('datefield[name=deviceghdata]').setValue(deviceghdata ? new Date(deviceghdata) : null);
        var deviceyhId = record.get('deviceyh') ? record.get('deviceyh').id : null;
        devicexzwindow.down('combo[name=deviceyh.id]').setValue(deviceyhId);
        devicexzwindow.down('combo[name=devicestate.id]').setValue(devicestate ? devicestate.id : null);
        devicexzwindow.down('textfield[name=deviceid]').setValue(deviceid);
        
        var devCpu = record.get('devCpu');
        var devType = record.get('devType');
        var devManufacturer = record.get('devManufacturer');
        
        var cpuCombo = devicexzwindow.down('combo[name=devcpu_id]');
        var typeCombo = devicexzwindow.down('combo[name=devtype_id]');
        var manufacturerCombo = devicexzwindow.down('combo[name=devmanufacturer_id]');
        
        var cpuStore = cpuCombo.getStore();
        var typeStore = typeCombo.getStore();
        var manufacturerStore = manufacturerCombo.getStore();
        
        var setComboValues = function() {
            cpuCombo.setValue(devCpu ? devCpu.id : null);
            typeCombo.setValue(devType ? devType.id : null);
            manufacturerCombo.setValue(devManufacturer ? devManufacturer.id : null);
        };
        
        var pendingLoads = 3;
        var onStoreLoad = function() {
            pendingLoads--;
            if (pendingLoads === 0) {
                setComboValues();
            }
        };
        
        if (cpuStore.getCount() > 0) {
            pendingLoads--;
        } else {
            cpuStore.on('load', onStoreLoad, null, { single: true });
        }
        
        if (typeStore.getCount() > 0) {
            pendingLoads--;
        } else {
            typeStore.on('load', onStoreLoad, null, { single: true });
        }
        
        if (manufacturerStore.getCount() > 0) {
            pendingLoads--;
        } else {
            manufacturerStore.on('load', onStoreLoad, null, { single: true });
        }
        
        if (pendingLoads === 0) {
            setComboValues();
        }
        
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
        console.log('onupdatebuttonclick');
        var grid = this.getTestgrid();
        var selection = grid.getSelectionModel().getSelection();
        
        if (selection.length === 0) {
            Ext.Msg.alert('提示', '请先选择要修改的设备');
            return;
        }
        
        var record = selection[0];
        
        var devicexzwindow = Ext.widget({
            xtype: 'devicexzwindow',
            title: '修改设备',
            isEdit: true
        });
        
        devicexzwindow.down('textfield[name=deviceid]').setValue(record.get('id'));
        devicexzwindow.down('textfield[name=devicexh]').setValue(record.get('devicexh'));
        devicexzwindow.down('textfield[name=devicesn]').setValue(record.get('devicesn'));
        devicexzwindow.down('textfield[name=deviceno]').setValue(record.get('deviceno'));
        devicexzwindow.down('datefield[name=devicescdata]').setValue(record.get('devicescdata') ? new Date(record.get('devicescdata')) : null);
        devicexzwindow.down('datefield[name=deviceajdata]').setValue(record.get('deviceajdata') ? new Date(record.get('deviceajdata')) : null);
        devicexzwindow.down('datefield[name=deviceghdata]').setValue(record.get('deviceghdata') ? new Date(record.get('deviceghdata')) : null);
        var yhRecord = record.get('deviceyh');
        devicexzwindow.down('combo[name=deviceyh.id]').setValue(yhRecord ? yhRecord.id : null);
        var devicestate = record.get('devicestate');
        devicexzwindow.down('combo[name=devicestate.id]').setValue(devicestate ? devicestate.id : null);
        
        var devCpu = record.get('devCpu');
        var devType = record.get('devType');
        var devManufacturer = record.get('devManufacturer');
        
        var cpuCombo = devicexzwindow.down('combo[name=devcpu_id]');
        var typeCombo = devicexzwindow.down('combo[name=devtype_id]');
        var manufacturerCombo = devicexzwindow.down('combo[name=devmanufacturer_id]');
        
        var cpuStore = cpuCombo.getStore();
        var typeStore = typeCombo.getStore();
        var manufacturerStore = manufacturerCombo.getStore();
        
        var setComboValues = function() {
            cpuCombo.setValue(devCpu ? devCpu.id : null);
            typeCombo.setValue(devType ? devType.id : null);
            manufacturerCombo.setValue(devManufacturer ? devManufacturer.id : null);
        };
        
        var pendingLoads = 3;
        var onStoreLoad = function() {
            pendingLoads--;
            if (pendingLoads === 0) {
                setComboValues();
            }
        };
        
        if (cpuStore.getCount() > 0) {
            pendingLoads--;
        } else {
            cpuStore.on('load', onStoreLoad, null, { single: true });
        }
        
        if (typeStore.getCount() > 0) {
            pendingLoads--;
        } else {
            typeStore.on('load', onStoreLoad, null, { single: true });
        }
        
        if (manufacturerStore.getCount() > 0) {
            pendingLoads--;
        } else {
            manufacturerStore.on('load', onStoreLoad, null, { single: true });
        }
        
        if (pendingLoads === 0) {
            setComboValues();
        }
        
        devicexzwindow.show();
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
        
        var target = e.getTarget('.check-device-link');
        if (target) {
            e.stopEvent();
            var deviceId = target.getAttribute('data-id');
            Ext.Msg.confirm('确认安检', '确定要对该设备进行安检吗？', function(btn) {
                if (btn === 'yes') {
                    Ext.Ajax.request({
                        url: 'deviceaction/checkdevice/' + deviceId,
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        success: function(response, opts) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                Ext.Msg.alert('结果显示', obj.message);
                                var store = Ext.data.StoreMgr.lookup('deviceliststore');
                                store.reload();
                            } else {
                                Ext.Msg.alert('提示', obj.message);
                            }
                        },
                        failure: function(response, opts) {
                            Ext.Msg.alert('安检失败', '设备安检失败');
                        },
                        scope: this
                    });
                }
            }, this);
            return;
        }
        
        target = e.getTarget('.shelve-device-link');
        if (target) {
            e.stopEvent();
            var deviceId = target.getAttribute('data-id');
            
            var actionWindow = Ext.create('Ext.window.Window', {
                title: '设备操作',
                width: 300,
                height: 150,
                layout: 'hbox',
                align: 'center',
                items: [{
                    xtype: 'button',
                    text: '归还',
                    width: 100,
                    margin: '10 10 10 40',
                    handler: function() {
                        Ext.Ajax.request({
                            url: 'deviceaction/returndevice/' + deviceId,
                            method: 'PUT',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            success: function(response, opts) {
                                var obj = Ext.decode(response.responseText);
                                if (obj.success) {
                                    Ext.Msg.alert('结果显示', obj.message);
                                    var store = Ext.data.StoreMgr.lookup('deviceliststore');
                                    store.reload();
                                } else {
                                    Ext.Msg.alert('提示', obj.message);
                                }
                            },
                            failure: function(response, opts) {
                                Ext.Msg.alert('操作失败', '设备归还失败');
                            }
                        });
                        actionWindow.close();
                    }
                }, {
                    xtype: 'button',
                    text: '维修',
                    width: 100,
                    margin: '10 0 10 10',
                    handler: function() {
                        actionWindow.close();
                        
                        var repairWindow = Ext.create('Ext.window.Window', {
                            title: '设备维修',
                            width: 400,
                            height: 200,
                            layout: 'vbox',
                            align: 'center',
                            items: [{
                                xtype: 'textarea',
                                fieldLabel: '维修原因',
                                name: 'repairReason',
                                width: 350,
                                height: 80,
                                labelWidth: 60,
                                margin: '10 0 10 0',
                                emptyText: '请输入维修原因...'
                            }, {
                                xtype: 'panel',
                                layout: 'hbox',
                                margin: '0 0 10 50',
                                items: [{
                                    xtype: 'button',
                                    text: '确定',
                                    width: 100,
                                    margin: '0 10 0 0',
                                    handler: function() {
                                        var repairReason = repairWindow.down('textarea[name=repairReason]').getValue();
                                        if (!repairReason || repairReason.trim() === '') {
                                            Ext.Msg.alert('提示', '请输入维修原因');
                                            return;
                                        }
                                        
                                        Ext.Ajax.request({
                                            url: 'devicerepair/create',
                                            method: 'POST',
                                            jsonData: {
                                                deviceId: deviceId,
                                                repairReason: repairReason
                                            },
                                            headers: {
                                                'Content-Type': 'application/json'
                                            },
                                            success: function(response, opts) {
                                                var obj = Ext.decode(response.responseText);
                                                if (obj.success) {
                                                    Ext.Ajax.request({
                                                        url: 'deviceaction/repairdevice/' + deviceId,
                                                        method: 'PUT',
                                                        headers: {
                                                            'Content-Type': 'application/json'
                                                        },
                                                        success: function(response2, opts2) {
                                                            var obj2 = Ext.decode(response2.responseText);
                                                            if (obj2.success) {
                                                                Ext.Msg.alert('结果显示', '维修记录创建成功，设备状态已更新');
                                                                var store = Ext.data.StoreMgr.lookup('deviceliststore');
                                                                store.reload();
                                                            } else {
                                                                Ext.Msg.alert('提示', obj2.message);
                                                            }
                                                        },
                                                        failure: function(response2, opts2) {
                                                            Ext.Msg.alert('操作失败', '设备维修状态更新失败');
                                                        }
                                                    });
                                                } else {
                                                    Ext.Msg.alert('提示', obj.message);
                                                }
                                            },
                                            failure: function(response, opts) {
                                                Ext.Msg.alert('操作失败', '维修记录创建失败');
                                            }
                                        });
                                        repairWindow.close();
                                    }
                                }, {
                                    xtype: 'button',
                                    text: '取消',
                                    width: 100,
                                    handler: function() {
                                        repairWindow.close();
                                    }
                                }]
                            }]
                        });
                        repairWindow.show();
                    }
                }]
            });
            actionWindow.show();
            return;
        }
        
        target = e.getTarget('.unshelve-device-link');
        if (target) {
            e.stopEvent();
            var deviceId = target.getAttribute('data-id');
            
            var unshelveWindow = Ext.create('Ext.window.Window', {
                title: '设备上架',
                width: 400,
                height: 200,
                layout: 'vbox',
                align: 'center',
                items: [{
                    xtype: 'textarea',
                    fieldLabel: '维修记录',
                    name: 'repairRecord',
                    width: 350,
                    height: 80,
                    labelWidth: 60,
                    margin: '10 0 10 0',
                    emptyText: '请输入维修记录备注...'
                }, {
                    xtype: 'panel',
                    layout: 'hbox',
                    margin: '0 0 10 50',
                    items: [{
                        xtype: 'button',
                        text: '确定',
                        width: 100,
                        margin: '0 10 0 0',
                        handler: function() {
                            var repairRecord = unshelveWindow.down('textarea[name=repairRecord]').getValue();
                            
                            Ext.Ajax.request({
                                url: 'deviceaction/unshelvedevice/' + deviceId,
                                method: 'PUT',
                                jsonData: {
                                    repairRecord: repairRecord
                                },
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                success: function(response, opts) {
                                    var obj = Ext.decode(response.responseText);
                                    if (obj.success) {
                                        Ext.Msg.alert('结果显示', obj.message);
                                        var store = Ext.data.StoreMgr.lookup('deviceliststore');
                                        store.reload();
                                    } else {
                                        Ext.Msg.alert('提示', obj.message);
                                    }
                                },
                                failure: function(response, opts) {
                                    Ext.Msg.alert('上架失败', '设备上架失败');
                                },
                                scope: this
                            });
                            unshelveWindow.close();
                        }
                    }, {
                        xtype: 'button',
                        text: '取消',
                        width: 100,
                        handler: function() {
                            unshelveWindow.close();
                        }
                    }]
                }]
            });
            unshelveWindow.show();
            return;
        }
        
        target = e.getTarget('.view-repair-link');
        if (target) {
            e.stopEvent();
            var deviceId = target.getAttribute('data-id');
            
            Ext.Ajax.request({
                url: 'devicerepair/bydevice/' + deviceId,
                method: 'GET',
                success: function(response, opts) {
                    var repairs = Ext.decode(response.responseText);
                    
                    var grid = Ext.create('Ext.grid.Panel', {
                        border: false,
                        columns: [{
                            text: '维修时间',
                            dataIndex: 'repairTime',
                            width: 180
                        }, {
                            text: '结束时间',
                            dataIndex: 'endRepairTime',
                            width: 180
                        }, {
                            text: '维修原因',
                            dataIndex: 'repairReason',
                            flex: 1
                        }, {
                            text: '修理记录',
                            dataIndex: 'repairRecord',
                            flex: 1
                        }],
                        store: Ext.create('Ext.data.Store', {
                            fields: ['repairTime', 'endRepairTime', 'repairReason', 'repairRecord'],
                            data: repairs.map(function(r) {
                                return {
                                    repairTime: r.repairTime ? r.repairTime.replace('T', ' ') : '-',
                                    endRepairTime: r.endRepairTime ? r.endRepairTime.replace('T', ' ') : '-',
                                    repairReason: r.repairReason || '-',
                                    repairRecord: r.repairRecord || '-'
                                };
                            })
                        }),
                        height: 300
                    });
                    
                    var window = Ext.create('Ext.window.Window', {
                        title: '设备维修记录',
                        width: 800,
                        height: 400,
                        layout: 'fit',
                        items: [grid],
                        buttons: [{
                            text: '关闭',
                            handler: function() {
                                window.close();
                            }
                        }]
                    });
                    window.show();
                },
                failure: function(response, opts) {
                    Ext.Msg.alert('错误', '获取维修记录失败');
                }
            });
            return;
        }
        
        target = e.getTarget('.borrow-device-link');
        if (target) {
            e.stopEvent();
            var deviceId = target.getAttribute('data-id');
            Ext.Msg.confirm('确认借用', '确定要借用该设备吗？', function(btn) {
                if (btn === 'yes') {
                    Ext.Ajax.request({
                        url: 'devicerecord/createDeviceRecord',
                        method: 'POST',
                        jsonData: {
                            device: { id: deviceId },
                            borrowTime: new Date().toISOString(),
                            deviceRecordState: { id: 1 }
                        },
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        success: function(response, opts) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                Ext.Msg.alert('结果显示', obj.message);
                                var store = Ext.data.StoreMgr.lookup('deviceliststore');
                                store.reload();
                            } else {
                                Ext.Msg.alert('提示', obj.message);
                            }
                        },
                        failure: function(response, opts) {
                            Ext.Msg.alert('借用失败', '设备借用失败');
                        },
                        scope: this
                    });
                }
            }, this);
            return;
        }
        
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
    
    onDeviceGridClick: function(view, record, item, index, e, eOpts) {
        var target = e.getTarget('.check-device-link');
        if (target) {
            e.stopEvent();
            var deviceId = target.getAttribute('data-id');
            Ext.Msg.confirm('确认安检', '确定要对该设备进行安检吗？', function(btn) {
                if (btn === 'yes') {
                    Ext.Ajax.request({
                        url: 'deviceaction/checkdevice/' + deviceId,
                        method: 'PUT',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        success: function(response, opts) {
                            var obj = Ext.decode(response.responseText);
                            if (obj.success) {
                                Ext.Msg.alert('结果显示', obj.message);
                                var store = Ext.data.StoreMgr.lookup('deviceliststore');
                                store.reload();
                            } else {
                                Ext.Msg.alert('提示', obj.message);
                            }
                        },
                        failure: function(response, opts) {
                            Ext.Msg.alert('安检失败', '设备安检失败');
                        },
                        scope: this
                    });
                }
            }, this);
        }
    },

});