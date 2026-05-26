package com.example.demo20250620.controller;

import com.example.demo20250620.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/systemconfig")
public class SystemConfigController {

    @Autowired
    private SystemConfigService systemConfigService;

    @GetMapping("/pageSize")
    public Map<String, Object> getPageSize() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("pageSize", systemConfigService.getPageSize());
        return result;
    }

    @PostMapping("/pageSize")
    public Map<String, Object> setPageSize(@RequestParam("pageSize") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        if (pageSize <= 0) {
            result.put("success", false);
            result.put("message", "分页大小必须大于0");
            return result;
        }
        systemConfigService.setPageSize(pageSize);
        result.put("success", true);
        result.put("message", "分页大小设置成功");
        result.put("pageSize", pageSize);
        return result;
    }
}