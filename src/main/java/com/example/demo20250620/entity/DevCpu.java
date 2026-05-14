package com.example.demo20250620.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "devcpu")
public class DevCpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cpuname", nullable = false, unique = true)
    private String cpuname;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpuname() {
        return cpuname;
    }

    public void setCpuname(String cpuname) {
        this.cpuname = cpuname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}