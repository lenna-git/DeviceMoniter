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

import java.util.List;
import java.util.Optional;

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
//    public List<Device> getAllUsers() { return deviceRepository.findAll();}//另一种用service
    public List<Device> getAllDevices(@RequestParam(required = false) String devicexh,String devicecs){
        if (EmptyorNot(devicexh)&&EmptyorNot(devicecs)){
            return deviceRepository.findAll();
        }else if(!EmptyorNot(devicexh)&&EmptyorNot(devicecs)) {
            return deviceRepository.findDeviceByDevicexh(devicexh);
        }else if(EmptyorNot(devicexh)&&!EmptyorNot(devicecs)){
            return deviceRepository.findDeviceByDevicecs(devicecs);
        }else{
            return deviceRepository.findDeviceByDevicexhAndDevicecs(devicexh,devicecs);
        }
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
