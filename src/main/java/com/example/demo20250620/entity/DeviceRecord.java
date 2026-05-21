package com.example.demo20250620.entity;

import jakarta.persistence.*;

@Entity
public class DeviceRecord {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sysUser_id")
    private SysUser sysUser;




    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

//,referencedColumnName = "id"


    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private SysUser borrowUser;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "returnApprovalUserId", insertable = false, updatable = false)
    private SysUser returnApprovalUser;

    private Long returnApprovalUserId;
    private String borrorDate;
    private String approvalDate;
    private String returnDate;
    private String returnApprovalDate;
    private String detail;
    
    @Transient
    private String borrowerUsername;//借用人用户名
    
    @Transient
    private String returnApprovalUsername;//归还批准人用户名

//-------------------------------------------
//    @OneToOne
//    @JoinColumn(name = "id")
//    private Device device;
//
//    @OneToOne
//    @JoinColumn(name = "id")
//    private User user;
//-------------------------------------------

//    public Long getDeviceId() {
//        return DeviceId;
//    }
//
//    public void setDeviceId(Long deviceId) {
//        DeviceId = deviceId;
//    }




    public Long getId() {
        return id;
    }



    public String getBorrorDate() {
        return borrorDate;
    }

    public void setBorrorDate(String borrorDate) {
        this.borrorDate = borrorDate;
    }

    public String getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(String approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }


    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "DeviceRecord{" +
                "id=" + id +
                ", borrorDate='" + borrorDate + '\'' +
                ", approvalDate='" + approvalDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", returnApprovalUserId=" + returnApprovalUserId +
                ", returnApprovalDate='" + returnApprovalDate + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }

    public Long getReturnApprovalUserId() {
        return returnApprovalUserId;
    }

    public void setReturnApprovalUserId(Long returnApprovalUserId) {
        this.returnApprovalUserId = returnApprovalUserId;
    }

    public String getReturnApprovalDate() {
        return returnApprovalDate;
    }

    public void setReturnApprovalDate(String returnApprovalDate) {
        this.returnApprovalDate = returnApprovalDate;
    }


//    public Device getDevice() {
//        return device;
//    }
//
//    public void setDevice(Device device) {
//        this.device = device;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }


    public SysUser getSysUser() {
        return sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public SysUser getBorrowUser() {
        return borrowUser;
    }

    public void setBorrowUser(SysUser borrowUser) {
        this.borrowUser = borrowUser;
    }

    public SysUser getReturnApprovalUser() {
        return returnApprovalUser;
    }

    public void setReturnApprovalUser(SysUser returnApprovalUser) {
        this.returnApprovalUser = returnApprovalUser;
    }

    public String getBorrowerUsername() {
        return borrowerUsername;
    }

    public void setBorrowerUsername(String borrowerUsername) {
        this.borrowerUsername = borrowerUsername;
    }

    public String getReturnApprovalUsername() {
        return returnApprovalUsername;
    }

    public void setReturnApprovalUsername(String returnApprovalUsername) {
        this.returnApprovalUsername = returnApprovalUsername;
    }

//    public Device getDevice() {
//        return device;
//    }
//
//    public void setDevice(Device device) {
//        this.device = device;
//    }
}
