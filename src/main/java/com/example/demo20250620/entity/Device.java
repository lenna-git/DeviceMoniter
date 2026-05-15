package com.example.demo20250620.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Device {
    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "devcpu_id")
    private DevCpu devCpu;//设备芯片
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "devtype_id")
    private DevType devType;//设备类型
    
    private String devicexh;//设备型号

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "devmanufacturer_id")
    private DevManufacturer devManufacturer;//设备厂商
    
    private String devicesn;//设备序列号
    private String deviceno;//设备编号

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime devicescdata;//设备送测日期
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime deviceajdata;//设备安检日期
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime deviceghdata;//设备归还厂商日期
    private String deviceyh;//设备借用人
    private String devicestate;//设备状态
    private String deviceop;//设备操作




    @OneToMany(mappedBy = "device")
    private List<DeviceRecord> deviceRecordLists;




    public DevCpu getDevCpu() {
        return devCpu;
    }

    public void setDevCpu(DevCpu devCpu) {
        this.devCpu = devCpu;
    }

    public DevType getDevType() {
        return devType;
    }

    public void setDevType(DevType devType) {
        this.devType = devType;
    }

    public String getDevicexh() {
        return devicexh;
    }

    public void setDevicexh(String devicexh) {
        this.devicexh = devicexh;
    }

    public DevManufacturer getDevManufacturer() {
        return devManufacturer;
    }

    public void setDevManufacturer(DevManufacturer devManufacturer) {
        this.devManufacturer = devManufacturer;
    }

    public String getDevicesn() {
        return devicesn;
    }

    public void setDevicesn(String devicesn) {
        this.devicesn = devicesn;
    }

    public String getDeviceno() {
        return deviceno;
    }

    public void setDeviceno(String deviceno) {
        this.deviceno = deviceno;
    }

    public LocalDateTime getDevicescdata() {
        return devicescdata;
    }

    public void setDevicescdata(LocalDateTime devicescdata) {
        this.devicescdata = devicescdata;
    }

    public LocalDateTime getDeviceajdata() {
        return deviceajdata;
    }

    public void setDeviceajdata(LocalDateTime deviceajdata) {
        this.deviceajdata = deviceajdata;
    }

    public LocalDateTime getDeviceghdata() {
        return deviceghdata;
    }

    public void setDeviceghdata(LocalDateTime deviceghdata) {
        this.deviceghdata = deviceghdata;
    }

    public String getDeviceyh() {
        return deviceyh;
    }

    public void setDeviceyh(String deviceyh) {
        this.deviceyh = deviceyh;
    }

    public String getDevicestate() {
        return devicestate;
    }

    public void setDevicestate(String devicestate) {
        this.devicestate = devicestate;
    }

    public String getDeviceop() {
        return deviceop;
    }

    public void setDeviceop(String deviceop) {
        this.deviceop = deviceop;
    }


    public Long getDeviceid() {
        return id;
    }

    public void setDeviceid(Long id) {
        this.id = id;
    }

//     public DevType getDevType(){
//         return devType;
//     }



//    public List<DeviceRecord> getDeviceRecordLists() {
//        return deviceRecordLists;
//    }
//
//    public void setDeviceRecordLists(List<DeviceRecord> deviceRecordLists) {
//        this.deviceRecordLists = deviceRecordLists;
//    }
}
