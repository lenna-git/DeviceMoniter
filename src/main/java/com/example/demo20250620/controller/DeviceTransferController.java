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
    
    /**
     * 获取所有转借记录（供页面展示）
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
            
            // 填充用户名和状态文本
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
                // 设置状态文本
                record.setStatusText(getStatusText(record.getStatus()));
            }
            
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
        
        // 填充用户名和状态文本
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
        String[] headers = {"设备编号", "芯片", "类型", "型号", "厂商", "原借用人", "转借申请日期", "新借用人", "新借用人同意日期", "批准管理员", "批准日期", "状态", "详情"};
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
            
            // 批准日期
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
     * 获取状态文本
     */
    private String getStatusText(Integer status) {
        if (status == null) return "";
        switch (status) {
            case 1: return "申请中";
            case 2: return "新借用人已同意";
            case 3: return "管理员已同意";
            case 4: return "已拒绝";
            default: return "未知状态";
        }
    }
}