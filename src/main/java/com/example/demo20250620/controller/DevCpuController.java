package com.example.demo20250620.controller;

import com.example.demo20250620.entity.DevCpu;
import com.example.demo20250620.repository.DevCpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/devcpuaction/")
public class DevCpuController {

    @Autowired
    private DevCpuRepository devCpuRepository;

    @GetMapping("/allcpus")
    public List<DevCpu> getAllCpus() {
        return devCpuRepository.findAll();
    }

    @GetMapping("/getcpu/{id}")
    public Optional<DevCpu> getCpuById(@PathVariable Long id) {
        return devCpuRepository.findById(id);
    }

    @PostMapping("/createcpu")
    public Map<String, Object> createCpu(@RequestBody DevCpu devCpu) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<DevCpu> existingCpu = devCpuRepository.findByCpuname(devCpu.getCpuname());
            if (existingCpu.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "CPU名称已存在");
                return responseObj;
            }
            devCpuRepository.save(devCpu);
            responseObj.put("success", true);
            responseObj.put("message", "CPU创建成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "CPU创建失败: " + e.getMessage());
        }
        return responseObj;
    }

    @PutMapping("/updatecpu")
    public Map<String, Object> updateCpu(@RequestBody DevCpu devCpu) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            if (!devCpuRepository.existsById(devCpu.getId())) {
                responseObj.put("success", false);
                responseObj.put("message", "CPU不存在");
                return responseObj;
            }
            devCpuRepository.save(devCpu);
            responseObj.put("success", true);
            responseObj.put("message", "CPU更新成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "CPU更新失败: " + e.getMessage());
        }
        return responseObj;
    }

    @DeleteMapping("/deletecpu/{id}")
    public Map<String, Object> deleteCpu(@PathVariable Long id) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            if (!devCpuRepository.existsById(id)) {
                responseObj.put("success", false);
                responseObj.put("message", "CPU不存在");
                return responseObj;
            }
            devCpuRepository.deleteById(id);
            responseObj.put("success", true);
            responseObj.put("message", "CPU删除成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "CPU删除失败: " + e.getMessage());
        }
        return responseObj;
    }
}