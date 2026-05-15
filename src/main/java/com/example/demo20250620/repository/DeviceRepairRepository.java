package com.example.demo20250620.repository;

import com.example.demo20250620.entity.DeviceRepair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepairRepository extends JpaRepository<DeviceRepair, Long> {
    List<DeviceRepair> findByDeviceId(Long deviceId);
}