package com.example.demo20250620.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Device {
    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String devicexp;//设备芯片
    private String devicetype;//设备类型
    private String devicexh;//设备型号

    private String devicecs;//设备厂商
    private String devicesn;//设备序列号
    private String deviceno;//设备编号

    private String devicescdata;//设备送测日期
    private String deviceajdata;//设备安检日期
    private String deviceghdata;//设备归还厂商日期
    private String deviceyh;//设备借用人
    private String devicestate;//设备状态
    private String deviceop;//设备操作




    @OneToMany(mappedBy = "device")
    private List<DeviceRecord> deviceRecordLists;









    public String getDevicexp() {
        return devicexp;
    }

    public void setDevicexp(String devicexp) {
        this.devicexp = devicexp;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getDevicexh() {
        return devicexh;
    }

    public void setDevicexh(String devicexh) {
        this.devicexh = devicexh;
    }

    public String getDevicecs() {
        return devicecs;
    }

    public void setDevicecs(String devicecs) {
        this.devicecs = devicecs;
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

    public String getDevicescdata() {
        return devicescdata;
    }

    public void setDevicescdata(String devicescdata) {
        this.devicescdata = devicescdata;
    }

    public String getDeviceajdata() {
        return deviceajdata;
    }

    public void setDeviceajdata(String deviceajdata) {
        this.deviceajdata = deviceajdata;
    }

    public String getDeviceghdata() {
        return deviceghdata;
    }

    public void setDeviceghdata(String deviceghdata) {
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


//    public List<DeviceRecord> getDeviceRecordLists() {
//        return deviceRecordLists;
//    }
//
//    public void setDeviceRecordLists(List<DeviceRecord> deviceRecordLists) {
//        this.deviceRecordLists = deviceRecordLists;
//    }
}
