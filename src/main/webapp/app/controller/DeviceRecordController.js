Ext.define('AM.controller.DeviceRecordController', {
    extend: 'Ext.app.Controller',
    init: function() {
        this.control(
            {
            'viewport > DeviceRecordView > DeviceRecordGrid': {
                itemDblClick: this.onItemDblClick1,  //双击修改
                selectionchange:this.onselectchange,
                onCellDblClick1:this.onCellDblClick1,
            },
            'viewport centerpage DeviceRecordView DeviceRecordGrid':{
                itemdblclick: this.onDbClickForXG,
            },
            'viewport centerpage DeviceRecordView button[action=xj]': {
                click: this.onxjbuttioncick
            },
            'viewport centerpage DeviceRecordView button[action=sc]': {
                click: this.onscbuttioncick
            },
            'viewport centerpage DeviceRecordView button[action=update]': {
                click: this.onupdatebuttioncick
            },
            'viewport centerpage DeviceRecordView button[action=select]': {
                click: this.onselectbuttioncick
            }
        }
        );
    },

    models:['DeviceRecordModel'],
    stores:['DeviceRecordStore'],

    refs:[{
        selector: 'viewport centerpage DeviceRecordView DeviceRecordGrid',
        ref:'DeviceRecordGrid'
    },{
        selector: 'viewport centerpage DeviceRecordView textfield[name=searchUserId]',
        ref: 'searchUserIdtf'
    },{
        selector: 'viewport centerpage DeviceRecordView textfield[name=searchDetail]',
        ref: 'searchDetailtf'
    },

    {
        selector: 'viewport centerpage DeviceRecordView DeviceRecordGrid xinjianyonghupanel textfield[name=userName]',
        ref: 'userNametf'
    },
    {
        selector: 'xinjianyonghupanel textfield[name=xinpian]',
        ref: 'xinpiantf'
    },
    {
        selector: 'xinjianyonghupanel textfield[name=leixing]',
        ref: 'leixingtf'
    },
    {
        selector: 'xinjianyonghupanel textfield[name=xinghao]',
        ref: 'xinghaotf'
    },
    {
        selector: 'xinjianyonghupanel textfield[name=changshang]',
        ref: 'changshangtf'
    },
    {
        selector: 'xinjianyonghupanel textfield[name=userId]',
        ref: 'userIdtf'
    },{
        selector: 'xinjianyonghupanel textfield[name=device.deviceno]',
        ref: 'deviceIdtf'
    },
    {
        selector: 'xinjianyonghupanel textfield[name=borrorDate]',
        ref: 'borrorDatetf'
    },{
        selector: 'xinjianyonghupanel textfield[name=returnDate]',
        ref: 'returnDatetf'
    }],



    //查询
    onselectbuttioncick:function (){
        console.log('搜索用户id：'+this.getSearchUserIdtf().getValue()+'搜索Detail：'+this.getSearchDetailtf().getValue());
        var store = this.getDeviceRecordGrid().getStore();
        store.getProxy().extraParams.userId=this.getSearchUserIdtf().getValue();
        store.getProxy().extraParams.detail=this.getSearchDetailtf().getValue();
        store.reload();
    },


    //新增
    onxjbuttioncick:function (){
        var xinjianyonghujiluwindow = Ext.widget({
            xtype: 'xinjianyonghujiluwindow'
        });
        xinjianyonghujiluwindow.show();
    },
    //删除
    onscbuttioncick:function (){

        var sr = this.getDeviceRecordGrid().getSelectionModel().getSelection();

        var idx = sr[0].get('id');
        console.log('--------------'+idx);

        Ext.Ajax.request({
            url:'devicerecord/delDeviceRecords/'+idx,
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
        this.getDeviceRecordGrid().getStore().reload();

    },

    //双击开始修改
    onDbClickForXG:function (param1,record){
        //获取
        var xinjianyonghujiluwindow = Ext.widget({
            xtype: 'xinjianyonghujiluwindow'
        });

        //回显连表数据
        // this.getUserNametf().setValue();
        // this.getXinpiantf().setValue();
        // this.getLeixingtf().setValue();
        // this.getXinghaotf().setValue();
        // this.getChangshangtf().setValue();

        //回显数据
        this.getIdtf().setValue(record.get('id'));
        this.getUserIdtf().setValue(record.get('userId'));
        this.getDeviceIdtf().setValue(record.get('device.deviceno'));
        this.getDetailtf().setValue(record.get('detail'));
        this.getBorrorDatetf().setValue(record.get('borrorDate'));
        this.getReturnDatetf().setValue(record.get('returnDate'));
        //展示
        xinjianyonghujiluwindow.show();

    },

    //单击修改
    onupdatebuttioncick:function (){
        var DeviceRecordGrid = this.getDeviceRecordGrid();
        var SelectionModel = DeviceRecordGrid.getSelectionModel();
        var Selection = SelectionModel.getSelection();
        var idx = Selection[0].get('id');
        console.log('点到的修改id：'+idx);


        return;


        // var rec ={
        //     userName:'888',
        //     deviceId:'999',
        //     detail:'ttt'
        // }
        //
        // //将id为122的记录修改为上面rec指定的值
        // Ext.Ajax.request({
        //     url:'devicerecord/updateDeviceRecordById/'+"4",
        //     method:'PUT',
        //     jsonData:rec,
        //     headers: {
        //         'Content-Type': 'application/json'
        //     },
        //     sucess:function(response,opts){
        //         var obj = Ext.decode(response.responseText);
        //         if(obj.sucess){
        //             Ext.Msg.alert('结果显示',obj.message);
        //         }
        //
        //     },
        //     failure:function(response,opts){
        //         var obj = Ext.decode(response.responseText);
        //         Ext.Msg.alert('保存错误','错误原因：'+obj.message+"-------"+obj.msg);
        //     }
        // })
        //
        // location.reload()
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
    }
});