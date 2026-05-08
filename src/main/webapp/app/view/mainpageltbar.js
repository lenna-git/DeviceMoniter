Ext.define('AM.view.mainpageltbar', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.mainpageltbar',
    layout: {
        type: 'vbox',
        align: 'center'
    },
    // autoScroll: true,
    items: [
        {
            xtype: 'tbspacer',
            flex: 1
        }, {
            xtype: 'panel',
            // name: 'dbqshxpanel',
            border: false,
            width: '100%',
            layout: {
                type: 'vbox',
                align: 'center'
            },
            items: [
                {
                    xtype: 'tbspacer',
                    height: 80
                }, {
                    xtype: 'button',
                    width: 70,
                    height: 70,
                    border: false,
                    action: 'showuser',

                }, {
                    xtype: 'label',
                    height: 20,
                    text: '用户',
                }, {
                    xtype: 'tbspacer',
                    height: 80
                }]

        }, {
            xtype: 'tbspacer',
            flex: 1,
        }, {
            xtype: 'panel',
            // name: 'dbqszxpanel',
            border: false,
            width: '100%',
            layout: {
                type: 'vbox',
                align: 'center'
            },
            items: [
                {
                    xtype: 'tbspacer',
                    height: 80
                }, {
                    xtype: 'button',
                    width: 70,
                    height: 70,
                    border: false,
                    action: 'showdevices',
                }, {
                    xtype: 'label',
                    height: 20,
                    text: '设备',
                }, {
                    xtype: 'tbspacer',
                    height: 80
                }]

        }, {
            xtype: 'tbspacer',
            flex: 1
        }
    ]
});