package com.example.demo20250620.controller;

import com.example.demo20250620.entity.Device;
import com.example.demo20250620.entity.DeviceRecord;
import com.example.demo20250620.entity.Devicestate;
import com.example.demo20250620.entity.SysUser;
import com.example.demo20250620.repository.DeviceRecordRepository;
import com.example.demo20250620.repository.DeviceRepository;
import com.example.demo20250620.repository.SysUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public List<DeviceRecord> getAllDevicerecords(@RequestParam(required = false) Long userId,String detail){
//<<<<<<< .mine
//        if(EmptyorNot(userId)&&EmptyorNot(detail)){
//            return deviceRecordRepository.findAll();
//        }
//        else if(!EmptyorNot(userId)&&EmptyorNot(detail)){
//
//            return deviceRecordRepository.findByUserId(userId);
//        }
//        else if(EmptyorNot(userId)&&!EmptyorNot(detail)){
//            return deviceRecordRepository.findByDetail(detail);
//        }else {
//            return deviceRecordRepository.findDeviceRecordByUserIdAndDetail(userId,detail);
//        }
//=======
        return deviceRecordRepository.findAll();
//        if(EmptyorNot(userId)&&EmptyorNot(detail))
//            return deviceRecordRepository.findAll();
//        else if(!EmptyorNot(userId)&&EmptyorNot(detail))
//            return deviceRecordRepository.findByUserId(userId);
//        else if(EmptyorNot(userId)&&!EmptyorNot(detail)){
//            return deviceRecordRepository.findByDetail(detail);
//        }else {
//            return deviceRecordRepository.findDeviceRecordByUserIdAndDetail(userId,detail);
//        }

//>>>>>>> .r35
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
        try {
            Long deviceId = request.get("deviceId");
            Long userId = request.get("userId");
            
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
            
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备借用失败: " + e.getMessage());
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
    public Map<String, Object> approveBorrow(@RequestBody Map<String, Long> request) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Long deviceId = request.get("deviceId");
            Long adminId = request.get("adminId");
            
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
                return responseObj;
            }
            
            // 查询设备
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
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
            
            responseObj.put("success", true);
            responseObj.put("message", "借用申请已通过，设备状态已更新为借用中");
            
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "通过借用申请失败: " + e.getMessage());
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
    public Map<String, Object> rejectBorrow(@RequestBody Map<String, Long> request) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Long deviceId = request.get("deviceId");
            Long adminId = request.get("adminId");
            
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
                return responseObj;
            }
            
            // 查询设备
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
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
            
            responseObj.put("success", true);
            responseObj.put("message", "借用申请已拒绝，设备状态已更新为已安检待借用");
            
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "拒绝借用申请失败: " + e.getMessage());
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
        try {
            Long deviceId = request.get("deviceId");
            Long userId = request.get("userId");
            
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
                return responseObj;
            }
            
            Device device = deviceOpt.get();
            
            // 更新设备状态为"申请归还中待通过"(ID=8)
            Devicestate state = new Devicestate();
            state.setId(8L);
            device.setDevicestate(state);
            deviceRepository.save(device);
            
            responseObj.put("success", true);
            responseObj.put("message", "设备退回申请已提交，等待管理员审核");
            
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "退回设备失败: " + e.getMessage());
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
        try {
            Long deviceId = request.get("deviceId");
            Long adminId = request.get("adminId");
            
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
                return responseObj;
            }
            
            Device device = deviceOpt.get();
            
            // 更新设备状态为"已安检待借用"(ID=2)，借用人置空
            Devicestate state = new Devicestate();
            state.setId(2L);
            device.setDevicestate(state);
            device.setDeviceyh(null);
            deviceRepository.save(device);
            
            // 找到该设备returndate为空的记录，填入归还时间
            Optional<DeviceRecord> recordOpt = deviceRecordRepository.findActiveBorrowRecord(deviceId);
            if (recordOpt.isPresent()) {
                DeviceRecord record = recordOpt.get();
                record.setReturnDate(java.time.LocalDateTime.now().toString());
                record.setDetail("设备已归还");
                deviceRecordRepository.save(record);
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "设备归还已批准，状态已更新为已安检待借用");
            
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "批准归还失败: " + e.getMessage());
        }
        return responseObj;
    }

}
