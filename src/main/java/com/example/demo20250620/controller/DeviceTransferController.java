package com.example.demo20250620.controller;

import com.example.demo20250620.entity.*;
import com.example.demo20250620.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
    @Autowired
    private com.example.demo20250620.service.DeviceStatusNotificationService deviceStatusNotificationService;
    
    @Autowired
    private com.example.demo20250620.service.LogOperationService logOperationService;

    // 状态常量
    private static final int STATUS_PENDING = 1;      // 申请中
    private static final int STATUS_USER_APPROVED = 2; // 新借用人已同意
    private static final int STATUS_ADMIN_APPROVED = 3; // 管理员已同意
    private static final int STATUS_REJECTED = 4;      // 已拒绝

    /**
     * 操作员申请转借
     */
    @PostMapping("/apply")
    public Map<String, Object> applyTransfer(@RequestBody Map<String, Object> request, jakarta.servlet.http.HttpServletRequest httpRequest) {
        Map<String, Object> responseObj = new HashMap<>();
        Long deviceId = null;
        Long fromUserId = null;
        String fromUserName = null;
        Integer fromUserRole = null;
        
        try {
            deviceId = Long.parseLong(request.get("deviceId").toString());
            fromUserId = Long.parseLong(request.get("fromUserId").toString());
            Long toUserId = Long.parseLong(request.get("toUserId").toString());
            
            // 获取申请人信息
            Optional<SysUser> fromUserOpt = sysUserRepository.findById(fromUserId);
            if (fromUserOpt.isPresent()) {
                fromUserName = fromUserOpt.get().getSysusername();
                fromUserRole = fromUserOpt.get().getSysuserrole().intValue();
            }
            
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                
                // 记录失败日志
                if (logOperationService != null) {
                    logOperationService.logFail(
                            fromUserId,
                            fromUserName,
                            fromUserRole,
                            com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_APPLY,
                            com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                            "转借申请失败: 设备不存在",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            "设备不存在",
                            httpRequest);
                }
                
                // 广播设备状态更新
                if (deviceStatusNotificationService != null) {
                    deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
                }
                
                return responseObj;
            }
            
            Device device = deviceOpt.get();
            
            // 验证当前借用人是否是申请人
            if (device.getDeviceyh() == null || !device.getDeviceyh().getId().equals(fromUserId)) {
                responseObj.put("success", false);
                responseObj.put("message", "只有当前借用人才能申请转借");
                
                // 记录失败日志
                if (logOperationService != null) {
                    logOperationService.logFail(
                            fromUserId,
                            fromUserName,
                            fromUserRole,
                            com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_APPLY,
                            com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                            "转借申请失败: 只有当前借用人才能申请转借",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            device.getDeviceno(),
                            "只有当前借用人才能申请转借",
                            httpRequest);
                }
                
                // 广播设备状态更新
                if (deviceStatusNotificationService != null) {
                    deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
                }
                
                return responseObj;
            }
            
            Optional<SysUser> toUserOpt = sysUserRepository.findById(toUserId);
            
            if (!toUserOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "新借用人不存在");
                
                // 记录失败日志
                if (logOperationService != null) {
                    logOperationService.logFail(
                            fromUserId,
                            fromUserName,
                            fromUserRole,
                            com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_APPLY,
                            com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                            "转借申请失败: 新借用人不存在",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            device.getDeviceno(),
                            "新借用人不存在",
                            httpRequest);
                }
                
                // 广播设备状态更新
                if (deviceStatusNotificationService != null) {
                    deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
                }
                
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
                
                // 记录成功日志
                if (logOperationService != null) {
                    logOperationService.logSuccess(
                            fromUserId,
                            fromUserName,
                            fromUserRole,
                            com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_APPLY,
                            com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                            "转借申请成功: 设备" + device.getDeviceno() + "转借给" + toUserOpt.get().getSysusername(),
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            device.getDeviceno(),
                            httpRequest);
                }
                
                // 通知客户端设备状态更新
                System.out.println("=== 转借申请：准备通知客户端设备状态更新 ===");
                System.out.println("设备ID: " + deviceId);
                if (deviceStatusNotificationService != null) {
                    deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
                    System.out.println("=== 转借申请：通知已发送 ===");
                } else {
                    System.out.println("=== 转借申请：deviceStatusNotificationService 为 null ===");
                }
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "转借申请已提交，等待新借用人确认");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "转借申请失败: " + e.getMessage());
            
            // 记录失败日志
            if (logOperationService != null) {
                logOperationService.logFail(
                        fromUserId,
                        fromUserName,
                        fromUserRole,
                        com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_APPLY,
                        com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                        "转借申请失败: " + e.getMessage(),
                        com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                        deviceId,
                        null,
                        e.getMessage(),
                        httpRequest);
            }
            
            // 广播设备状态更新
            if (deviceStatusNotificationService != null && deviceId != null) {
                deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
            }
        }
        return responseObj;
    }

    /**
     * 操作员同意转借
     */
    @PostMapping("/userApprove")
    public Map<String, Object> userApproveTransfer(@RequestBody Map<String, Object> request, jakarta.servlet.http.HttpServletRequest httpRequest) {
        Map<String, Object> responseObj = new HashMap<>();
        Long transferId = null;
        Long userId = null;
        String userName = null;
        Integer userRole = null;

        try {
            transferId = Long.parseLong(request.get("transferId").toString());
            userId = Long.parseLong(request.get("userId").toString());

            Optional<SysUser> userOpt = sysUserRepository.findById(userId);
            if (userOpt.isPresent()) {
                userName = userOpt.get().getSysusername();
                userRole = userOpt.get().getSysuserrole().intValue();
            }

            Optional<DeviceTransferRecord> recordOpt = transferRecordRepository.findById(transferId);
            if (!recordOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "转借记录不存在");

                if (logOperationService != null) {
                    logOperationService.logFail(
                            userId,
                            userName,
                            userRole,
                            com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_USER_APPROVE,
                            com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                            "同意转借失败: 转借记录不存在",
                            com.example.demo20250620.entity.LogOperation.TARGET_TRANSFER_RECORD,
                            transferId,
                            null,
                            "转借记录不存在",
                            httpRequest);
                }

                if (deviceStatusNotificationService != null) {
                    deviceStatusNotificationService.notifyDeviceStatusUpdate(null);
                }

                return responseObj;
            }

            DeviceTransferRecord record = recordOpt.get();

            if (!record.getToUser().getId().equals(userId)) {
                responseObj.put("success", false);
                responseObj.put("message", "您不是转借目标用户，无法同意");

                if (logOperationService != null) {
                    logOperationService.logFail(
                            userId,
                            userName,
                            userRole,
                            com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_USER_APPROVE,
                            com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                            "同意转借失败: 不是转借目标用户",
                            com.example.demo20250620.entity.LogOperation.TARGET_TRANSFER_RECORD,
                            transferId,
                            record.getDevice() != null ? record.getDevice().getDeviceno() : null,
                            "不是转借目标用户",
                            httpRequest);
                }

                if (deviceStatusNotificationService != null && record.getDevice() != null) {
                    deviceStatusNotificationService.notifyDeviceStatusUpdate(record.getDevice().getId());
                }

                return responseObj;
            }

            if (record.getStatus() != STATUS_PENDING) {
                responseObj.put("success", false);
                responseObj.put("message", "当前状态不允许此操作");

                if (logOperationService != null) {
                    logOperationService.logFail(
                            userId,
                            userName,
                            userRole,
                            com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_USER_APPROVE,
                            com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                            "同意转借失败: 状态不允许操作",
                            com.example.demo20250620.entity.LogOperation.TARGET_TRANSFER_RECORD,
                            transferId,
                            record.getDevice() != null ? record.getDevice().getDeviceno() : null,
                            "当前状态不允许此操作",
                            httpRequest);
                }

                if (deviceStatusNotificationService != null && record.getDevice() != null) {
                    deviceStatusNotificationService.notifyDeviceStatusUpdate(record.getDevice().getId());
                }

                return responseObj;
            }

            record.setStatus(STATUS_USER_APPROVED);
            record.setApprovalDate(LocalDateTime.now());
            transferRecordRepository.save(record);

            Device device = record.getDevice();
            Optional<Devicestate> stateOpt = devicestateRepository.findById(8L);
            if (stateOpt.isPresent()) {
                device.setDevicestate(stateOpt.get());
                deviceRepository.save(device);

                if (logOperationService != null) {
                    logOperationService.logSuccess(
                            userId,
                            userName,
                            userRole,
                            com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_USER_APPROVE,
                            com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                            "同意转借成功: 设备" + device.getDeviceno() + "转借申请已同意",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            device.getId(),
                            device.getDeviceno(),
                            httpRequest);
                }

                if (deviceStatusNotificationService != null) {
                    deviceStatusNotificationService.notifyDeviceStatusUpdate(device.getId());
                }
            }

            responseObj.put("success", true);
            responseObj.put("message", "已同意转借，等待管理员批准");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "同意转借失败: " + e.getMessage());

            if (logOperationService != null) {
                logOperationService.logFail(
                        userId,
                        userName,
                        userRole,
                        com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_USER_APPROVE,
                        com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                        "同意转借失败: " + e.getMessage(),
                        com.example.demo20250620.entity.LogOperation.TARGET_TRANSFER_RECORD,
                        transferId,
                        null,
                        e.getMessage(),
                        httpRequest);
            }

            if (deviceStatusNotificationService != null) {
                deviceStatusNotificationService.notifyDeviceStatusUpdate(null);
            }
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
            
            // 更新状态为"管理员已同意"(ID=3)
            record.setStatus(STATUS_ADMIN_APPROVED);
            record.setAdminApprovalDate(LocalDateTime.now());
            
            Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
            if (adminOpt.isPresent()) {
                record.setAdminApprovalUser(adminOpt.get());
            }
            
            transferRecordRepository.save(record);
            
            // 更新设备信息
            Device device = record.getDevice();
            device.setDeviceyh(record.getToUser());
            device.setTransferTargetId(null);
            
            // 更新设备状态为"借用中"(ID=4)
            Optional<Devicestate> stateOpt = devicestateRepository.findById(4L);
            if (stateOpt.isPresent()) {
                device.setDevicestate(stateOpt.get());
            }
            deviceRepository.save(device);
            
            // 通知客户端设备状态更新
            System.out.println("=== 管理员批准转借：准备通知客户端设备状态更新 ===");
            System.out.println("设备ID: " + device.getId());
            if (deviceStatusNotificationService != null) {
                deviceStatusNotificationService.notifyDeviceStatusUpdate(device.getId());
                System.out.println("=== 管理员批准转借：通知已发送 ===");
            } else {
                System.out.println("=== 管理员批准转借：deviceStatusNotificationService 为 null ===");
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "转借已批准");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "批准转借失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 操作员撤销转借
     */
    @PostMapping("/cancel")
    public Map<String, Object> cancelTransfer(@RequestBody Map<String, Object> request, jakarta.servlet.http.HttpServletRequest httpRequest) {
        Map<String, Object> responseObj = new HashMap<>();
        Long transferId = null;
        Long userId = null;
        String userName = null;
        Integer userRole = null;

        try {
            transferId = Long.parseLong(request.get("transferId").toString());
            userId = Long.parseLong(request.get("userId").toString());

            Optional<SysUser> userOpt = sysUserRepository.findById(userId);
            if (userOpt.isPresent()) {
                userName = userOpt.get().getSysusername();
                userRole = userOpt.get().getSysuserrole().intValue();
            }

            Optional<DeviceTransferRecord> recordOpt = transferRecordRepository.findById(transferId);
            if (!recordOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "转借记录不存在");

                if (logOperationService != null) {
                    logOperationService.logFail(
                            userId,
                            userName,
                            userRole,
                            com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_CANCEL,
                            com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                            "撤销转借失败: 转借记录不存在",
                            com.example.demo20250620.entity.LogOperation.TARGET_TRANSFER_RECORD,
                            transferId,
                            null,
                            "转借记录不存在",
                            httpRequest);
                }

                if (deviceStatusNotificationService != null) {
                    deviceStatusNotificationService.notifyDeviceStatusUpdate(null);
                }

                return responseObj;
            }

            DeviceTransferRecord record = recordOpt.get();

            if (!record.getFromUser().getId().equals(userId)) {
                responseObj.put("success", false);
                responseObj.put("message", "只有原借用人才能撤销转借");

                if (logOperationService != null) {
                    logOperationService.logFail(
                            userId,
                            userName,
                            userRole,
                            com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_CANCEL,
                            com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                            "撤销转借失败: 只有原借用人才能撤销",
                            com.example.demo20250620.entity.LogOperation.TARGET_TRANSFER_RECORD,
                            transferId,
                            record.getDevice() != null ? record.getDevice().getDeviceno() : null,
                            "只有原借用人才能撤销转借",
                            httpRequest);
                }

                if (deviceStatusNotificationService != null && record.getDevice() != null) {
                    deviceStatusNotificationService.notifyDeviceStatusUpdate(record.getDevice().getId());
                }

                return responseObj;
            }

            record.setStatus(STATUS_REJECTED);
            transferRecordRepository.save(record);

            Device device = record.getDevice();
            Optional<Devicestate> stateOpt = devicestateRepository.findById(4L);
            if (stateOpt.isPresent()) {
                device.setDevicestate(stateOpt.get());
            }
            device.setTransferTargetId(null);
            deviceRepository.save(device);

            if (logOperationService != null) {
                logOperationService.logSuccess(
                        userId,
                        userName,
                        userRole,
                        com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_CANCEL,
                        com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                        "撤销转借成功: 设备" + device.getDeviceno() + "转借已撤销",
                        com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                        device.getId(),
                        device.getDeviceno(),
                        httpRequest);
            }

            if (deviceStatusNotificationService != null) {
                deviceStatusNotificationService.notifyDeviceStatusUpdate(device.getId());
            }

            responseObj.put("success", true);
            responseObj.put("message", "转借已撤销");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "撤销转借失败: " + e.getMessage());

            if (logOperationService != null) {
                logOperationService.logFail(
                        userId,
                        userName,
                        userRole,
                        com.example.demo20250620.entity.LogOperation.TYPE_TRANSFER_CANCEL,
                        com.example.demo20250620.entity.LogOperation.MODULE_TRANSFER,
                        "撤销转借失败: " + e.getMessage(),
                        com.example.demo20250620.entity.LogOperation.TARGET_TRANSFER_RECORD,
                        transferId,
                        null,
                        e.getMessage(),
                        httpRequest);
            }

            if (deviceStatusNotificationService != null) {
                deviceStatusNotificationService.notifyDeviceStatusUpdate(null);
            }
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
     * 获取管理员待审批的转借申请
     */
    @GetMapping("/pendingForAdmin")
    public List<DeviceTransferRecord> getPendingForAdmin() {
        return transferRecordRepository.findByStatus(STATUS_USER_APPROVED);     
    }

    /**
     * 获取设备的转借记录
     */
    @GetMapping("/history/{deviceId}")
    public List<DeviceTransferRecord> getTransferHistory(@PathVariable Long deviceId) {
        return transferRecordRepository.findByDeviceId(deviceId);
    }

    /**
     * 获取所有转借记录（供页面显示）
     */
    @GetMapping("/list")
    public Map<String, Object> listTransferRecords(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> result = new HashMap<>();
        try {
            int pageIndex = Math.max(0, page - 1);
            Page<DeviceTransferRecord> recordPage;

            if (keyword == null || keyword.trim().isEmpty()) {
                recordPage = transferRecordRepository.findAllWithDetails(PageRequest.of(pageIndex, limit));
            } else {
                recordPage = transferRecordRepository.findByKeywordWithDetails(keyword.trim(), PageRequest.of(pageIndex, limit));
            }

            List<DeviceTransferRecord> records = recordPage.getContent();       

            result.put("success", true);
            result.put("data", records);
            result.put("total", recordPage.getTotalElements());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取转借记录失败: " + e.getMessage());  
        }
        return result;
    }

    /**
     * 导出转借记录到Excel
     */
    @GetMapping("/exportExcel")
    public ResponseEntity<byte[]> exportTransferExcel(@RequestParam(required = false) String keyword) throws IOException {
        // 获取数据
        List<DeviceTransferRecord> records;
        if (keyword == null || keyword.trim().isEmpty()) {
            records = transferRecordRepository.findAllWithDetails();
        } else {
            records = transferRecordRepository.findByKeywordWithDetails(keyword.trim());
        }

        // 填充用户名和状态文字
        for (DeviceTransferRecord record : records) {
            if (record.getFromUser() != null) {
                record.setFromUsername(record.getFromUser().getSysusername());  
            }
            if (record.getToUser() != null) {
                record.setToUsername(record.getToUser().getSysusername());      
            }
            if (record.getAdminApprovalUser() != null) {
                record.setAdminApprovalUsername(record.getAdminApprovalUser().getSysusername());
            }
            record.setStatusText(getStatusText(record.getStatus()));
        }

        // 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook();  
        Sheet sheet = workbook.createSheet("设备转借记录");

        // 创建表头样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        // 创建数据样式
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setAlignment(HorizontalAlignment.CENTER);
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        // 创建表头
        String[] headers = {"设备编号", "芯片", "类型", "型号", "厂商", "原借用人", "转借申请日期", "新借用人", "新借用人同意日期", "批准管理员", "批准时间", "状态", "详情"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // 填充数据
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int rowNum = 1;
        for (DeviceTransferRecord record : records) {
            Row row = sheet.createRow(rowNum++);

            // 设备编号
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(record.getDevice() != null ? record.getDevice().getDeviceno() : "");
            cell0.setCellStyle(dataStyle);

            // 芯片
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(record.getDevice() != null && record.getDevice().getDevCpu() != null ? record.getDevice().getDevCpu().getCpuname() : "");        
            cell1.setCellStyle(dataStyle);

            // 类型
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(record.getDevice() != null && record.getDevice().getDevType() != null ? record.getDevice().getDevType().getTypename() : "");     
            cell2.setCellStyle(dataStyle);

            // 型号
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(record.getDevice() != null ? record.getDevice().getDevicexh() : "");
            cell3.setCellStyle(dataStyle);

            // 厂商
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(record.getDevice() != null && record.getDevice().getDevManufacturer() != null ? record.getDevice().getDevManufacturer().getManufacturername() : "");
            cell4.setCellStyle(dataStyle);

            // 原借用人
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(record.getFromUsername() != null ? record.getFromUsername() : "");
            cell5.setCellStyle(dataStyle);

            // 转借申请日期
            Cell cell6 = row.createCell(6);     
            cell6.setCellValue(record.getTransferDate() != null ? record.getTransferDate().format(formatter) : "");
            cell6.setCellStyle(dataStyle);

            // 新借用人
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(record.getToUsername() != null ? record.getToUsername() : "");
            cell7.setCellStyle(dataStyle);

            // 新借用人同意日期
            Cell cell8 = row.createCell(8);
            cell8.setCellValue(record.getApprovalDate() != null ? record.getApprovalDate().format(formatter) : "");
            cell8.setCellStyle(dataStyle);

            // 批准管理员
            Cell cell9 = row.createCell(9);       
            cell9.setCellValue(record.getAdminApprovalUsername() != null ? record.getAdminApprovalUsername() : "");
            cell9.setCellStyle(dataStyle);

            // 批准时间
            Cell cell10 = row.createCell(10);
            cell10.setCellValue(record.getAdminApprovalDate() != null ? record.getAdminApprovalDate().format(formatter) : "");
            cell10.setCellStyle(dataStyle);

            // 状态
            Cell cell11 = row.createCell(11);
            cell11.setCellValue(record.getStatusText() != null ? record.getStatusText() : "");
            cell11.setCellStyle(dataStyle);

            // 详情
            Cell cell12 = row.createCell(12);
            cell12.setCellValue(record.getDetail() != null ? record.getDetail() : "");
            cell12.setCellStyle(dataStyle);
        }

        // 调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // 写入字节数组
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        // 设置响应头
        byte[] body = out.toByteArray();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Disposition", "attachment;filename=transfer_records.xlsx");

        return new ResponseEntity<>(body, responseHeaders, org.springframework.http.HttpStatus.OK);
    }

    /**
     * 获取状态文字
     */
    private String getStatusText(Integer status) {
        if (status == null) return "";
        switch (status) {
            case 1: return "申请中";
            case 2: return "新借用人已同意";
            case 3: return "管理员已同意";
            case 4: return "已撤销";
            default: return "未知状态";
        }
    }
}
