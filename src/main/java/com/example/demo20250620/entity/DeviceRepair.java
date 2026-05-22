package com.example.demo20250620.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "device_repair")
public class DeviceRepair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;
    
    @Column(name = "repair_time", nullable = false)
    private LocalDateTime repairTime;
    
    @Column(name = "end_repair_time")
    private LocalDateTime endRepairTime;
    
    @Column(name = "repair_reason", length = 500)
    private String repairReason;
    
    @Column(name = "repair_record", length = 1000)
    private String repairRecord;
    
    @Column(name = "reporter_id")
    private Long reporterId;
    
    @Column(name = "repair_person_id")
    private Long repairPersonId;
    
    @Column(name = "admin_start_repair_time")
    private LocalDateTime adminStartRepairTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public LocalDateTime getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(LocalDateTime repairTime) {
        this.repairTime = repairTime;
    }

    public LocalDateTime getEndRepairTime() {
        return endRepairTime;
    }

    public void setEndRepairTime(LocalDateTime endRepairTime) {
        this.endRepairTime = endRepairTime;
    }

    public String getRepairReason() {
        return repairReason;
    }

    public void setRepairReason(String repairReason) {
        this.repairReason = repairReason;
    }

    public String getRepairRecord() {
        return repairRecord;
    }

    public void setRepairRecord(String repairRecord) {
        this.repairRecord = repairRecord;
    }

    public Long getReporterId() {
        return reporterId;
    }

    public void setReporterId(Long reporterId) {
        this.reporterId = reporterId;
    }

    public Long getRepairPersonId() {
        return repairPersonId;
    }

    public void setRepairPersonId(Long repairPersonId) {
        this.repairPersonId = repairPersonId;
    }

    public LocalDateTime getAdminStartRepairTime() {
        return adminStartRepairTime;
    }

    public void setAdminStartRepairTime(LocalDateTime adminStartRepairTime) {
        this.adminStartRepairTime = adminStartRepairTime;
    }
}