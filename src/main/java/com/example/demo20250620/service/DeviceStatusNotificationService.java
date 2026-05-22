package com.example.demo20250620.service;

import org.springframework.stereotype.Service;

import com.example.demo20250620.handler.DeviceStatusWebSocketHandler;

@Service
public class DeviceStatusNotificationService {

    private final DeviceStatusWebSocketHandler webSocketHandler;

    public DeviceStatusNotificationService(DeviceStatusWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    /**
     * 通知所有客户端设备状态已更新
     */
    public void notifyDeviceStatusUpdate(Long deviceId) {
        webSocketHandler.broadcastDeviceStatusUpdate(deviceId);
    }
}
