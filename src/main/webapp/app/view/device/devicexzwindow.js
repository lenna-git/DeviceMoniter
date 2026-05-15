Ext.define('AM.view.device.devicexzwindow',{
    extend: 'Ext.window.Window',
    alias:'widget.devicexzwindow',
    border: true,
    width:600,
    height:700,
    layout:{
        type:'vbox',
        align:'left'
    },
    title: '设备新增',
    isEdit: false,
    
    listeners: {
        afterrender: function(win) {
            var cpuCombo = win.down('combo[name=devcpu_id]');
            var typeCombo = win.down('combo[name=devtype_id]');
            var manufacturerCombo = win.down('combo[name=devmanufacturer_id]');
            var stateCombo = win.down('#devicestateCombo');
            
            var scDateField = win.down('datefield[name=devicescdata]');
            var ajDateField = win.down('datefield[name=deviceajdata]');
            var ghDateField = win.down('datefield[name=deviceghdata]');
            
            var yhField = win.down('#deviceyhField');
            
            var setDefaultValue = function(combo) {
                var store = combo.getStore();
                if (store.getCount() > 0) {
                    combo.setValue(store.first().getId());
                } else {
                    store.on('load', function() {
                        if (store.getCount() > 0) {
                            combo.setValue(store.first().getId());
                        }
                    }, null, { single: true });
                }
            };
            
            setDefaultValue(cpuCombo);
            setDefaultValue(typeCombo);
            setDefaultValue(manufacturerCombo);
            
            if (win.isEdit) {
                stateCombo.setReadOnly(true);
                stateCombo.setFieldStyle('background-color: #f0f0f0;');
                
                scDateField.setReadOnly(true);
                scDateField.setFieldStyle('background-color: #f0f0f0;');
                ajDateField.setReadOnly(true);
                ajDateField.setFieldStyle('background-color: #f0f0f0;');
                ghDateField.setReadOnly(true);
                ghDateField.setFieldStyle('background-color: #f0f0f0;');
                
                yhField.setReadOnly(true);
                yhField.setFieldStyle('background-color: #f0f0f0;');
            } else {
                stateCombo.hide();
                scDateField.hide();
                ajDateField.hide();
                ghDateField.hide();
                yhField.hide();
            }
        }
    },

    items:[{
        xtype: 'textfield',
        fieldLabel:'设备id',
        hidden:true,
        name:'deviceid',
    },{
        xtype: 'combo',
        fieldLabel:'芯片',
        name:'devcpu_id',
        allowBlank:false,
        width:300,
        labelWidth: 80,
        margin: '10 0 10 60',
        store: Ext.create('AM.store.devcpustore'),
        displayField: 'cpuname',
        valueField: 'id',
        queryMode: 'local',
        emptyText: '请选择芯片型号'
    },{
        xtype: 'combo',
        fieldLabel:'类型',
        name:'devtype_id',
        allowBlank:false,
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
        store: Ext.create('AM.store.devtypestore'),
        displayField: 'typename',
        valueField: 'id',
        queryMode: 'local',
        emptyText: '请选择设备类型'
    },{
        xtype: 'textfield',
        fieldLabel:'型号',
        name:'devicexh',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'combo',
        fieldLabel:'厂商',
        name:'devmanufacturer_id',
        allowBlank:false,
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
        store: Ext.create('AM.store.devmanufacturerstore'),
        displayField: 'manufacturername',
        valueField: 'id',
        queryMode: 'local',
        emptyText: '请选择厂商'
    },{
        xtype: 'textfield',
        fieldLabel:'序列号',
        name:'devicesn',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'textfield',
        fieldLabel:'编号',
        name:'deviceno',
        allowBlank:false,//不允许为空
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'datefield',
        fieldLabel:'送测日期',
        name:'devicescdata',
        allowBlank:true,
        width:400,
        labelWidth: 80,
        margin: '0 0 10 60',
        format: 'Y-m-d H:i:s',
        editable: true,
        createPicker: function() {
            var me = this,
                format = Ext.String.format;
            return new Ext.picker.Date({
                pickerField: me,
                ownerCt: me.ownerCt,
                renderTo: document.body,
                floating: true,
                hidden: true,
                focusOnShow: true,
                minDate: me.minValue,
                maxDate: me.maxValue,
                disabledDatesRE: me.disabledDatesRE,
                disabledDatesText: me.disabledDatesText,
                disabledDays: me.disabledDays,
                disabledDaysText: me.disabledDaysText,
                format: me.format,
                showToday: me.showToday,
                startDay: me.startDay,
                minText: format(me.minText, me.formatDate(me.minValue)),
                maxText: format(me.maxText, me.formatDate(me.maxValue)),
                listeners: {
                    scope: me,
                    select: me.onSelect
                },
                keyNavConfig: {
                    esc: function() {
                        me.collapse();
                    }
                }
            });
        }
    },{
        xtype: 'datefield',
        fieldLabel:'安检日期',
        name:'deviceajdata',
        allowBlank:true,
        width:400,
        labelWidth: 80,
        margin: '0 0 10 60',
        format: 'Y-m-d H:i:s',
        editable: true,
        createPicker: function() {
            var me = this,
                format = Ext.String.format;
            return new Ext.picker.Date({
                pickerField: me,
                ownerCt: me.ownerCt,
                renderTo: document.body,
                floating: true,
                hidden: true,
                focusOnShow: true,
                minDate: me.minValue,
                maxDate: me.maxValue,
                disabledDatesRE: me.disabledDatesRE,
                disabledDatesText: me.disabledDatesText,
                disabledDays: me.disabledDays,
                disabledDaysText: me.disabledDaysText,
                format: me.format,
                showToday: me.showToday,
                startDay: me.startDay,
                minText: format(me.minText, me.formatDate(me.minValue)),
                maxText: format(me.maxText, me.formatDate(me.maxValue)),
                listeners: {
                    scope: me,
                    select: me.onSelect
                },
                keyNavConfig: {
                    esc: function() {
                        me.collapse();
                    }
                }
            });
        }
    },{
        xtype: 'datefield',
        fieldLabel:'归还厂商日期',
        name:'deviceghdata',
        allowBlank:true,
        width:400,
        labelWidth: 80,
        margin: '0 0 10 60',
        format: 'Y-m-d H:i:s',
        editable: true,
        createPicker: function() {
            var me = this,
                format = Ext.String.format;
            return new Ext.picker.Date({
                pickerField: me,
                ownerCt: me.ownerCt,
                renderTo: document.body,
                floating: true,
                hidden: true,
                focusOnShow: true,
                minDate: me.minValue,
                maxDate: me.maxValue,
                disabledDatesRE: me.disabledDatesRE,
                disabledDatesText: me.disabledDatesText,
                disabledDays: me.disabledDays,
                disabledDaysText: me.disabledDaysText,
                format: me.format,
                showToday: me.showToday,
                startDay: me.startDay,
                minText: format(me.minText, me.formatDate(me.minValue)),
                maxText: format(me.maxText, me.formatDate(me.maxValue)),
                listeners: {
                    scope: me,
                    select: me.onSelect
                },
                keyNavConfig: {
                    esc: function() {
                        me.collapse();
                    }
                }
            });
        }
    },{
        xtype: 'textfield',
        fieldLabel:'借用人',
        name:'deviceyh',
        itemId: 'deviceyhField',
        allowBlank:true,
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
    },{
        xtype: 'combo',
        fieldLabel:'状态',
        name:'devicestate.id',
        itemId: 'devicestateCombo',
        allowBlank:true,
        width:300,
        labelWidth: 80,
        margin: '0 0 10 60',
        store: Ext.create('AM.store.devicestatestore'),
        displayField: 'stateDetail',
        valueField: 'id',
        queryMode: 'local',
        emptyText: '请选择设备状态'
    },{
        xtype: 'panel',
        layout:'hbox',
        margin: '0 0 0 450',
        items: [{
            xtype: 'button',
            action:'device-save',
            text:'保存',
            height:'80',
            width:'150',
        },{
            xtype: 'button',
            action:'device-quxiao',
            text:'取消',
            height:'80',
            width:'150',
        }]
    }],


});
