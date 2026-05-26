package com.example.demo20250620.service;

import com.example.demo20250620.entity.SystemConfig;
import com.example.demo20250620.repository.SystemConfigRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigService {

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    private static final String PAGE_SIZE_KEY = "page_size";
    private static final int DEFAULT_PAGE_SIZE = 20;

    @PostConstruct
    public void init() {
        // 如果数据库中没有page_size配置，创建默认配置
        if (systemConfigRepository.findByConfigKey(PAGE_SIZE_KEY).isEmpty()) {
            SystemConfig config = new SystemConfig();
            config.setConfigKey(PAGE_SIZE_KEY);
            config.setConfigValue(String.valueOf(DEFAULT_PAGE_SIZE));
            config.setConfigDesc("默认分页大小");
            systemConfigRepository.save(config);
        }
    }

    public int getPageSize() {
        return systemConfigRepository.findByConfigKey(PAGE_SIZE_KEY)
                .map(config -> {
                    try {
                        return Integer.parseInt(config.getConfigValue());
                    } catch (NumberFormatException e) {
                        return DEFAULT_PAGE_SIZE;
                    }
                })
                .orElse(DEFAULT_PAGE_SIZE);
    }

    public void setPageSize(int pageSize) {
        SystemConfig config = systemConfigRepository.findByConfigKey(PAGE_SIZE_KEY)
                .orElse(new SystemConfig());
        config.setConfigKey(PAGE_SIZE_KEY);
        config.setConfigValue(String.valueOf(pageSize));
        config.setConfigDesc("默认分页大小");
        systemConfigRepository.save(config);
    }

    public String getConfig(String key) {
        return systemConfigRepository.findByConfigKey(key)
                .map(SystemConfig::getConfigValue)
                .orElse(null);
    }

    public void setConfig(String key, String value, String desc) {
        SystemConfig config = systemConfigRepository.findByConfigKey(key)
                .orElse(new SystemConfig());
        config.setConfigKey(key);
        config.setConfigValue(value);
        config.setConfigDesc(desc);
        systemConfigRepository.save(config);
    }
}