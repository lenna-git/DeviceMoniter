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
            record.setSysUser(user);
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

}
