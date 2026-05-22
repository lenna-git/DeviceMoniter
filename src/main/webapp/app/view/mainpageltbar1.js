Ext.define('AM.view.mainpageltbar1', {
    extend: 'Ext.tree.Panel',
    alias : 'widget.mainpageltbar1',
    lines : false,
    
    initComponent: function() {
        var me = this;
        
        var store = Ext.create('Ext.data.TreeStore', {
            root: {
                expanded: true
            },
            proxy: {
                type: 'ajax',
                url: 'app/view/leftlist.json'
            },
            listeners: {
                load: function(store, records, successful, operation, node) {
                    if (!successful) return;
                    
                    var userRole = SYS_USER ? SYS_USER.sysuserrole : 2;
                    
                    if (userRole !== 1) {
                        var rootNode = store.getRootNode();
                        var userFolder = rootNode.findChild('text', '<font size=4>用户</font>', true);
                        
                        if (userFolder) {
                            var userInfoItem = userFolder.findChild('id', 'main-user', true);
                            if (userInfoItem) {
                                userFolder.removeChild(userInfoItem);
                            }
                            
                            // 隐藏日志审计菜单项
                            var logOperationItem = userFolder.findChild('id', 'main-LogOperation', true);
                            if (logOperationItem) {
                                userFolder.removeChild(logOperationItem);
                            }
                        }
                    }
                }
            }
        });
        
        me.store = store;
        me.callParent(arguments);
    }
});
