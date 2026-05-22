Ext.define('AM.model.LogOperationModel', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id', type: 'int' },
        { name: 'operationTime', type: 'string' },
        { name: 'operatorId', type: 'int' },
        { name: 'operatorName', type: 'string' },
        { name: 'operatorRole', type: 'int' },
        { name: 'operationType', type: 'string' },
        { name: 'operationModule', type: 'string' },
        { name: 'operationDescription', type: 'string' },
        { name: 'operationResult', type: 'string' },
        { name: 'targetType', type: 'string' },
        { name: 'targetId', type: 'int' },
        { name: 'targetName', type: 'string' },
        { name: 'detail', type: 'string' },
        { name: 'ipAddress', type: 'string' },
        { name: 'userAgent', type: 'string' },
        { name: 'errorMessage', type: 'string' }
    ]
});