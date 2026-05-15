package com.example.demo20250620.controller;

import com.example.demo20250620.entity.Devicestate;
import com.example.demo20250620.repository.DevicestateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/devicestateaction")
public class DevicestateController {

    @Autowired
    private DevicestateRepository devicestateRepository;

    @GetMapping("/allstates")
    public List<Devicestate> getAllStates() {
        return devicestateRepository.findAll();
    }
}