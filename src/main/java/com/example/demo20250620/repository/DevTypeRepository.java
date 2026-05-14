package com.example.demo20250620.repository;

import com.example.demo20250620.entity.DevType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DevTypeRepository extends JpaRepository<DevType, Long> {
    Optional<DevType> findByTypename(String typename);
}