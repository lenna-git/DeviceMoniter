package com.example.demo20250620.controller;

import com.example.demo20250620.entity.Device;
import com.example.demo20250620.repository.DeviceRepository;
import com.example.demo20250620.util.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

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
                devicePage = deviceRepository.findAll(PageRequest.of(pageIndex, limit));
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
    public Device createDevice(@RequestBody Device device){return deviceRepository.save(device);}

    @DeleteMapping("/deldevices/{id}")
    public void deleteDevice(@PathVariable Long id){deviceRepository.deleteById(id);}

    @PutMapping("updatedevicebyid/{id}")
    public void updateDevice(@PathVariable Long id, @RequestBody Device device){
        Optional<Device> device1 = deviceRepository.findById(id);
        Device device2 =device1.orElseGet(() -> new Device());
        device2.setDevicexp(device.getDevicexp());
        device2.setDevicetype(device.getDevicetype());
        device2.setDevicexh(device.getDevicexh());
        device2.setDevicecs(device.getDevicecs());
        device2.setDevicesn(device.getDevicesn());
        device2.setDeviceno(device.getDeviceno());
        device2.setDevicescdata(device.getDevicescdata());
        device2.setDeviceajdata(device.getDeviceajdata());
        device2.setDeviceghdata(device.getDeviceghdata());
        device2.setDeviceyh(device.getDeviceyh());
        device2.setDevicestate(device.getDevicestate());
        device2.setDeviceop(device.getDeviceop());

        deviceRepository.save(device2);
    }




}
