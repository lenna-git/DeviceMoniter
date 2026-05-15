package com.example.demo20250620.repository;

import com.example.demo20250620.entity.Devicestate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevicestateRepository extends JpaRepository<Devicestate, Long> {
}