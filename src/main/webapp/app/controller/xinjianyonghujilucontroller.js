Ext.define('AM.controller.xinjianyonghujilucontroller', {
    extend: 'Ext.app.Controller',
    init: function () {
        this.control({
            'xinjianyonghujiluwindow button[action=qd]': {
                click: this.onBaocun
            },
            'xinjianyonghujiluwindow button[action=qx]': {
                click: this.onQuxiao
            },
        });
    },
    refs: [{
        selector: 'xinjianyonghujiluwindow',
        ref: 'xinjianyonghujiluwindow'
    },{
        selector: 'viewport centerpage DeviceRecordView DeviceRecordGrid',
        ref:'DeviceRecordGrid'
    },{
        selector: 'viewport centerpage DeviceRecordView textfield[name=search]',
        ref: 'searchtf'
    },{
        selector: 'xinjianyonghupanel textfield[name=id]',
        ref: 'idtf'
    },{
        selector: 'xinjianyonghupanel textfield[name=userId]',
        ref: 'userIdtf'
    },{
        selector: 'xinjianyonghupanel textfield[name=device.deviceno]',
        ref: 'devicenotf'
    },{
        selector: 'xinjianyonghupanel textfield[name=detail]',
        ref: 'detailtf'
    },{
        selector: 'xinjianyonghupanel textfield[name=borrorDate]',
        ref: 'borrorDatetf'
    },{
        selector: 'xinjianyonghupanel textfield[name=returnDate]',
        ref: 'returnDatetf'
    }],

    //取消用户信息到数据库。
    onQuxiao: function () {
        this.getXinjianyonghujiluwindow().close();
    },

    //保存用户信息到数据库。
    onBaocun: function () {
        var id = this.getIdtf().getValue();
        var userIdVal = this.getUserIdtf().getValue();
        var devicenoVal = this.getDevicenotf().getValue();
        var detailVal = this.getDetailtf().getValue();
        var borrorDateVal = this.getBorrorDatetf().getValue();
        var returnDateVal = this.getReturnDatetf().getValue();
        var rec ={
            userId: userIdVal,
            deviceno: devicenoVal,
            detail: detailVal,
            borrorDate:borrorDateVal,
            returnDate:returnDateVal
        }
        console.log('---------'+id);
        if(id!=null&&id!=''){
            console.log('修改')
            Ext.Ajax.request({
                url: 'devicerecord/updateDeviceRecordById/'+id,
                method: 'put',
                jsonData:rec,
                success: function (response, opts) {
                    Ext.Msg.alert('保存成功');
                },
                failure: function (response, opts) {
                    Ext.Msg.alert('保存错误');
                }
            });
        }else{//id为空，新增
            console.log('新增')
            Ext.Ajax.request({
                url: 'devicerecord/createDeviceRecord',
                method: 'post',
                jsonData:rec,
                success: function (response, opts) {
                    Ext.Msg.alert('保存成功');
                },
                failure: function (response, opts) {
                    Ext.Msg.alert('保存错误');
                }
            });
        }
        this.getDeviceRecordGrid().getStore().reload();
    },

});