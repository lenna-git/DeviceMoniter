package com.example.demo20250620.repository;

import com.example.demo20250620.entity.DeviceTransferRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceTransferRecordRepository extends JpaRepository<DeviceTransferRecord, Long> {
    
    List<DeviceTransferRecord> findByDeviceId(Long deviceId);
    
    List<DeviceTransferRecord> findByToUserIdAndStatus(Long toUserId, Integer status);
    
    List<DeviceTransferRecord> findByStatus(Integer status);
    
    DeviceTransferRecord findByDeviceIdAndStatus(Long deviceId, Integer status);
}