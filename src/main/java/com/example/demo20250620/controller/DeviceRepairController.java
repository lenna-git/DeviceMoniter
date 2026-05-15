package com.example.demo20250620.controller;

import com.example.demo20250620.entity.Device;
import com.example.demo20250620.entity.DeviceRepair;
import com.example.demo20250620.repository.DeviceRepository;
import com.example.demo20250620.repository.DeviceRepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/devicerepair/")
public class DeviceRepairController {
    
    @Autowired
    private DeviceRepairRepository deviceRepairRepository;
    
    @Autowired
    private DeviceRepository deviceRepository;
    
    @GetMapping("/all")
    public List<DeviceRepair> getAllRepairs() {
        return deviceRepairRepository.findAll();
    }
    
    @GetMapping("/bydevice/{deviceId}")
    public List<DeviceRepair> getRepairsByDeviceId(@PathVariable Long deviceId) {
        return deviceRepairRepository.findByDeviceId(deviceId);
    }
    
    @PostMapping("/create")
    public Map<String, Object> createRepair(@RequestBody Map<String, Object> request) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Long deviceId = Long.parseLong(request.get("deviceId").toString());
            String repairReason = (String) request.get("repairReason");
            
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            
            DeviceRepair repair = new DeviceRepair();
            repair.setDevice(deviceOpt.get());
            repair.setRepairTime(LocalDateTime.now());
            repair.setRepairReason(repairReason);
            
            deviceRepairRepository.save(repair);
            responseObj.put("success", true);
            responseObj.put("message", "维修记录创建成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "维修记录创建失败: " + e.getMessage());
        }
        return responseObj;
    }
    
    @PutMapping("/finish/{id}")
    public Map<String, Object> finishRepair(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<DeviceRepair> repairOpt = deviceRepairRepository.findById(id);
            if (!repairOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "维修记录不存在");
                return responseObj;
            }
            
            DeviceRepair repair = repairOpt.get();
            repair.setEndRepairTime(LocalDateTime.now());
            repair.setRepairRecord((String) request.get("repairRecord"));
            
            deviceRepairRepository.save(repair);
            responseObj.put("success", true);
            responseObj.put("message", "维修完成");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "维修完成失败: " + e.getMessage());
        }
        return responseObj;
    }
}