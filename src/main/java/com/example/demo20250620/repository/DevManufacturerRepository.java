package com.example.demo20250620.repository;

import com.example.demo20250620.entity.DevManufacturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DevManufacturerRepository extends JpaRepository<DevManufacturer, Long> {
    Optional<DevManufacturer> findByManufacturername(String manufacturername);
}