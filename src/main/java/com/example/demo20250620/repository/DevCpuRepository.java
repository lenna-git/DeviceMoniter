package com.example.demo20250620.repository;

import com.example.demo20250620.entity.DevCpu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DevCpuRepository extends JpaRepository<DevCpu, Long> {
    Optional<DevCpu> findByCpuname(String cpuname);
}