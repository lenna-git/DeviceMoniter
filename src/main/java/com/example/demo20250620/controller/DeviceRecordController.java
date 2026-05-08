package com.example.demo20250620.controller;

import com.example.demo20250620.entity.Device;
import com.example.demo20250620.entity.DeviceRecord;
import com.example.demo20250620.entity.User;
import com.example.demo20250620.repository.DeviceRecordRepository;
import com.example.demo20250620.repository.DeviceRepository;
import com.example.demo20250620.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/devicerecord")
public class DeviceRecordController {
    @Autowired
    private DeviceRecordRepository deviceRecordRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeviceRepository deviceRepository;


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

}
