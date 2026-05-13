package com.example.demo20250620.controller;


import com.example.demo20250620.entity.SysUser;
import com.example.demo20250620.repository.SysUserRepository;
import com.example.demo20250620.util.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/sysuseraction/")
public class SysUserController {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private final HttpServletRequest request;


    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);
    private boolean success;

    private String message;



    public SysUserController(HttpServletRequest request) {
        this.request = request;
    }


    @CrossOrigin(origins = "http://127.0.0.1:8080")

    @GetMapping("/allusers")
    public List<SysUser> getAllSysUsers() {
        return sysUserRepository.findAll();
    }

    @GetMapping("/login")
    public Optional<SysUser> login(@RequestParam String sysusername, @RequestParam String sysuserpassword,HttpSession session) {
        Optional<SysUser> sysuser = sysUserRepository.findUserBySysusernameAndSysuserpassword(sysusername, sysuserpassword);
//        System.out.print( sysuser.isPresent());
//        HttpServletRequest request = ServletActionContext.getRequest();
//        HttpSession session = request.getSession();
        session.setAttribute("SYS_USER",sysuser);
//        javax.servlet.http.HttpServletRequest httpRequest = (HttpServletRequest) request;
        return sysuser;

    }

    @GetMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.print( "in sysusreraction logout:");
        logger.info("logout:");
        Map<String, Object> responseObj = new HashMap<>();
        // 获取当前 session
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                // 清除 session 中特定的用户记录，假设用户记录的键名为 "user"
                session.removeAttribute("SYS_USER");
                // 使 session 失效
                session.invalidate();
            }
            responseObj.put("message", "登出成功，已清除用户信息");
            responseObj.put("success", true);

        }catch (Exception e){
            responseObj.put("message", "验证登录信息出错，请稍后重试。");
            responseObj.put("success", false);

        }
        return responseObj;
    }

    @PostMapping("/createuser")
    public Map<String, Object> createUser(@RequestBody SysUser sysUser) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            sysUserRepository.save(sysUser);
            responseObj.put("success", true);
            responseObj.put("message", "用户创建成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "用户创建失败: " + e.getMessage());
        }
        return responseObj;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


