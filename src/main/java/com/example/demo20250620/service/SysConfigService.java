package com.example.demo20250620.service;

import com.example.demo20250620.entity.SysConfig;
import com.example.demo20250620.repository.SysConfigRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysConfigService {
    @Autowired
    private SysConfigRepository sysConfigRepository;

    public static final String SESSION_TIMEOUT_SECONDS_KEY = "session.timeout.seconds";
    public static final int DEFAULT_SESSION_TIMEOUT_SECONDS = 180;

    @PostConstruct
    public void init() {
        if (sysConfigRepository.findByConfigKey(SESSION_TIMEOUT_SECONDS_KEY).isEmpty()) {
            SysConfig config = new SysConfig(SESSION_TIMEOUT_SECONDS_KEY, 
                    String.valueOf(DEFAULT_SESSION_TIMEOUT_SECONDS), 
                    "会话超时时间（秒）");
            sysConfigRepository.save(config);
        }
    }

    public int getSessionTimeoutSeconds() {
        return sysConfigRepository.findByConfigKey(SESSION_TIMEOUT_SECONDS_KEY)
                .map(config -> {
                    try {
                        return Integer.parseInt(config.getConfigValue());
                    } catch (NumberFormatException e) {
                        return DEFAULT_SESSION_TIMEOUT_SECONDS;
                    }
                })
                .orElse(DEFAULT_SESSION_TIMEOUT_SECONDS);
    }

    public void setSessionTimeoutSeconds(int seconds) {
        SysConfig config = sysConfigRepository.findByConfigKey(SESSION_TIMEOUT_SECONDS_KEY)
                .orElse(new SysConfig(SESSION_TIMEOUT_SECONDS_KEY, String.valueOf(seconds), "会话超时时间（秒）"));
        config.setConfigValue(String.valueOf(seconds));
        sysConfigRepository.save(config);
    }

    public String getConfigValue(String key) {
        return sysConfigRepository.findByConfigKey(key)
                .map(SysConfig::getConfigValue)
                .orElse(null);
    }

    public void setConfigValue(String key, String value, String description) {
        SysConfig config = sysConfigRepository.findByConfigKey(key)
                .orElse(new SysConfig(key, value, description));
        config.setConfigValue(value);
        if (description != null) {
            config.setDescription(description);
        }
        sysConfigRepository.save(config);
    }
}
