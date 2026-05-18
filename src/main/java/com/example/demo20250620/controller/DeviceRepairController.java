package com.example.demo20250620.controller;

import com.example.demo20250620.entity.Device;
import com.example.demo20250620.entity.DeviceRepair;
import com.example.demo20250620.repository.DeviceRepository;
import com.example.demo20250620.repository.DeviceRepairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
    @Autowired
    private com.example.demo20250620.repository.DevicestateRepository devicestateRepository;
    
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
            Long reporterId = request.get("reporterId") != null ? Long.parseLong(request.get("reporterId").toString()) : null;
            
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            
            Device device = deviceOpt.get();
            
            // 创建维修记录
            DeviceRepair repair = new DeviceRepair();
            repair.setDevice(device);
            repair.setRepairTime(LocalDateTime.now());
            repair.setRepairReason("操作员申请维修");
            if (reporterId != null) {
                repair.setReporterId(reporterId);
            }
            
            deviceRepairRepository.save(repair);
            
            // 更新设备状态为"借出中待修理"(ID=5)
            Optional<com.example.demo20250620.entity.Devicestate> stateOpt = devicestateRepository.findById(5L);
            if (stateOpt.isPresent()) {
                device.setDevicestate(stateOpt.get());
                deviceRepository.save(device);
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "维修申请已提交，设备状态已更新为借出中待修理");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "维修记录创建失败: " + e.getMessage());
        }
        return responseObj;
    }
    
    /**
     * 管理员确认维修 - 将设备状态改为"修理中"并创建维修记录
     */
    @PostMapping("/confirm/{deviceId}")
    public Map<String, Object> confirmRepair(@PathVariable Long deviceId, @RequestBody Map<String, Object> request) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Long adminId = request.get("adminId") != null ? Long.parseLong(request.get("adminId").toString()) : null;
            
            Optional<Device> deviceOpt = deviceRepository.findById(deviceId);
            if (!deviceOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            
            Device device = deviceOpt.get();
            
            // 创建维修记录
            DeviceRepair repair = new DeviceRepair();
            repair.setDevice(device);
            repair.setRepairTime(LocalDateTime.now());
            repair.setRepairReason("管理员确认维修");
            if (adminId != null) {
                repair.setRepairPersonId(adminId);
            }
            
            deviceRepairRepository.save(repair);
            
            // 更新设备状态为"修理中"(ID=6)
            Optional<com.example.demo20250620.entity.Devicestate> stateOpt = devicestateRepository.findById(6L);
            if (stateOpt.isPresent()) {
                device.setDevicestate(stateOpt.get());
                deviceRepository.save(device);
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "维修已确认，设备状态已更新为修理中");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "确认维修失败: " + e.getMessage());
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