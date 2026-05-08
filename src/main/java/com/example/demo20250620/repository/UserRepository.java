package com.example.demo20250620.repository;




import com.example.demo20250620.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}