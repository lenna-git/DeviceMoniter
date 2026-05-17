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

    @Query("SELECT dr FROM DeviceRecord dr LEFT JOIN FETCH dr.device d LEFT JOIN FETCH d.devCpu LEFT JOIN FETCH d.devType LEFT JOIN FETCH d.devManufacturer LEFT JOIN FETCH dr.sysUser")
    List<DeviceRecord> findAllWithDeviceAndUser();

    @Query("SELECT dr FROM DeviceRecord dr WHERE dr.device.id = :deviceId AND dr.borrorDate IS NOT NULL AND dr.approvalDate IS NULL")
    Optional<DeviceRecord> findPendingBorrowRecord(@Param("deviceId") Long deviceId);

    @Query("SELECT dr FROM DeviceRecord dr WHERE dr.device.id = :deviceId AND dr.borrorDate IS NOT NULL AND dr.approvalDate IS NOT NULL AND dr.returnDate IS NULL")
    Optional<DeviceRecord> findActiveBorrowRecord(@Param("deviceId") Long deviceId);

    @Query("SELECT dr FROM DeviceRecord dr LEFT JOIN FETCH dr.device d LEFT JOIN FETCH d.devCpu LEFT JOIN FETCH d.devType LEFT JOIN FETCH d.devManufacturer LEFT JOIN FETCH dr.sysUser WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "d.deviceno LIKE %:keyword% OR " +
           "dr.detail LIKE %:keyword%)")
    List<DeviceRecord> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT dr FROM DeviceRecord dr LEFT JOIN FETCH dr.device d LEFT JOIN FETCH d.devCpu LEFT JOIN FETCH d.devType LEFT JOIN FETCH d.devManufacturer LEFT JOIN FETCH dr.sysUser LEFT JOIN SysUser u ON dr.userId = u.id WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR " +
           "d.deviceno LIKE %:keyword% OR " +
           "dr.detail LIKE %:keyword% OR " +
           "u.sysusername LIKE %:keyword%)")
    List<DeviceRecord> findByKeywordWithUsername(@Param("keyword") String keyword);

}