package com.example.demo20250620.controller;

import com.example.demo20250620.entity.LogOperation;
import com.example.demo20250620.service.LogOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logoperation/")
public class LogOperationController {

    @Autowired
    private LogOperationService logOperationService;

    /**
     * 分页查询操作日志
     */
    @GetMapping("/list")
    public Map<String, Object> getLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Map<String, Object> response = new HashMap<>();
        Page<LogOperation> logPage = logOperationService.findLogsByPage(page, size);
        response.put("total", logPage.getTotalElements());
        response.put("data", logPage.getContent());
        return response;
    }

    /**
     * 多条件查询操作日志
     */
    @GetMapping("/search")
    public Map<String, Object> searchLogs(
            @RequestParam(required = false) Long operatorId,
            @RequestParam(required = false) String operatorName,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String operationModule,
            @RequestParam(required = false) String operationResult,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(required = false) String targetType,
            @RequestParam(required = false) Long targetId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        // ExtJS 默认从1开始，转换为从0开始
        int pageIndex = page - 1;
        Map<String, Object> response = new HashMap<>();
        Page<LogOperation> logPage = logOperationService.findLogsByConditions(
                operatorId, operatorName, operationType, operationModule, operationResult,
                startTime, endTime, targetType, targetId, pageIndex, limit);
        response.put("total", logPage.getTotalElements());
        response.put("data", logPage.getContent());
        return response;
    }

    /**
     * 根据用户ID查询操作日志
     */
    @GetMapping("/byuser/{userId}")
    public Map<String, Object> getLogsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Map<String, Object> response = new HashMap<>();
        Page<LogOperation> logPage = logOperationService.findLogsByOperatorId(userId, page, size);
        response.put("total", logPage.getTotalElements());
        response.put("data", logPage.getContent());
        return response;
    }

    /**
     * 获取最近的操作日志
     */
    @GetMapping("/recent")
    public List<LogOperation> getRecentLogs() {
        return logOperationService.getRecentLogs();
    }

    /**
     * 获取操作统计
     */
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return logOperationService.getOperationStatistics(startTime, endTime);
    }

    /**
     * 获取操作类型列表（用于前端下拉框）
     */
    @GetMapping("/types")
    public List<String> getOperationTypes() {
        return List.of(
                LogOperation.TYPE_DEVICE_CHECK,
                LogOperation.TYPE_DEVICE_RETURN,
                LogOperation.TYPE_DEVICE_REPAIR,
                LogOperation.TYPE_DEVICE_UNSHELVE,
                LogOperation.TYPE_DEVICE_BORROW,
                LogOperation.TYPE_DEVICE_TRANSFER,
                LogOperation.TYPE_REPAIR_APPLY,
                LogOperation.TYPE_REPAIR_CONFIRM,
                LogOperation.TYPE_REPAIR_FINISH,
                LogOperation.TYPE_TRANSFER_APPLY,
                LogOperation.TYPE_TRANSFER_USER_APPROVE,
                LogOperation.TYPE_TRANSFER_ADMIN_APPROVE,
                LogOperation.TYPE_BORROW_APPROVE
        );
    }

    /**
     * 获取操作模块列表
     */
    @GetMapping("/modules")
    public List<String> getOperationModules() {
        return List.of(
                LogOperation.MODULE_DEVICE,
                LogOperation.MODULE_BORROW,
                LogOperation.MODULE_REPAIR,
                LogOperation.MODULE_TRANSFER
        );
    }

    /**
     * 获取目标类型列表
     */
    @GetMapping("/targettypes")
    public List<String> getTargetTypes() {
        return List.of(
                LogOperation.TARGET_DEVICE,
                LogOperation.TARGET_BORROW_RECORD,
                LogOperation.TARGET_REPAIR_RECORD,
                LogOperation.TARGET_TRANSFER_RECORD
        );
    }
}