package com.example.demo20250620.controller;

import com.example.demo20250620.entity.DevManufacturer;
import com.example.demo20250620.repository.DevManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/devmanufactureraction/")
public class DevManufacturerController {

    @Autowired
    private DevManufacturerRepository devManufacturerRepository;

    @GetMapping("/allmanufacturers")
    public List<DevManufacturer> getAllManufacturers() {
        return devManufacturerRepository.findAll();
    }

    @GetMapping("/getmanufacturer/{id}")
    public Optional<DevManufacturer> getManufacturerById(@PathVariable Long id) {
        return devManufacturerRepository.findById(id);
    }

    @PostMapping("/createmanufacturer")
    public Map<String, Object> createManufacturer(@RequestBody DevManufacturer devManufacturer) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<DevManufacturer> existingManufacturer = devManufacturerRepository.findByManufacturername(devManufacturer.getManufacturername());
            if (existingManufacturer.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "厂商名称已存在");
                return responseObj;
            }
            devManufacturerRepository.save(devManufacturer);
            responseObj.put("success", true);
            responseObj.put("message", "厂商创建成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "厂商创建失败: " + e.getMessage());
        }
        return responseObj;
    }

    @PutMapping("/updatemanufacturer")
    public Map<String, Object> updateManufacturer(@RequestBody DevManufacturer devManufacturer) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            if (!devManufacturerRepository.existsById(devManufacturer.getId())) {
                responseObj.put("success", false);
                responseObj.put("message", "厂商不存在");
                return responseObj;
            }
            devManufacturerRepository.save(devManufacturer);
            responseObj.put("success", true);
            responseObj.put("message", "厂商更新成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "厂商更新失败: " + e.getMessage());
        }
        return responseObj;
    }

    @DeleteMapping("/deletemanufacturer/{id}")
    public Map<String, Object> deleteManufacturer(@PathVariable Long id) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            if (!devManufacturerRepository.existsById(id)) {
                responseObj.put("success", false);
                responseObj.put("message", "厂商不存在");
                return responseObj;
            }
            devManufacturerRepository.deleteById(id);
            responseObj.put("success", true);
            responseObj.put("message", "厂商删除成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "厂商删除失败: " + e.getMessage());
        }
        return responseObj;
    }
}