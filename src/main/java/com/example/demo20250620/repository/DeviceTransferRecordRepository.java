package com.example.demo20250620.repository;

import com.example.demo20250620.entity.DeviceTransferRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceTransferRecordRepository extends JpaRepository<DeviceTransferRecord, Long> {
    
    List<DeviceTransferRecord> findByDeviceId(Long deviceId);
    
    List<DeviceTransferRecord> findByToUserIdAndStatus(Long toUserId, Integer status);
    
    List<DeviceTransferRecord> findByStatus(Integer status);
    
    DeviceTransferRecord findByDeviceIdAndStatus(Long deviceId, Integer status);
    
    @Query("SELECT d FROM DeviceTransferRecord d LEFT JOIN FETCH d.device dev LEFT JOIN FETCH dev.devCpu LEFT JOIN FETCH dev.devType LEFT JOIN FETCH dev.devManufacturer LEFT JOIN FETCH d.fromUser LEFT JOIN FETCH d.toUser LEFT JOIN FETCH d.adminApprovalUser WHERE d.id > 0")
    List<DeviceTransferRecord> findAllWithDetails();
    
    @Query("SELECT d FROM DeviceTransferRecord d LEFT JOIN FETCH d.device dev LEFT JOIN FETCH dev.devCpu LEFT JOIN FETCH dev.devType LEFT JOIN FETCH dev.devManufacturer LEFT JOIN FETCH d.fromUser LEFT JOIN FETCH d.toUser LEFT JOIN FETCH d.adminApprovalUser WHERE d.id > 0")
    Page<DeviceTransferRecord> findAllWithDetails(Pageable pageable);
    
    @Query("SELECT d FROM DeviceTransferRecord d LEFT JOIN FETCH d.device dev LEFT JOIN FETCH dev.devCpu LEFT JOIN FETCH dev.devType LEFT JOIN FETCH dev.devManufacturer LEFT JOIN FETCH d.fromUser LEFT JOIN FETCH d.toUser LEFT JOIN FETCH d.adminApprovalUser WHERE d.id > 0 AND (dev.deviceno LIKE %:keyword% OR d.fromUser.sysusername LIKE %:keyword% OR d.toUser.sysusername LIKE %:keyword%)")
    List<DeviceTransferRecord> findByKeywordWithDetails(@Param("keyword") String keyword);
    
    @Query("SELECT d FROM DeviceTransferRecord d LEFT JOIN FETCH d.device dev LEFT JOIN FETCH dev.devCpu LEFT JOIN FETCH dev.devType LEFT JOIN FETCH dev.devManufacturer LEFT JOIN FETCH d.fromUser LEFT JOIN FETCH d.toUser LEFT JOIN FETCH d.adminApprovalUser WHERE d.id > 0 AND (dev.deviceno LIKE %:keyword% OR d.fromUser.sysusername LIKE %:keyword% OR d.toUser.sysusername LIKE %:keyword%)")
    Page<DeviceTransferRecord> findByKeywordWithDetails(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT d FROM DeviceTransferRecord d LEFT JOIN FETCH d.device dev LEFT JOIN FETCH dev.devCpu LEFT JOIN FETCH dev.devType LEFT JOIN FETCH dev.devManufacturer LEFT JOIN FETCH d.fromUser LEFT JOIN FETCH d.toUser LEFT JOIN FETCH d.adminApprovalUser WHERE d.id > 0 AND (d.fromUser.id = :userId OR d.toUser.id = :userId)")
    Page<DeviceTransferRecord> findByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT d FROM DeviceTransferRecord d LEFT JOIN FETCH d.device dev LEFT JOIN FETCH dev.devCpu LEFT JOIN FETCH dev.devType LEFT JOIN FETCH dev.devManufacturer LEFT JOIN FETCH d.fromUser LEFT JOIN FETCH d.toUser LEFT JOIN FETCH d.adminApprovalUser WHERE d.id > 0 AND (d.fromUser.id = :userId OR d.toUser.id = :userId) AND (dev.deviceno LIKE %:keyword% OR d.fromUser.sysusername LIKE %:keyword% OR d.toUser.sysusername LIKE %:keyword%)")
    Page<DeviceTransferRecord> findByUserIdAndKeyword(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
}