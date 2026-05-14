package com.example.demo20250620.repository;




import com.example.demo20250620.entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findDeviceByDevicexh(String devicexh);

    List<Device> findDeviceByDevicecs(String devicecs);

    List<Device> findDeviceByDevicexhAndDevicecs(String devicexh, String devicecs);

    Page<Device> findDeviceByDevicexh(String devicexh, Pageable pageable);

    Page<Device> findDeviceByDevicecs(String devicecs, Pageable pageable);

    Page<Device> findDeviceByDevicexhAndDevicecs(String devicexh, String devicecs, Pageable pageable);
}