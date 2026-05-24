package com.example.demo20250620.controller;

import com.example.demo20250620.entity.Device;
import com.example.demo20250620.entity.DeviceRecord;
import com.example.demo20250620.entity.Devicestate;
import com.example.demo20250620.entity.SysUser;
import com.example.demo20250620.repository.DeviceRecordRepository;
import com.example.demo20250620.repository.DeviceRepository;
import com.example.demo20250620.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/devicerecord")
public class DeviceRecordController {
    @Autowired
    private DeviceRecordRepository deviceRecordRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private SysUserRepository sysUserRepository;
    @Autowired
    private com.example.demo20250620.service.DeviceStatusNotificationService deviceStatusNotificationService;
    @Autowired
    private com.example.demo20250620.service.LogOperationService logOperationService;


    /**
     * 根据用户名查询对应记录
     * @return
     */
//    @GetMapping("/getByUserName")
//    public Optional<DeviceRecord> getByUserName(@RequestParam String UserName){
//
//        Optional<DeviceRecord> byUserName = deviceRecordRepository.findByUserName(UserName);
//
//
//        System.out.println(byUserName.toString());
//        return byUserName;
//    }


    public boolean EmptyorNot(String s){
        return s == null || s.isEmpty();





    }

    public boolean EmptyorNot(Long s){
        return s == null;
    }

    @GetMapping("/alldevicerecords")
    public Map<String, Object> getAllDevicerecords(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit){
        Map<String, Object> responseObj = new HashMap<>();
        try {
            int pageIndex = Math.max(0, page - 1);
            Page<DeviceRecord> deviceRecordPage;
            if (keyword == null || keyword.trim().isEmpty()) {
                deviceRecordPage = deviceRecordRepository.findAllWithDeviceAndUser(PageRequest.of(pageIndex, limit));
            } else {
                deviceRecordPage = deviceRecordRepository.findByKeywordWithUsername(keyword.trim(), PageRequest.of(pageIndex, limit));
            }
            
            List<DeviceRecord> records = deviceRecordPage.getContent();
            
            responseObj.put("data", records);
            responseObj.put("total", deviceRecordPage.getTotalElements());
            responseObj.put("success", true);
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "获取借用记录失败: " + e.getMessage());
        }
        return responseObj;
    }

    @PutMapping("/updateDeviceRecordById/{deviceId}")
    public void updateDeviceRecordById(@PathVariable Long deviceId, @RequestBody DeviceRecord deviceRecord) {
        Optional<DeviceRecord> deviceRecord1 = deviceRecordRepository.findById(deviceId);
        DeviceRecord deviceRecord2 = deviceRecord1.orElseGet(() -> new DeviceRecord());
        deviceRecord2.getDevice().setDeviceno(deviceRecord.getDevice().getDeviceno());
        deviceRecord2.setDetail(deviceRecord.getDetail());
        //deviceRecord2.setUserId(deviceRecord.getUserId());
        deviceRecord2.setBorrorDate(deviceRecord.getBorrorDate());
        deviceRecord2.setReturnDate(deviceRecord.getReturnDate());
        deviceRecordRepository.save(deviceRecord2);

    }

    @PostMapping("/createDeviceRecord")
    public DeviceRecord createDeviceRecord(@RequestBody DeviceRecord deviceRecord) {
        System.out.println("执行了");
        System.out.println(deviceRecord.toString());
        return deviceRecordRepository.save(deviceRecord);
    }


    @DeleteMapping("/delDeviceRecords/{deviceId}")
    public void delDeviceRecords(@PathVariable Long deviceId) {
        deviceRecordRepository.deleteById(deviceId);
    }

    /**
     * 设备借用接口
     * 1. 更新设备表：设置借用人ID，状态改为"借用中待通过"(ID=3)
     * 2. 在devicerecord表中插入一条借用记录
     */
    @PostMapping("/borrowDevice")
    public Map<String, Object> borrowDevice(@RequestBody Map<String, Long> request) {
        Map<String, Object> responseObj = new HashMap<>();
        Long deviceId = null;
        Long userId = null;
        try {
            deviceId = request.get("deviceId");
            userId = request.get("userId");
            
            if (deviceId == null || userId == null) {
                responseObj.put("success", false);
                responseObj.put("message", "设备ID和用户ID不能为空");
                return responseObj;
            }
            
            // 查询设备
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                
                // 记录设备不存在的失败日志
                Optional<SysUser> userOpt = sysUserRepository.findById(userId);
                if (userOpt.isPresent() && logOperationService != null) {
                    SysUser user = userOpt.get();
                    logOperationService.logFail(
                            user.getId(),
                            user.getSysusername(),
                            user.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_BORROW,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "操作员【" + user.getSysusername() + "】借用设备失败：设备不存在",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            "设备不存在",
                            null);
                }
                return responseObj;
            }
            
            // 查询用户
            Optional<SysUser> userOpt = sysUserRepository.findById(userId);
            if (!userOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "用户不存在");
                return responseObj;
            }
            
            Device device = deviceOpt.get();
            SysUser user = userOpt.get();
            
            // 更新设备状态为"借用中待通过"(ID=3)
            Devicestate state = new Devicestate();
            state.setId(3L);
            device.setDevicestate(state);
            
            // 设置借用人
            device.setDeviceyh(user);
            
            deviceRepository.save(device);
            // 通知客户端设备状态更新
            deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
            
            // 创建借用记录
            DeviceRecord record = new DeviceRecord();
            record.setDevice(device);
            record.setUserId(userId);
            record.setBorrorDate(java.time.LocalDateTime.now().toString());
            record.setReturnDate(null);
            record.setDetail("设备借用");
            
            deviceRecordRepository.save(record);
            
            responseObj.put("success", true);
            responseObj.put("message", "设备借用申请成功，等待管理员审核");
            
            // 记录借用成功日志
            if (logOperationService != null) {
                logOperationService.logSuccess(
                        user.getId(),
                        user.getSysusername(),
                        user.getSysuserrole().intValue(),
                        com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_BORROW,
                        com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                        "操作员【" + user.getSysusername() + "】借用设备【" + device.getDeviceno() + "】成功",
                        com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                        deviceId,
                        device.getDeviceno(),
                        null);
            }
            
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备借用失败: " + e.getMessage());
            
            // 记录借用失败日志
            if (userId != null) {
                Optional<SysUser> userOpt = sysUserRepository.findById(userId);
                if (userOpt.isPresent() && logOperationService != null) {
                    SysUser user = userOpt.get();
                    logOperationService.logFail(
                            user.getId(),
                            user.getSysusername(),
                            user.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_BORROW,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "操作员【" + user.getSysusername() + "】借用设备失败：" + e.getMessage(),
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            e.getMessage(),
                            null);
                }
            }
        }
        return responseObj;
    }

    /**
     * 管理员通过借用申请
     * 1. 在devicerecord表中找到该设备的待批准记录
     * 2. 填入管理员ID和批准时间
     * 3. 更新设备状态为"借用中"(ID=4)
     */
    @PostMapping("/approveBorrow")
    @Transactional
    public Map<String, Object> approveBorrow(@RequestBody Map<String, Long> request) {
        Map<String, Object> responseObj = new HashMap<>();
        Long deviceId = null;
        Long adminId = null;
        try {
            deviceId = request.get("deviceId");
            adminId = request.get("adminId");
            
            if (deviceId == null || adminId == null) {
                responseObj.put("success", false);
                responseObj.put("message", "设备ID和管理员ID不能为空");
                return responseObj;
            }
            
            // 查询待批准的借用记录
            Optional<DeviceRecord> recordOpt = deviceRecordRepository.findPendingBorrowRecord(deviceId);
            if (!recordOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "未找到待批准的借用记录");
                
                // 记录未找到待批准记录的失败日志
                Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
                if (adminOpt.isPresent() && logOperationService != null) {
                    SysUser admin = adminOpt.get();
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_BORROW_APPROVE,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "管理员【" + admin.getSysusername() + "】通过借用申请失败：未找到待批准的借用记录",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            "未找到待批准的借用记录",
                            null);
                }
                return responseObj;
            }
            
            // 查询设备
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                
                // 记录设备不存在的失败日志
                Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
                if (adminOpt.isPresent() && logOperationService != null) {
                    SysUser admin = adminOpt.get();
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_BORROW_APPROVE,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "管理员【" + admin.getSysusername() + "】通过借用申请失败：设备不存在",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            "设备不存在",
                            null);
                }
                return responseObj;
            }
            
            // 查询管理员用户
            Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
            if (!adminOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "管理员用户不存在");
                return responseObj;
            }
            
            DeviceRecord record = recordOpt.get();
            Device device = deviceOpt.get();
            SysUser admin = adminOpt.get();
            
            // 更新借用记录：设置管理员ID和批准时间
            record.setSysUser(admin);
            record.setApprovalDate(java.time.LocalDateTime.now().toString());
            deviceRecordRepository.save(record);
            
            // 更新设备状态为"借用中"(ID=4)
            Devicestate state = new Devicestate();
            state.setId(4L);
            device.setDevicestate(state);
            deviceRepository.save(device);
            // 通知客户端设备状态更新
            deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
            
            responseObj.put("success", true);
            responseObj.put("message", "借用申请已通过，设备状态已更新为借用中");
            
            // 记录通过借用申请成功日志
            if (logOperationService != null) {
                logOperationService.logSuccess(
                        admin.getId(),
                        admin.getSysusername(),
                        admin.getSysuserrole().intValue(),
                        com.example.demo20250620.entity.LogOperation.TYPE_BORROW_APPROVE,
                        com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                        "管理员【" + admin.getSysusername() + "】通过借用申请，设备【" + device.getDeviceno() + "】状态已更新为借用中",
                        com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                        deviceId,
                        device.getDeviceno(),
                        null);
            }
            
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "通过借用申请失败: " + e.getMessage());
            
            // 记录通过借用申请失败日志
            if (adminId != null) {
                Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
                if (adminOpt.isPresent() && logOperationService != null) {
                    SysUser admin = adminOpt.get();
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_BORROW_APPROVE,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "管理员【" + admin.getSysusername() + "】通过借用申请失败：" + e.getMessage(),
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            e.getMessage(),
                            null);
                }
            }
        }
        return responseObj;
    }

    /**
     * 管理员拒绝借用申请
     * 1. 在devicerecord表中找到该设备的待批准记录
     * 2. 填入管理员ID、批准时间和拒绝原因
     * 3. 更新设备状态为"已安检待借用"(ID=2)，借用人置空
     */
    @PostMapping("/rejectBorrow")
    @Transactional
    public Map<String, Object> rejectBorrow(@RequestBody Map<String, Long> request) {
        Map<String, Object> responseObj = new HashMap<>();
        Long deviceId = null;
        Long adminId = null;
        try {
            deviceId = request.get("deviceId");
            adminId = request.get("adminId");
            
            if (deviceId == null || adminId == null) {
                responseObj.put("success", false);
                responseObj.put("message", "设备ID和管理员ID不能为空");
                return responseObj;
            }
            
            // 查询待批准的借用记录
            Optional<DeviceRecord> recordOpt = deviceRecordRepository.findPendingBorrowRecord(deviceId);
            if (!recordOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "未找到待批准的借用记录");
                
                // 记录未找到待批准记录的失败日志
                Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
                if (adminOpt.isPresent() && logOperationService != null) {
                    SysUser admin = adminOpt.get();
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_BORROW_REJECT,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "管理员【" + admin.getSysusername() + "】拒绝借用申请失败：未找到待批准的借用记录",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            "未找到待批准的借用记录",
                            null);
                }
                return responseObj;
            }
            
            // 查询设备
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                
                // 记录设备不存在的失败日志
                Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
                if (adminOpt.isPresent() && logOperationService != null) {
                    SysUser admin = adminOpt.get();
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_BORROW_REJECT,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "管理员【" + admin.getSysusername() + "】拒绝借用申请失败：设备不存在",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            "设备不存在",
                            null);
                }
                return responseObj;
            }
            
            // 查询管理员用户
            Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
            if (!adminOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "管理员用户不存在");
                return responseObj;
            }
            
            DeviceRecord record = recordOpt.get();
            Device device = deviceOpt.get();
            SysUser admin = adminOpt.get();
            
            // 更新借用记录：设置管理员ID、批准时间和拒绝原因
            record.setSysUser(admin);
            record.setApprovalDate(java.time.LocalDateTime.now().toString());
            record.setDetail("管理员拒绝借用申请");
            deviceRecordRepository.save(record);
            
            // 更新设备状态为"已安检待借用"(ID=2)，借用人置空
            Devicestate state = new Devicestate();
            state.setId(2L);
            device.setDevicestate(state);
            device.setDeviceyh(null);
            deviceRepository.save(device);
            // 通知客户端设备状态更新
            deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
            
            responseObj.put("success", true);
            responseObj.put("message", "借用申请已拒绝，设备状态已更新为已安检待借用");
            
            // 记录拒绝借用申请成功日志
            if (logOperationService != null) {
                logOperationService.logSuccess(
                        admin.getId(),
                        admin.getSysusername(),
                        admin.getSysuserrole().intValue(),
                        com.example.demo20250620.entity.LogOperation.TYPE_BORROW_REJECT,
                        com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                        "管理员【" + admin.getSysusername() + "】拒绝借用申请，设备【" + device.getDeviceno() + "】状态已更新为已安检待借用",
                        com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                        deviceId,
                        device.getDeviceno(),
                        null);
            }
            
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "拒绝借用申请失败: " + e.getMessage());
            
            // 记录拒绝借用申请失败日志
            if (adminId != null) {
                Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
                if (adminOpt.isPresent() && logOperationService != null) {
                    SysUser admin = adminOpt.get();
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_BORROW_REJECT,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "管理员【" + admin.getSysusername() + "】拒绝借用申请失败：" + e.getMessage(),
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            e.getMessage(),
                            null);
                }
            }
        }
        return responseObj;
    }

    /**
     * 操作员退回设备
     * 1. 更新设备状态为"申请归还中待通过"(ID=8)
     */
    @PostMapping("/returnDevice")
    public Map<String, Object> returnDevice(@RequestBody Map<String, Long> request) {
        Map<String, Object> responseObj = new HashMap<>();
        Long deviceId = null;
        Long userId = null;
        try {
            deviceId = request.get("deviceId");
            userId = request.get("userId");
            
            if (deviceId == null || userId == null) {
                responseObj.put("success", false);
                responseObj.put("message", "设备ID和用户ID不能为空");
                return responseObj;
            }
            
            // 查询设备
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                
                // 记录设备不存在的失败日志
                Optional<SysUser> userOpt = sysUserRepository.findById(userId);
                if (userOpt.isPresent() && logOperationService != null) {
                    SysUser user = userOpt.get();
                    logOperationService.logFail(
                            user.getId(),
                            user.getSysusername(),
                            user.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_RETURN,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "操作员【" + user.getSysusername() + "】退回设备失败：设备不存在",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            "设备不存在",
                            null);
                }
                return responseObj;
            }
            
            Device device = deviceOpt.get();
            
            // 更新设备状态为"申请归还中待通过"(ID=8)
            Devicestate state = new Devicestate();
            state.setId(8L);
            device.setDevicestate(state);
            deviceRepository.save(device);
            // 通知客户端设备状态更新
            deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
            
            responseObj.put("success", true);
            responseObj.put("message", "设备退回申请已提交，等待管理员审核");
            
            // 记录退回设备成功日志
            Optional<SysUser> userOpt = sysUserRepository.findById(userId);
            if (userOpt.isPresent() && logOperationService != null) {
                SysUser user = userOpt.get();
                logOperationService.logSuccess(
                        user.getId(),
                        user.getSysusername(),
                        user.getSysuserrole().intValue(),
                        com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_RETURN,
                        com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                        "操作员【" + user.getSysusername() + "】退回设备【" + device.getDeviceno() + "】成功",
                        com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                        deviceId,
                        device.getDeviceno(),
                        null);
            }
            
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "退回设备失败: " + e.getMessage());
            
            // 记录退回设备失败日志
            if (userId != null) {
                Optional<SysUser> userOpt = sysUserRepository.findById(userId);
                if (userOpt.isPresent() && logOperationService != null) {
                    SysUser user = userOpt.get();
                    logOperationService.logFail(
                            user.getId(),
                            user.getSysusername(),
                            user.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_RETURN,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "操作员【" + user.getSysusername() + "】退回设备失败：" + e.getMessage(),
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            e.getMessage(),
                            null);
                }
            }
        }
        return responseObj;
    }

    /**
     * 管理员批准设备归还
     * 1. 更新设备状态为"已安检待借用"(ID=2)，借用人置空
     * 2. 在devicerecord中找到该设备returndate为空的记录，填入当前时间
     */
    @PostMapping("/approveReturn")
    public Map<String, Object> approveReturn(@RequestBody Map<String, Long> request) {
        Map<String, Object> responseObj = new HashMap<>();
        Long deviceId = null;
        Long adminId = null;
        try {
            deviceId = request.get("deviceId");
            adminId = request.get("adminId");
            
            if (deviceId == null || adminId == null) {
                responseObj.put("success", false);
                responseObj.put("message", "设备ID和管理员ID不能为空");
                return responseObj;
            }
            
            // 查询设备
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                
                // 记录设备不存在的失败日志
                Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
                if (adminOpt.isPresent() && logOperationService != null) {
                    SysUser admin = adminOpt.get();
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_RETURN_APPROVAL,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "管理员【" + admin.getSysusername() + "】批准设备归还失败：设备不存在",
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            "设备不存在",
                            null);
                }

                // 通知客户端设备状态更新
                if (deviceStatusNotificationService != null) {
                    deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
                }

                return responseObj;
            }
            
            Device device = deviceOpt.get();
            
            // 更新设备状态为"已安检待借用"(ID=2)，借用人置空
            Devicestate state = new Devicestate();
            state.setId(2L);
            device.setDevicestate(state);
            device.setDeviceyh(null);
            deviceRepository.save(device);
            // 通知客户端设备状态更新
            deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
            
            // 找到该设备returndate为空的记录，填入归还时间和归还批准人
            List<DeviceRecord> records = deviceRecordRepository.findActiveBorrowRecords(deviceId);
            if (!records.isEmpty()) {
                // 取最新的一条记录进行处理
                DeviceRecord record = records.get(0);
                record.setReturnDate(java.time.LocalDateTime.now().toString());
                record.setReturnApprovalUserId(adminId);
                record.setReturnApprovalDate(java.time.LocalDateTime.now().toString());
                record.setDetail("设备已归还");
                deviceRecordRepository.save(record);
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "设备归还已批准，状态已更新为已安检待借用");
            
            // 记录批准归还成功日志
            Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
            if (adminOpt.isPresent() && logOperationService != null) {
                SysUser admin = adminOpt.get();
                logOperationService.logSuccess(
                        admin.getId(),
                        admin.getSysusername(),
                        admin.getSysuserrole().intValue(),
                        com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_RETURN_APPROVAL,
                        com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                        "管理员【" + admin.getSysusername() + "】批准设备【" + device.getDeviceno() + "】归还成功",
                        com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                        deviceId,
                        device.getDeviceno(),
                        null);
            }
            
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "批准归还失败: " + e.getMessage());
            
            // 记录批准归还失败日志
            if (adminId != null) {
                Optional<SysUser> adminOpt = sysUserRepository.findById(adminId);
                if (adminOpt.isPresent() && logOperationService != null) {
                    SysUser admin = adminOpt.get();
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_DEVICE_RETURN_APPROVAL,
                            com.example.demo20250620.entity.LogOperation.MODULE_DEVICE,
                            "管理员【" + admin.getSysusername() + "】批准设备归还失败：" + e.getMessage(),
                            com.example.demo20250620.entity.LogOperation.TARGET_DEVICE,
                            deviceId,
                            null,
                            e.getMessage(),
                            null);
                }
            }

            // 通知客户端设备状态更新
            if (deviceStatusNotificationService != null) {
                deviceStatusNotificationService.notifyDeviceStatusUpdate(deviceId);
            }
        }
        return responseObj;
    }

    @PostMapping("/checkDeviceSnAndNo")
    @ResponseBody
    public Map<String, Object> checkDeviceSnAndNo(@RequestParam String devicesn, 
                                                   @RequestParam String deviceno,
                                                   @RequestParam(required = false) Long excludeId) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            List<Device> snDevices = deviceRepository.findByDevicesn(devicesn);
            if (!snDevices.isEmpty()) {
                if (excludeId == null || !snDevices.get(0).getId().equals(excludeId)) {
                    responseObj.put("success", false);
                    responseObj.put("message", "序列号已存在");
                    return responseObj;
                }
            }
            
            List<Device> noDevices = deviceRepository.findByDeviceno(deviceno);
            if (!noDevices.isEmpty()) {
                if (excludeId == null || !noDevices.get(0).getId().equals(excludeId)) {
                    responseObj.put("success", false);
                    responseObj.put("message", "设备编号已存在");
                    return responseObj;
                }
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "验证通过");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "验证失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 导出设备借用记录到Excel
     */
    @GetMapping("/exportExcel")
    public ResponseEntity<byte[]> exportExcel(@RequestParam(required = false) String keyword) throws IOException {
        // 获取数据
        List<DeviceRecord> records;
        if (keyword == null || keyword.trim().isEmpty()) {
            records = deviceRecordRepository.findAllWithDeviceAndUser();
        } else {
            records = deviceRecordRepository.findByKeywordWithUsername(keyword.trim());
        }
        
        // 填充用户名
        for (DeviceRecord record : records) {
            if (record.getUserId() != null) {
                Optional<SysUser> userOpt = sysUserRepository.findById(record.getUserId());
                if (userOpt.isPresent()) {
                    record.setBorrowerUsername(userOpt.get().getSysusername());
                }
            }
            if (record.getReturnApprovalUserId() != null) {
                Optional<SysUser> userOpt = sysUserRepository.findById(record.getReturnApprovalUserId());
                if (userOpt.isPresent()) {
                    record.setReturnApprovalUsername(userOpt.get().getSysusername());
                }
            }
        }
        
        // 创建Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("设备借用记录");
        
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
        String[] headers = {"设备编号", "芯片", "类型", "型号", "厂商", "借用人", "借用日期", "批准人", "批准借用日期", "归还日期", "批准归还人", "批准归还日期"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // 填充数据
        int rowNum = 1;
        for (DeviceRecord record : records) {
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
            
            // 借用人
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(record.getBorrowerUsername() != null ? record.getBorrowerUsername() : "");
            cell5.setCellStyle(dataStyle);
            
            // 借用日期
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(record.getBorrorDate() != null ? record.getBorrorDate() : "");
            cell6.setCellStyle(dataStyle);
            
            // 批准人
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(record.getSysUser() != null ? record.getSysUser().getSysusername() : "");
            cell7.setCellStyle(dataStyle);
            
            // 批准借用日期
            Cell cell8 = row.createCell(8);
            cell8.setCellValue(record.getApprovalDate() != null ? record.getApprovalDate() : "");
            cell8.setCellStyle(dataStyle);
            
            // 归还日期
            Cell cell9 = row.createCell(9);
            cell9.setCellValue(record.getReturnDate() != null ? record.getReturnDate() : "");
            cell9.setCellStyle(dataStyle);
            
            // 批准归还人
            Cell cell10 = row.createCell(10);
            cell10.setCellValue(record.getReturnApprovalUsername() != null ? record.getReturnApprovalUsername() : "");
            cell10.setCellStyle(dataStyle);
            
            // 批准归还日期
            Cell cell11 = row.createCell(11);
            cell11.setCellValue(record.getReturnApprovalDate() != null ? record.getReturnApprovalDate() : "");
            cell11.setCellStyle(dataStyle);
        }
        
        // 自动调整列宽
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // 写入字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        // 设置响应头
        String filename = "设备借用记录_" + java.time.LocalDateTime.now().toString().replace(":", "-") + ".xlsx";
        String encodedFilename = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
        
        org.springframework.http.HttpHeaders responseHeaders = new org.springframework.http.HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        responseHeaders.setContentDispositionFormData("attachment", encodedFilename);
        responseHeaders.add("Access-Control-Expose-Headers", "Content-Disposition");
        
        return new ResponseEntity<>(outputStream.toByteArray(), responseHeaders, org.springframework.http.HttpStatus.OK);
    }

}
