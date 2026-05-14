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
    
    @Query("SELECT d FROM Device d LEFT JOIN FETCH d.devType LEFT JOIN FETCH d.devCpu LEFT JOIN FETCH d.devManufacturer")
    Page<Device> findAllWithDevType(Pageable pageable);
    
    List<Device> findDeviceByDevicexh(String devicexh);

    Page<Device> findDeviceByDevicexh(String devicexh, Pageable pageable);

    @Query("SELECT d FROM Device d LEFT JOIN FETCH d.devType dt LEFT JOIN FETCH d.devCpu dc LEFT JOIN FETCH d.devManufacturer dm WHERE " +
           "(:devicexp IS NULL OR :devicexp = '' OR dc.cpuname LIKE %:devicexp%) AND " +
           "(:devicetype IS NULL OR :devicetype = '' OR dt.typename LIKE %:devicetype%) AND " +
           "(:devicexh IS NULL OR :devicexh = '' OR d.devicexh LIKE %:devicexh%) AND " +
           "(:devicecs IS NULL OR :devicecs = '' OR dm.manufacturername LIKE %:devicecs%)")
    Page<Device> findByMultipleConditions(
            @Param("devicexp") String devicexp,
            @Param("devicetype") String devicetype,
            @Param("devicexh") String devicexh,
            @Param("devicecs") String devicecs,
            Pageable pageable);
}