package com.example.demo20250620.repository;


import com.example.demo20250620.entity.DeviceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DeviceRecordRepository extends JpaRepository<DeviceRecord, Long> {
//    @Query(value = "select * from device_record where user_name = :UserName",nativeQuery = true)
//    List<DeviceRecord> findByUserId(Long userId);

    List<DeviceRecord> findByDetail(String detail);

//    List<DeviceRecord> findDeviceRecordByUserIdAndDetail(Long userId,String detail);

}