package com.example.demo20250620.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.demo20250620.handler.DeviceStatusWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DeviceStatusWebSocketHandler deviceStatusWebSocketHandler;

    public WebSocketConfig(DeviceStatusWebSocketHandler deviceStatusWebSocketHandler) {
        this.deviceStatusWebSocketHandler = deviceStatusWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(deviceStatusWebSocketHandler, "/ws/device-status")
                .setAllowedOrigins("*");
    }
}
