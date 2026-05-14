package com.example.demo20250620.controller;

import com.example.demo20250620.entity.DevType;
import com.example.demo20250620.repository.DevTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/devtypeaction/")
public class DevTypeController {

    @Autowired
    private DevTypeRepository devTypeRepository;

    @GetMapping("/alltypes")
    public List<DevType> getAllTypes() {
        return devTypeRepository.findAll();
    }

    @GetMapping("/gettype/{id}")
    public Optional<DevType> getTypeById(@PathVariable Long id) {
        return devTypeRepository.findById(id);
    }

    @PostMapping("/createtype")
    public Map<String, Object> createType(@RequestBody DevType devType) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<DevType> existingType = devTypeRepository.findByTypename(devType.getTypename());
            if (existingType.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "类型名称已存在");
                return responseObj;
            }
            devTypeRepository.save(devType);
            responseObj.put("success", true);
            responseObj.put("message", "类型创建成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "类型创建失败: " + e.getMessage());
        }
        return responseObj;
    }

    @PutMapping("/updatetype")
    public Map<String, Object> updateType(@RequestBody DevType devType) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            if (!devTypeRepository.existsById(devType.getId())) {
                responseObj.put("success", false);
                responseObj.put("message", "类型不存在");
                return responseObj;
            }
            devTypeRepository.save(devType);
            responseObj.put("success", true);
            responseObj.put("message", "类型更新成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "类型更新失败: " + e.getMessage());
        }
        return responseObj;
    }

    @DeleteMapping("/deletetype/{id}")
    public Map<String, Object> deleteType(@PathVariable Long id) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            if (!devTypeRepository.existsById(id)) {
                responseObj.put("success", false);
                responseObj.put("message", "类型不存在");
                return responseObj;
            }
            devTypeRepository.deleteById(id);
            responseObj.put("success", true);
            responseObj.put("message", "类型删除成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "类型删除失败: " + e.getMessage());
        }
        return responseObj;
    }
}