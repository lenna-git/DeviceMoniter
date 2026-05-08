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


    private Long userId;


//    @Column(name = "device_id")
//    private Long DeviceId;
    private String borrorDate;//找获取当前日期的函数搜

    private String returnDate;//空

    private String detail;//详情  随便写

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

//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }


    @Override
    public String toString() {
        return "DeviceRecord{" +
                "id=" + id +
//                ", userId='" + userId + '\'' +
//                ", DeviceId=" + DeviceId +
                ", borrorDate='" + borrorDate + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", detail='" + detail + '\'' +
                '}';
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

//    public Device getDevice() {
//        return device;
//    }
//
//    public void setDevice(Device device) {
//        this.device = device;
//    }
}
