package com.example.demo20250620.controller;

import com.example.demo20250620.entity.SysConfig;
import com.example.demo20250620.repository.SysConfigRepository;
import com.example.demo20250620.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sysconfig")
public class SysConfigController {
    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private SysConfigRepository sysConfigRepository;

    @GetMapping("/sessionTimeout")
    public Map<String, Object> getSessionTimeout() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("timeoutSeconds", sysConfigService.getSessionTimeoutSeconds());
        return response;
    }

    @PutMapping("/sessionTimeout")
    public Map<String, Object> setSessionTimeout(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Integer seconds = (Integer) request.get("timeoutSeconds");
            if (seconds == null || seconds <= 0) {
                response.put("success", false);
                response.put("message", "超时时间必须大于0");
                return response;
            }
            sysConfigService.setSessionTimeoutSeconds(seconds);
            response.put("success", true);
            response.put("message", "会话超时时间设置成功");
            response.put("timeoutSeconds", seconds);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "设置失败: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/all")
    public Map<String, Object> getAllConfigs() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<SysConfig> configs = sysConfigRepository.findAll();
            response.put("success", true);
            response.put("data", configs);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取配置失败: " + e.getMessage());
        }
        return response;
    }

    @PutMapping("/{key}")
    public Map<String, Object> updateConfig(@PathVariable String key, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String value = (String) request.get("value");
            String description = (String) request.get("description");
            sysConfigService.setConfigValue(key, value, description);
            response.put("success", true);
            response.put("message", "配置更新成功");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新失败: " + e.getMessage());
        }
        return response;
    }
}
