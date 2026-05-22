package com.example.demo20250620.service;

import org.springframework.stereotype.Service;

import com.example.demo20250620.handler.DeviceStatusWebSocketHandler;

@Service
public class LogOperationNotificationService {

    private final DeviceStatusWebSocketHandler webSocketHandler;

    public LogOperationNotificationService(DeviceStatusWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    /**
     * 通知所有客户端日志操作已更新
     */
    public void notifyLogOperationUpdate(Long logId, String operationType) {
        webSocketHandler.broadcastLogOperation(logId, operationType);
    }
}