Ext.define('AM.controller.DeviceRecordController', {
    extend: 'Ext.app.Controller',
    init: function() {
        this.control(
            {
            'viewport centerpage DeviceRecordView button[action=select]': {
                click: this.onselectbuttioncick
            },
            'viewport centerpage DeviceRecordView button[action=export]': {
                click: this.onexportbuttonclick
            }
        }
        );
    },

    models:['devicerecord'],
    stores:['deviceliststore'],

    refs:[{
        selector: 'viewport centerpage DeviceRecordView DeviceRecordGrid',
        ref:'DeviceRecordGrid'
    },{
        selector: 'viewport centerpage DeviceRecordView textfield[name=searchKeyword]',
        ref: 'searchKeywordtf'
    }],


    //查询
    onselectbuttioncick:function (){
        console.log('搜索关键词：'+this.getSearchKeywordtf().getValue());
        var store = this.getDeviceRecordGrid().getStore();
        store.getProxy().extraParams.keyword=this.getSearchKeywordtf().getValue();
        store.reload();
    },

    //导出Excel
    onexportbuttonclick:function (){
        var keyword = this.getSearchKeywordtf().getValue();
        var url = 'devicerecord/exportExcel';
        if (keyword && keyword.trim()) {
            url += '?keyword=' + encodeURIComponent(keyword.trim());
        }
        
        // 创建隐藏的iframe来下载文件
        var iframe = document.createElement('iframe');
        iframe.style.display = 'none';
        iframe.src = url;
        document.body.appendChild(iframe);
        
        // 下载完成后移除iframe
        setTimeout(function() {
            document.body.removeChild(iframe);
        }, 10000);
    }
});
