package com.example.demo20250620.controller;

import com.example.demo20250620.entity.*;
import com.example.demo20250620.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/transfer/")
public class DeviceTransferController {

    @Autowired
    private DeviceTransferRecordRepository transferRecordRepository;
    
    @Autowired
    private DeviceRepository deviceRepository;
    
    @Autowired
    private SysUserRepository sysUserRepository;
    
    @Autowired
    private DevicestateRepository devicestateRepository;

    // 状态常量
    private static final int STATUS_PENDING = 1;      // 申请中
    private static final int STATUS_USER_APPROVED = 2; // 新借用人已同意
    private static final int STATUS_ADMIN_APPROVED = 3; // 管理员已同意
    private static final int STATUS_REJECTED = 4;      // 已拒绝

    /**
     * 操作员申请转借
     */
    @PostMapping("/apply")
    public Map<String, Object> applyTransfer(@RequestBody Map<String, Object> request) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Long deviceId = Long.parseLong(request.get("deviceId").toString());
            Long fromUserId = Long.parseLong(request.get("fromUserId").toString());
            Long toUserId = Long.parseLong(request.get("toUserId").toString());
            
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            
            Device device = deviceOpt.get();
            
            // 验证当前借用人是否是申请人
            if (device.getDeviceyh() == null || !device.getDeviceyh().getId().equals(fromUserId)) {
                responseObj.put("success", false);
                responseObj.put("message", "只有当前借用人才能申请转借");
                return responseObj;
            }
            
            Optional<SysUser> fromUserOpt = sysUserRepository.findById(fromUserId);
            Optional<SysUser> toUserOpt = sysUserRepository.findById(toUserId);
            
            if (!fromUserOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "原借用人不存在");
                return responseObj;
            }
            
            if (!toUserOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "新借用人不存在");
                return responseObj;
            }
            
            // 创建转借记录
            DeviceTransferRecord record = new DeviceTransferRecord();
            record.setDevice(device);
            record.setFromUser(fromUserOpt.get());
            record.setToUser(toUserOpt.get());
            record.setTransferDate(LocalDateTime.now());
            record.setStatus(STATUS_PENDING);
            
            transferRecordRepository.save(record);
            
            // 更新设备状态为"转借中待转借人通过"(ID=7)
            Optional<Devicestate> stateOpt = devicestateRepository.findById(7L);
            if (stateOpt.isPresent()) {
                device.setDevicestate(stateOpt.get());
                device.setTransferTargetId(toUserId);
                deviceRepository.save(device);
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "转借申请已提交，等待新借用人确认");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "转借申请失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 新借用人同意转借
     */
    @PostMapping("/userApprove")
    public Map<String, Object> userApproveTransfer(@RequestBody Map<String, Object> request) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Long transferId = Long.parseLong(request.get("transferId").toString());
            Long userId = Long.parseLong(request.get("userId").toString());
            
            Optional<DeviceTransferRecord> recordOpt = transferRecordRepository.findById(transferId);
            if (!recordOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "转借记录不存在");
                return responseObj;
            }
            
            DeviceTransferRecord record = recordOpt.get();
            
            // 验证当前用户是否是被转借人
            if (!record.getToUser().getId().equals(userId)) {
                responseObj.put("success", false);
                responseObj.put("message", "您不是转借目标用户，无法同意");
                return responseObj;
            }
            
            // 验证状态
            if (record.getStatus() != STATUS_PENDING) {
                responseObj.put("success", false);
                responseObj.put("message", "当前状态不允许此操作");
                return responseObj;
            }
            
            record.setApprovalDate(LocalDateTime.now());
            record.setStatus(STATUS_USER_APPROVED);
            transferRecordRepository.save(record);
            
            // 更新设备状态为"转借中待管理员批准"(ID=11)
            Optional<Devicestate> stateOpt = devicestateRepository.findById(11L);
            if (stateOpt.isPresent()) {
                record.getDevice().setDevicestate(stateOpt.get());
                deviceRepository.save(record.getDevice());
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "已同意转借，请等待管理员批准");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "同意转借失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 管理员批准转借
     */
    @PostMapping("/adminApprove")
    public Map<String, Object> adminApproveTransfer(@RequestBody Map<String, Object> request) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Long transferId = Long.parseLong(request.get("transferId").toString());
            Long adminId = Long.parseLong(request.get("adminId").toString());
            
            Optional<DeviceTransferRecord> recordOpt = transferRecordRepository.findById(transferId);
            if (!recordOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "转借记录不存在");
                return responseObj;
            }
            
            DeviceTransferRecord record = recordOpt.get();
            
            // 验证状态
            if (record.getStatus() != STATUS_USER_APPROVED) {
                responseObj.put("success", false);
                responseObj.put("message", "当前状态不允许此操作");
                return responseObj;
            }
            
            Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
            if (!adminOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "管理员不存在");
                return responseObj;
            }
            
            record.setAdminApprovalUser(adminOpt.get());
            record.setAdminApprovalDate(LocalDateTime.now());
            record.setStatus(STATUS_ADMIN_APPROVED);
            transferRecordRepository.save(record);
            
            // 更新设备借用人和状态
            Device device = record.getDevice();
            device.setDeviceyh(record.getToUser());
            device.setTransferTargetId(null);
            
            Optional<Devicestate> stateOpt = devicestateRepository.findById(4L); // 借用中
            if (stateOpt.isPresent()) {
                device.setDevicestate(stateOpt.get());
            }
            deviceRepository.save(device);
            
            responseObj.put("success", true);
            responseObj.put("message", "转借已批准，设备状态已更新");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "管理员批准失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 拒绝转借（新借用人或管理员都可以拒绝）
     */
    @PostMapping("/reject")
    public Map<String, Object> rejectTransfer(@RequestBody Map<String, Object> request) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Long transferId = Long.parseLong(request.get("transferId").toString());
            Long userId = Long.parseLong(request.get("userId").toString());
            String reason = (String) request.getOrDefault("reason", "");
            
            Optional<DeviceTransferRecord> recordOpt = transferRecordRepository.findById(transferId);
            if (!recordOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "转借记录不存在");
                return responseObj;
            }
            
            DeviceTransferRecord record = recordOpt.get();
            
            // 验证当前用户是否有权限拒绝
            // 状态为申请中时，只有被转借人可以拒绝
            // 状态为已同意时，管理员可以拒绝
            boolean hasPermission = false;
            
            if (record.getStatus() == STATUS_PENDING && record.getToUser().getId().equals(userId)) {
                hasPermission = true;
            } else if (record.getStatus() == STATUS_USER_APPROVED) {
                hasPermission = true;
            }
            
            if (!hasPermission) {
                responseObj.put("success", false);
                responseObj.put("message", "您无权拒绝此转借申请");
                return responseObj;
            }
            
            record.setDetail(reason);
            
            // 判断拒绝时的状态，如果是被借用人拒绝，记录approvalDate；如果是管理员拒绝，记录adminApprovalUser和adminApprovalDate
            if (record.getStatus() == STATUS_PENDING) {
                record.setApprovalDate(LocalDateTime.now());
            } else if (record.getStatus() == STATUS_USER_APPROVED) {
                Optional<SysUser> adminOpt = sysUserRepository.findById(userId);
                if (adminOpt.isPresent()) {
                    record.setAdminApprovalUser(adminOpt.get());
                    record.setAdminApprovalDate(LocalDateTime.now());
                }
            }
            record.setStatus(STATUS_REJECTED);
            
            transferRecordRepository.save(record);
            
            // 恢复设备状态为"借用中"
            Device device = record.getDevice();
            device.setTransferTargetId(null);
            
            Optional<Devicestate> stateOpt = devicestateRepository.findById(4L); // 借用中
            if (stateOpt.isPresent()) {
                device.setDevicestate(stateOpt.get());
            }
            deviceRepository.save(device);
            
            responseObj.put("success", true);
            responseObj.put("message", "转借已拒绝");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "拒绝转借失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 获取用户待处理的转借申请
     */
    @GetMapping("/pendingForUser/{userId}")
    public List<DeviceTransferRecord> getPendingForUser(@PathVariable Long userId) {
        return transferRecordRepository.findByToUserIdAndStatus(userId, STATUS_PENDING);
    }

    /**
     * 获取管理员待批准的转借申请
     */
    @GetMapping("/pendingForAdmin")
    public List<DeviceTransferRecord> getPendingForAdmin() {
        return transferRecordRepository.findByStatus(STATUS_USER_APPROVED);
    }

    /**
     * 获取设备的转借历史
     */
    @GetMapping("/history/{deviceId}")
    public List<DeviceTransferRecord> getTransferHistory(@PathVariable Long deviceId) {
        return transferRecordRepository.findByDeviceId(deviceId);
    }
}