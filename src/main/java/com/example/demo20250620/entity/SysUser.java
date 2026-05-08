package com.example.demo20250620.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class SysUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sysusername;
    private String sysuserpassword;

    @OneToMany(mappedBy = "sysUser")
    private List<DeviceRecord> deviceRecordLists;

    private Long sysuserrole;   //1管理员  2普通用户

    public String getSysusername() {
        return sysusername;
    }

    public void setSysusername(String sysusername) {
        this.sysusername = sysusername;
    }

    public String getSysuserpassword() {
        return sysuserpassword;
    }

    public void setSysuserpassword(String sysuserpassword) {
        this.sysuserpassword = sysuserpassword;
    }

    public Long getId() {
        return id;
    }

    public Long getSysuserrole() {
        return sysuserrole;
    }

    public void setSysuserrole(Long sysuserrole) {
        this.sysuserrole = sysuserrole;
    }
}
