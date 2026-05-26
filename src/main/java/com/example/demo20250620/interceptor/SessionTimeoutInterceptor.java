package com.example.demo20250620.interceptor;

import com.example.demo20250620.entity.LogOperation;
import com.example.demo20250620.entity.SysUser;
import com.example.demo20250620.service.LogOperationService;
import com.example.demo20250620.service.SysConfigService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class SessionTimeoutInterceptor implements HandlerInterceptor {

    @Autowired(required = false)
    private LogOperationService logOperationService;

    @Autowired(required = false)
    private SysConfigService sysConfigService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // 获取上次访问时间
            Long lastAccessTime = (Long) session.getAttribute("LAST_ACCESS_TIME");
            long currentTime = System.currentTimeMillis();
            
            // 更新访问时间
            session.setAttribute("LAST_ACCESS_TIME", currentTime);
            
            // 从数据库获取超时时间（毫秒）
            long sessionTimeoutMs = getSessionTimeoutMs();
            
            // 检查是否超时
            if (lastAccessTime != null && (currentTime - lastAccessTime) > sessionTimeoutMs) {
                // 记录超时日志
                Optional<SysUser> currentUser = (Optional<SysUser>) session.getAttribute("SYS_USER");
                if (currentUser.isPresent() && logOperationService != null) {
                    logOperationService.logFail(
                            currentUser.get().getId(),
                            currentUser.get().getSysusername(),
                            currentUser.get().getSysuserrole().intValue(),
                            LogOperation.TYPE_SESSION_TIMEOUT,
                            LogOperation.MODULE_SYSTEM,
                            "用户会话超时被强制退出",
                            LogOperation.TARGET_USER,
                            currentUser.get().getId(),
                            currentUser.get().getSysusername(),
                            "会话超时",
                            request);
                }
                
                // 使session失效
                session.invalidate();
                
                // 返回超时响应
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"success\":false,\"message\":\"会话超时，请重新登录\"}");
                return false;
            }
        }
        
        return true;
    }

    private long getSessionTimeoutMs() {
        if (sysConfigService != null) {
            return (long) sysConfigService.getSessionTimeoutSeconds() * 1000;
        }
        // 默认30分钟
        return 1800000L;
    }
}
