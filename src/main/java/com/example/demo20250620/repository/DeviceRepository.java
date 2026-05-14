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
    
    @Query("SELECT d FROM Device d LEFT JOIN FETCH d.devType")
    Page<Device> findAllWithDevType(Pageable pageable);
    
    List<Device> findDeviceByDevicexh(String devicexh);

    List<Device> findDeviceByDevicecs(String devicecs);

    List<Device> findDeviceByDevicexhAndDevicecs(String devicexh, String devicecs);

    Page<Device> findDeviceByDevicexh(String devicexh, Pageable pageable);

    Page<Device> findDeviceByDevicecs(String devicecs, Pageable pageable);

    Page<Device> findDeviceByDevicexhAndDevicecs(String devicexh, String devicecs, Pageable pageable);

    @Query("SELECT d FROM Device d LEFT JOIN FETCH d.devType dt WHERE " +
           "(:devicexp IS NULL OR :devicexp = '' OR d.devicexp LIKE %:devicexp%) AND " +
           "(:devicetype IS NULL OR :devicetype = '' OR dt.typename LIKE %:devicetype%) AND " +
           "(:devicexh IS NULL OR :devicexh = '' OR d.devicexh LIKE %:devicexh%) AND " +
           "(:devicecs IS NULL OR :devicecs = '' OR d.devicecs LIKE %:devicecs%)")
    Page<Device> findByMultipleConditions(
            @Param("devicexp") String devicexp,
            @Param("devicetype") String devicetype,
            @Param("devicexh") String devicexh,
            @Param("devicecs") String devicecs,
            Pageable pageable);
}