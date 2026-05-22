package com.example.demo20250620.handler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class DeviceStatusWebSocketHandler extends TextWebSocketHandler {

    private static final CopyOnWriteArraySet<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("=== WebSocket连接建立 ===");
        System.out.println("会话ID: " + session.getId());
        System.out.println("客户端地址: " + session.getRemoteAddress());
        System.out.println("当前连接总数: " + sessions.size());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("=== WebSocket连接关闭 ===");
        System.out.println("会话ID: " + session.getId());
        System.out.println("关闭代码: " + status.getCode());
        System.out.println("关闭原因: " + status.getReason());
        System.out.println("当前连接总数: " + sessions.size());
    }

    /**
     * 向所有客户端发送设备状态更新通知
     */
    public void broadcastDeviceStatusUpdate(Long deviceId) {
        System.out.println("=== 开始广播设备状态更新 ===");
        System.out.println("设备ID: " + deviceId);
        System.out.println("当前连接数: " + sessions.size());
        
        String message = "{\"type\": \"DEVICE_STATUS_UPDATE\", \"deviceId\": " + deviceId + "}";
        int successCount = 0;
        int failCount = 0;
        
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                    successCount++;
                    System.out.println("成功发送到会话: " + session.getId());
                } catch (IOException e) {
                    failCount++;
                    System.out.println("发送失败到会话: " + session.getId() + ", 错误: " + e.getMessage());
                }
            } else {
                failCount++;
                System.out.println("会话已关闭: " + session.getId());
            }
        }
        
        System.out.println("=== 广播完成 ===");
        System.out.println("成功发送: " + successCount + " 个客户端");
        System.out.println("发送失败: " + failCount + " 个客户端");
    }

    /**
     * 获取当前连接数
     */
    public int getConnectedClients() {
        return sessions.size();
    }
}
