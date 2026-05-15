package com.example.demo20250620.controller;

import com.example.demo20250620.entity.Device;
import com.example.demo20250620.entity.DeviceRepair;
import com.example.demo20250620.entity.Devicestate;
import com.example.demo20250620.repository.DeviceRepository;
import com.example.demo20250620.repository.DeviceRepairRepository;
import com.example.demo20250620.repository.DevicestateRepository;
import com.example.demo20250620.util.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/deviceaction/")
public class DeviceController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private DevicestateRepository devicestateRepository;
    @Autowired
    private DeviceRepairRepository deviceRepairRepository;

    @Autowired
    private final HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    public DeviceController(HttpServletRequest request) {
        this.request = request;
    }

//    @CrossOrigin(origins = "http://127.0.0.1:8080")

    public boolean EmptyorNot(String s){return s == null || s.isEmpty();}



    @GetMapping("/alldevices")
    public Map<String, Object> getAllDevices(
            @RequestParam(required = false) String devicexp,
            @RequestParam(required = false) String devicetype,
            @RequestParam(required = false) String devicexh,
            @RequestParam(required = false) String devicecs,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            int pageIndex = Math.max(0, page - 1);
            Page<Device> devicePage;
            
            boolean hasXp = !EmptyorNot(devicexp);
            boolean hasType = !EmptyorNot(devicetype);
            boolean hasXh = !EmptyorNot(devicexh);
            boolean hasCs = !EmptyorNot(devicecs);
            
            if (!hasXp && !hasType && !hasXh && !hasCs) {
                devicePage = deviceRepository.findAllWithDevType(PageRequest.of(pageIndex, limit));
            } else {
                devicePage = deviceRepository.findByMultipleConditions(
                    devicexp, devicetype, devicexh, devicecs, PageRequest.of(pageIndex, limit));
            }
            
            responseObj.put("data", devicePage.getContent());
            responseObj.put("total", devicePage.getTotalElements());
            responseObj.put("success", true);
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "获取设备列表失败: " + e.getMessage());
        }
        return responseObj;
    }

    @PostMapping("/createdevice")
    public Map<String, Object> createDevice(@RequestBody Device device){
        Map<String, Object> responseObj = new HashMap<>();
        try {
            device.setDevicescdata(LocalDateTime.now());
            device.setDeviceajdata(null);
            device.setDeviceghdata(null);
            
            Devicestate state = new Devicestate();
            state.setId(1L);
            device.setDevicestate(state);
            
            deviceRepository.save(device);
            responseObj.put("success", true);
            responseObj.put("message", "设备创建成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备创建失败: " + e.getMessage());
        }
        return responseObj;
    }

    @DeleteMapping("/deldevices/{id}")
    public void deleteDevice(@PathVariable Long id){deviceRepository.deleteById(id);}
    
    @PutMapping("/checkdevice/{id}")
    public Map<String, Object> checkDevice(@PathVariable Long id){
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<Device> device1 = deviceRepository.findById(id);
            if (!device1.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            Device device2 = device1.get();
            device2.setDeviceajdata(LocalDateTime.now());
            
            Devicestate state = new Devicestate();
            state.setId(2L);
            device2.setDevicestate(state);
            
            deviceRepository.save(device2);
            responseObj.put("success", true);
            responseObj.put("message", "设备安检成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备安检失败: " + e.getMessage());
        }
        return responseObj;
    }
    
    @PutMapping("/returndevice/{id}")
    public Map<String, Object> returnDevice(@PathVariable Long id){
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<Device> device1 = deviceRepository.findById(id);
            if (!device1.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            Device device2 = device1.get();
            device2.setDeviceghdata(LocalDateTime.now());
            
            Optional<Devicestate> stateOpt = devicestateRepository.findById(10L);
            if (stateOpt.isPresent()) {
                device2.setDevicestate(stateOpt.get());
            } else {
                responseObj.put("success", false);
                responseObj.put("message", "设备状态不存在");
                return responseObj;
            }
            
            deviceRepository.save(device2);
            responseObj.put("success", true);
            responseObj.put("message", "设备归还成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备归还失败: " + e.getMessage());
        }
        return responseObj;
    }
    
    @PutMapping("/repairdevice/{id}")
    public Map<String, Object> repairDevice(@PathVariable Long id){
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<Device> device1 = deviceRepository.findById(id);
            if (!device1.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            Device device2 = device1.get();
            
            Optional<Devicestate> stateOpt = devicestateRepository.findById(6L);
            if (stateOpt.isPresent()) {
                device2.setDevicestate(stateOpt.get());
            } else {
                responseObj.put("success", false);
                responseObj.put("message", "设备状态不存在");
                return responseObj;
            }
            
            deviceRepository.save(device2);
            responseObj.put("success", true);
            responseObj.put("message", "设备维修状态更新成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备维修状态更新失败: " + e.getMessage());
        }
        return responseObj;
    }
    
    @PutMapping("/unshelvedevice/{id}")
    public Map<String, Object> unshelveDevice(@PathVariable Long id, @RequestBody(required = false) Map<String, String> request){
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<Device> device1 = deviceRepository.findById(id);
            if (!device1.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            Device device2 = device1.get();
            
            Optional<Devicestate> stateOpt = devicestateRepository.findById(2L);
            if (stateOpt.isPresent()) {
                device2.setDevicestate(stateOpt.get());
            } else {
                responseObj.put("success", false);
                responseObj.put("message", "设备状态不存在");
                return responseObj;
            }
            
            deviceRepository.save(device2);
            
            List<DeviceRepair> repairs = deviceRepairRepository.findByDeviceId(id);
            for (DeviceRepair repair : repairs) {
                if (repair.getEndRepairTime() == null) {
                    repair.setEndRepairTime(LocalDateTime.now());
                    if (request != null && request.containsKey("repairRecord")) {
                        repair.setRepairRecord(request.get("repairRecord"));
                    }
                    deviceRepairRepository.save(repair);
                }
            }
            
            responseObj.put("success", true);
            responseObj.put("message", "设备上架成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备上架失败: " + e.getMessage());
        }
        return responseObj;
    }

    @PutMapping("updatedevicebyid/{id}")
    public Map<String, Object> updateDevice(@PathVariable Long id, @RequestBody Device device){
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<Device> device1 = deviceRepository.findById(id);
            if (!device1.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "设备不存在");
                return responseObj;
            }
            Device device2 = device1.get();
            device2.setDevCpu(device.getDevCpu());
            device2.setDevType(device.getDevType());
            device2.setDevicexh(device.getDevicexh());
            device2.setDevManufacturer(device.getDevManufacturer());
            device2.setDevicesn(device.getDevicesn());
            device2.setDeviceno(device.getDeviceno());
            device2.setDevicescdata(device.getDevicescdata());
            device2.setDeviceajdata(device.getDeviceajdata());
            device2.setDeviceghdata(device.getDeviceghdata());
            device2.setDeviceyh(device.getDeviceyh());
            device2.setDevicestate(device.getDevicestate());

            deviceRepository.save(device2);
            responseObj.put("success", true);
            responseObj.put("message", "设备更新成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "设备更新失败: " + e.getMessage());
        }
        return responseObj;
    }




}
