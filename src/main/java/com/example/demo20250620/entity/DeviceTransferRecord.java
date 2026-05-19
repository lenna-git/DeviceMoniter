package com.example.demo20250620.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class DeviceTransferRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")
    private Device device;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_user_id")
    private SysUser fromUser;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_user_id")
    private SysUser toUser;
    
    private LocalDateTime transferDate;
    
    private LocalDateTime approvalDate;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_approval_user_id")
    private SysUser adminApprovalUser;
    
    private LocalDateTime adminApprovalDate;
    
    private Integer status;
    
    private String detail;
    
    @Transient
    private String fromUsername;
    
    @Transient
    private String toUsername;
    
    @Transient
    private String adminApprovalUsername;
    
    @Transient
    private String statusText;

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

    public SysUser getFromUser() {
        return fromUser;
    }

    public void setFromUser(SysUser fromUser) {
        this.fromUser = fromUser;
    }

    public SysUser getToUser() {
        return toUser;
    }

    public void setToUser(SysUser toUser) {
        this.toUser = toUser;
    }

    public LocalDateTime getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDateTime transferDate) {
        this.transferDate = transferDate;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public SysUser getAdminApprovalUser() {
        return adminApprovalUser;
    }

    public void setAdminApprovalUser(SysUser adminApprovalUser) {
        this.adminApprovalUser = adminApprovalUser;
    }

    public LocalDateTime getAdminApprovalDate() {
        return adminApprovalDate;
    }

    public void setAdminApprovalDate(LocalDateTime adminApprovalDate) {
        this.adminApprovalDate = adminApprovalDate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getToUsername() {
        return toUsername;
    }

    public void setToUsername(String toUsername) {
        this.toUsername = toUsername;
    }

    public String getAdminApprovalUsername() {
        return adminApprovalUsername;
    }

    public void setAdminApprovalUsername(String adminApprovalUsername) {
        this.adminApprovalUsername = adminApprovalUsername;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}