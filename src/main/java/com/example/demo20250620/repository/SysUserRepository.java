package com.example.demo20250620.repository;

import com.example.demo20250620.entity.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;



public interface SysUserRepository  extends JpaRepository<SysUser, Long> {
    Optional<SysUser> findUserBySysusernameAndSysuserpassword(String sysusername, String sysuserpassword);
    Optional<SysUser> findBySysusername(String sysusername);
}
