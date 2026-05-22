package com.example.demo20250620.service;

import com.example.demo20250620.entity.LogOperation;
import com.example.demo20250620.repository.LogOperationRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LogOperationService {

    @Autowired
    private LogOperationRepository logOperationRepository;

    /**
     * 记录操作日志
     */
    @Async
    public void logOperation(Long operatorId, String operatorName, Integer operatorRole,
                             String operationType, String operationModule, String operationDescription,
                             String operationResult, String targetType, Long targetId, String targetName,
                             String detail, HttpServletRequest request) {
        LogOperation log = new LogOperation();
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setOperatorRole(operatorRole);
        log.setOperationType(operationType);
        log.setOperationModule(operationModule);
        log.setOperationDescription(operationDescription);
        log.setOperationResult(operationResult);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setTargetName(targetName);
        log.setDetail(detail);
        
        // 获取客户端IP
        if (request != null) {
            log.setIpAddress(getClientIp(request));
            log.setUserAgent(request.getHeader("User-Agent"));
        }
        
        logOperationRepository.save(log);
    }

    /**
     * 记录成功操作日志（简化版）
     */
    @Async
    public void logSuccess(Long operatorId, String operatorName, Integer operatorRole,
                           String operationType, String operationModule, String operationDescription,
                           String targetType, Long targetId, String targetName, HttpServletRequest request) {
        logOperation(operatorId, operatorName, operatorRole, operationType, operationModule,
                operationDescription, LogOperation.RESULT_SUCCESS, targetType, targetId, targetName, null, request);
    }

    /**
     * 记录失败操作日志
     */
    @Async
    public void logFail(Long operatorId, String operatorName, Integer operatorRole,
                        String operationType, String operationModule, String operationDescription,
                        String targetType, Long targetId, String targetName, String errorMessage,
                        HttpServletRequest request) {
        LogOperation log = new LogOperation();
        log.setOperatorId(operatorId);
        log.setOperatorName(operatorName);
        log.setOperatorRole(operatorRole);
        log.setOperationType(operationType);
        log.setOperationModule(operationModule);
        log.setOperationDescription(operationDescription);
        log.setOperationResult(LogOperation.RESULT_FAIL);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        log.setTargetName(targetName);
        log.setErrorMessage(errorMessage);
        
        if (request != null) {
            log.setIpAddress(getClientIp(request));
            log.setUserAgent(request.getHeader("User-Agent"));
        }
        
        logOperationRepository.save(log);
    }

    /**
     * 分页查询日志
     */
    public Page<LogOperation> findLogsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return logOperationRepository.findAll(pageable);
    }

    /**
     * 多条件查询日志
     */
    public Page<LogOperation> findLogsByConditions(Long operatorId, String operatorName,
                                                    String operationType, String operationModule, String operationResult,
                                                    LocalDateTime startTime, LocalDateTime endTime,
                                                    String targetType, Long targetId,
                                                    int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return logOperationRepository.findByMultipleConditions(operatorId, operatorName, operationType, operationModule,
                operationResult, startTime, endTime, targetType, targetId, pageable);
    }

    /**
     * 根据用户ID查询日志
     */
    public Page<LogOperation> findLogsByOperatorId(Long operatorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "operationTime"));
        return logOperationRepository.findByOperatorId(operatorId, pageable);
    }

    /**
     * 获取最近的日志
     */
    public List<LogOperation> getRecentLogs() {
        return logOperationRepository.findTop10ByOrderByOperationTimeDesc();
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 获取操作统计
     */
    public Map<String, Object> getOperationStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 统计各操作类型的数量
        List<Object[]> typeCounts = logOperationRepository.countByOperationTypeInTimeRange(startTime, endTime);
        Map<String, Long> typeCountMap = new HashMap<>();
        for (Object[] row : typeCounts) {
            typeCountMap.put((String) row[0], (Long) row[1]);
        }
        statistics.put("typeCounts", typeCountMap);
        
        return statistics;
    }
}