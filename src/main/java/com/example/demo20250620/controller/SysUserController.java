package com.example.demo20250620.controller;


import com.example.demo20250620.entity.PasswordResetToken;
import com.example.demo20250620.entity.SysUser;
import com.example.demo20250620.repository.PasswordResetTokenRepository;
import com.example.demo20250620.repository.SysUserRepository;
import com.example.demo20250620.util.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


@RestController
@RequestMapping("/sysuseraction/")
public class SysUserController {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private com.example.demo20250620.service.LogOperationService logOperationService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
    public Map<String, Object> getAllSysUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            int pageIndex = Math.max(0, page - 1);
            Page<SysUser> userPage = sysUserRepository.findAll(PageRequest.of(pageIndex, limit));
            responseObj.put("data", userPage.getContent());
            responseObj.put("total", userPage.getTotalElements());
            responseObj.put("success", true);
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "获取用户列表失败: " + e.getMessage());
        }
        return responseObj;
    }

    @GetMapping("/login")
    public Optional<SysUser> login(@RequestParam String sysusername, @RequestParam String sysuserpassword,HttpSession session) {
        Optional<SysUser> optionalUser = sysUserRepository.findBySysusername(sysusername);
        if (optionalUser.isPresent()) {
            SysUser user = optionalUser.get();
            if (passwordEncoder.matches(sysuserpassword, user.getSysuserpassword())) {
                session.setAttribute("SYS_USER", optionalUser);

                // 记录登录成功日志
                if (logOperationService != null) {
                    logOperationService.logSuccess(
                            user.getId(),
                            user.getSysusername(),
                            user.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_USER_LOGIN,
                            com.example.demo20250620.entity.LogOperation.MODULE_USER,
                            "用户登录成功: " + user.getSysusername(),
                            null,
                            null,
                            null,
                            request);
                }

                return optionalUser;
            }
        }

        // 记录登录失败日志
        if (logOperationService != null) {
            logOperationService.logFail(
                    null,
                    sysusername,
                    null,
                    com.example.demo20250620.entity.LogOperation.TYPE_USER_LOGIN,
                    com.example.demo20250620.entity.LogOperation.MODULE_USER,
                    "用户登录失败: " + sysusername,
                    null,
                    null,
                    null,
                    "用户名或密码错误",
                    request);
        }

        return Optional.empty();
    }

    @GetMapping("/logout")
    public Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("=== 退出登录方法被调用 ===");
        logger.info("logout:");
        Map<String, Object> responseObj = new HashMap<>();
        // 获取当前 session
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                // 记录登出日志
                Optional<SysUser> optionalUser = (Optional<SysUser>) session.getAttribute("SYS_USER");
                System.out.println("Session中的用户信息: " + (optionalUser != null ? "存在" : "不存在"));
                if (optionalUser != null && optionalUser.isPresent()) {
                    SysUser user = optionalUser.get();
                    System.out.println("用户ID: " + user.getId() + ", 用户名: " + user.getSysusername());

                    if (logOperationService != null) {
                        System.out.println("logOperationService 可用，准备记录登出日志");
                        logOperationService.logSuccess(
                                user.getId(),
                                user.getSysusername(),
                                user.getSysuserrole().intValue(),
                                com.example.demo20250620.entity.LogOperation.TYPE_USER_LOGOUT,
                                com.example.demo20250620.entity.LogOperation.MODULE_USER,
                                "用户登出成功: " + user.getSysusername(),
                                null,
                                null,
                                null,
                                request);
                        System.out.println("登出日志记录成功");
                    } else {
                        System.out.println("logOperationService 为null");
                    }
                } else {
                    System.out.println("session 中没有用户信息，可能是已过期或未登录");
                }

                // 清除 session 中特定的用户记录，假设用户记录的键名为user
                session.removeAttribute("SYS_USER");
                // 使 session 失效
                session.invalidate();
            } else {
                System.out.println("session 为null");
            }
            responseObj.put("message", "登出成功，已清除用户信息");
            responseObj.put("success", true);

        }catch (Exception e){
            System.out.println("退出登录异常: " + e.getMessage());
            responseObj.put("message", "验证登录信息出错，请稍后重试");
            responseObj.put("success", false);

        }
        return responseObj;
    }

    @PostMapping("/createuser")
    public Map<String, Object> createUser(@RequestBody SysUser sysUser) {
        logger.info("收到创建用户请求: " + sysUser.getSysusername() + ", 角色: " + sysUser.getSysuserrole());
        Map<String, Object> responseObj = new HashMap<>();
        HttpSession session = request.getSession(false);
        Optional<SysUser> currentUser = Optional.empty();
        if (session != null) {
            currentUser = (Optional<SysUser>) session.getAttribute("SYS_USER");
        }
        try {
            Optional<SysUser> existingUser = sysUserRepository.findBySysusername(sysUser.getSysusername());
            if (existingUser.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "用户名已存在");
                // 记录新增用户失败日志 - 用户名已存在
                if (currentUser != null && currentUser.isPresent()) {
                    SysUser admin = currentUser.get();
                    if (logOperationService != null) {
                        logOperationService.logFail(
                                admin.getId(),
                                admin.getSysusername(),
                                admin.getSysuserrole().intValue(),
                                com.example.demo20250620.entity.LogOperation.TYPE_USER_CREATE,
                                com.example.demo20250620.entity.LogOperation.MODULE_USER,
                                "管理员【" + admin.getSysusername() + "】新增用户【" + sysUser.getSysusername() + "】失败：用户名已存在",
                                null,
                                null,
                                sysUser.getSysusername(),
                                "用户名已存在",
                                request);
                    }
                }
                return responseObj;
            }

            // 使用BCrypt加密密码
            if (sysUser.getSysuserpassword() != null && !sysUser.getSysuserpassword().isEmpty()) {
                sysUser.setSysuserpassword(passwordEncoder.encode(sysUser.getSysuserpassword()));
            }

            sysUserRepository.save(sysUser);
            sysUserRepository.flush();
            logger.info("用户已保存到数据库: " + sysUser.getSysusername() + ", ID: " + sysUser.getId());

            if (currentUser != null && currentUser.isPresent()) {
                SysUser admin = currentUser.get();
                if (logOperationService != null) {
                    logOperationService.logSuccess(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_USER_CREATE,
                            com.example.demo20250620.entity.LogOperation.MODULE_USER,
                            "管理员【" + admin.getSysusername() + "】新增用户【" + sysUser.getSysusername() + "】",
                            null,
                            sysUser.getId(),
                            sysUser.getSysusername(),
                            request);
                }
            }

            responseObj.put("success", true);
            responseObj.put("message", "用户创建成功");
        } catch (Exception e) {
            logger.error("创建用户异常: " + e.getMessage(), e);
            responseObj.put("success", false);
            responseObj.put("message", "用户创建失败: " + e.getMessage());
            // 记录新增用户失败日志 - 异常
            if (currentUser != null && currentUser.isPresent()) {
                SysUser admin = currentUser.get();
                if (logOperationService != null) {
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_USER_CREATE,
                            com.example.demo20250620.entity.LogOperation.MODULE_USER,
                            "管理员【" + admin.getSysusername() + "】新增用户【" + sysUser.getSysusername() + "】失败：" + e.getMessage(),
                            null,
                            null,
                            sysUser.getSysusername(),
                            e.getMessage(),
                            request);
                }
            }
        }
        return responseObj;
    }

    @DeleteMapping("/deleteuser")
    public Map<String, Object> deleteUser(@RequestParam Long id) {
        Map<String, Object> responseObj = new HashMap<>();
        Optional<SysUser> optionalUserToDelete = Optional.empty();
        HttpSession session = request.getSession(false);
        Optional<SysUser> currentUser = Optional.empty();
        if (session != null) {
            currentUser = (Optional<SysUser>) session.getAttribute("SYS_USER");
        }
        try {
            optionalUserToDelete = sysUserRepository.findById(id);
            if (!optionalUserToDelete.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "用户不存在");
                // 记录删除用户失败日志 - 用户不存在
                if (currentUser != null && currentUser.isPresent()) {
                    SysUser admin = currentUser.get();
                    if (logOperationService != null) {
                        logOperationService.logFail(
                                admin.getId(),
                                admin.getSysusername(),
                                admin.getSysuserrole().intValue(),
                                com.example.demo20250620.entity.LogOperation.TYPE_USER_DELETE,
                                com.example.demo20250620.entity.LogOperation.MODULE_USER,
                                "管理员【" + admin.getSysusername() + "】删除用户失败：用户不存在",
                                null,
                                id,
                                null,
                                "用户不存在",
                                request);
                    }
                }
                return responseObj;
            }
            SysUser userToDelete = optionalUserToDelete.get();
            sysUserRepository.deleteById(id);

            // 记录删除用户成功日志
            if (currentUser != null && currentUser.isPresent()) {
                SysUser admin = currentUser.get();
                if (logOperationService != null) {
                    logOperationService.logSuccess(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_USER_DELETE,
                            com.example.demo20250620.entity.LogOperation.MODULE_USER,
                            "管理员【" + admin.getSysusername() + "】删除用户【" + userToDelete.getSysusername() + "】",
                            null,
                            userToDelete.getId(),
                            userToDelete.getSysusername(),
                            request);
                }
            }

            responseObj.put("success", true);
            responseObj.put("message", "用户删除成功");
        } catch (Exception e) {
            responseObj.put("success", false);
            responseObj.put("message", "用户删除失败: " + e.getMessage());
            // 记录删除用户失败日志 - 异常
            String usernameToDelete = optionalUserToDelete.isPresent() ? optionalUserToDelete.get().getSysusername() : null;
            if (currentUser != null && currentUser.isPresent()) {
                SysUser admin = currentUser.get();
                if (logOperationService != null) {
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_USER_DELETE,
                            com.example.demo20250620.entity.LogOperation.MODULE_USER,
                            "管理员【" + admin.getSysusername() + "】删除用户失败：" + e.getMessage(),
                            null,
                            id,
                            usernameToDelete,
                            e.getMessage(),
                            request);
                }
            }
        }
        return responseObj;
    }

    @PutMapping("/updateuser")
    public Map<String, Object> updateUser(@RequestBody SysUser sysUser) {
        Map<String, Object> responseObj = new HashMap<>();
        HttpSession session = request.getSession(false);
        Optional<SysUser> currentUser = Optional.empty();
        if (session != null) {
            currentUser = (Optional<SysUser>) session.getAttribute("SYS_USER");
        }
        try {
            logger.info("updateUser called with id: " + sysUser.getId());
            logger.info("updateUser called with username: " + sysUser.getSysusername());

            if (sysUser.getId() == null) {
                responseObj.put("success", false);
                responseObj.put("message", "用户ID不能为空");
                return responseObj;
            }

            Optional<SysUser> existingUserOpt = sysUserRepository.findById(sysUser.getId());
            if (!existingUserOpt.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "用户不存在");
                return responseObj;
            }
            SysUser existingUser = existingUserOpt.get();

            Optional<SysUser> userWithSameName = sysUserRepository.findBySysusername(sysUser.getSysusername());
            if (userWithSameName.isPresent() && !userWithSameName.get().getId().equals(sysUser.getId())) {
                responseObj.put("success", false);
                responseObj.put("message", "用户名已存在");
                // 记录修改用户失败日志 - 用户名已存在
                if (currentUser != null && currentUser.isPresent()) {
                    SysUser admin = currentUser.get();
                    if (logOperationService != null) {
                        logOperationService.logFail(
                                admin.getId(),
                                admin.getSysusername(),
                                admin.getSysuserrole().intValue(),
                                com.example.demo20250620.entity.LogOperation.TYPE_USER_UPDATE,
                                com.example.demo20250620.entity.LogOperation.MODULE_USER,
                                "管理员【" + admin.getSysusername() + "】修改用户【" + sysUser.getSysusername() + "】失败：用户名已存在",
                                null,
                                sysUser.getId(),
                                sysUser.getSysusername(),
                                "用户名已存在",
                                request);
                    }
                }
                return responseObj;
            }

            existingUser.setSysusername(sysUser.getSysusername());
            existingUser.setSysuserrole(sysUser.getSysuserrole());
            sysUserRepository.save(existingUser);

            // 记录修改用户成功日志
            if (currentUser != null && currentUser.isPresent()) {
                SysUser admin = currentUser.get();
                if (logOperationService != null) {
                    logOperationService.logSuccess(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_USER_UPDATE,
                            com.example.demo20250620.entity.LogOperation.MODULE_USER,
                            "管理员【" + admin.getSysusername() + "】修改用户【" + sysUser.getSysusername() + "】",
                            null,
                            sysUser.getId(),
                            sysUser.getSysusername(),
                            request);
                }
            }

            responseObj.put("success", true);
            responseObj.put("message", "用户更新成功");
        } catch (Exception e) {
            logger.error("updateUser error: " + e.getMessage(), e);
            responseObj.put("success", false);
            responseObj.put("message", "用户更新失败: " + e.getMessage());
            // 记录修改用户失败日志 - 异常
            if (currentUser != null && currentUser.isPresent()) {
                SysUser admin = currentUser.get();
                if (logOperationService != null) {
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_USER_UPDATE,
                            com.example.demo20250620.entity.LogOperation.MODULE_USER,
                            "管理员【" + admin.getSysusername() + "】修改用户【" + sysUser.getSysusername() + "】失败：" + e.getMessage(),
                            null,
                            sysUser.getId(),
                            sysUser.getSysusername(),
                            e.getMessage(),
                            request);
                }
            }
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

    /**
     * 获取操作员列表（角色为2的用户）
     */
    @GetMapping("/getOperators")
    public List<Map<String, Object>> getOperators() {
        List<SysUser> operators = sysUserRepository.findBySysuserrole(2);
        return operators.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getId());
            map.put("sysusername", user.getSysusername());
            return map;
        }).toList();
    }

    /**
     * 修改密码
     */
    @PostMapping("/changePassword")
    public Map<String, Object> changePassword(
            @RequestParam Long userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        Map<String, Object> responseObj = new HashMap<>();
        HttpSession session = request.getSession(false);
        Optional<SysUser> currentUser = Optional.empty();
        if (session != null) {
            currentUser = (Optional<SysUser>) session.getAttribute("SYS_USER");
        }
        Optional<SysUser> optionalUser = Optional.empty();
        try {
            optionalUser = sysUserRepository.findById(userId);
            if (!optionalUser.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "用户不存在");
                // 记录修改密码失败日志 - 用户不存在
                if (currentUser != null && currentUser.isPresent()) {
                    SysUser admin = currentUser.get();
                    if (logOperationService != null) {
                        logOperationService.logFail(
                                admin.getId(),
                                admin.getSysusername(),
                                admin.getSysuserrole().intValue(),
                                com.example.demo20250620.entity.LogOperation.TYPE_USER_CHANGE_PASSWORD,
                                com.example.demo20250620.entity.LogOperation.MODULE_USER,
                                "用户【" + admin.getSysusername() + "】修改密码失败：用户不存在",
                                null,
                                userId,
                                null,
                                "用户不存在",
                                request);
                    }
                }
                return responseObj;
            }

            SysUser user = optionalUser.get();

            // 使用BCrypt验证原密码
            if (!passwordEncoder.matches(oldPassword, user.getSysuserpassword())) {
                responseObj.put("success", false);
                responseObj.put("message", "原密码不正确");
                // 记录修改密码失败日志 - 原密码不正确
                if (currentUser != null && currentUser.isPresent()) {
                    SysUser admin = currentUser.get();
                    if (logOperationService != null) {
                        logOperationService.logFail(
                                admin.getId(),
                                admin.getSysusername(),
                                admin.getSysuserrole().intValue(),
                                com.example.demo20250620.entity.LogOperation.TYPE_USER_CHANGE_PASSWORD,
                                com.example.demo20250620.entity.LogOperation.MODULE_USER,
                                "用户【" + admin.getSysusername() + "】修改密码失败：原密码不正确",
                                null,
                                userId,
                                admin.getSysusername(),
                                "原密码不正确",
                                request);
                    }
                }
                return responseObj;
            }

            // 使用BCrypt加密新密码
            user.setSysuserpassword(passwordEncoder.encode(newPassword));
            sysUserRepository.save(user);
            sysUserRepository.flush();

            // 记录修改密码成功日志
            if (currentUser != null && currentUser.isPresent()) {
                SysUser admin = currentUser.get();
                if (logOperationService != null) {
                    logOperationService.logSuccess(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_USER_CHANGE_PASSWORD,
                            com.example.demo20250620.entity.LogOperation.MODULE_USER,
                            "用户【" + admin.getSysusername() + "】修改密码成功",
                            null,
                            userId,
                            admin.getSysusername(),
                            request);
                }
            }

            responseObj.put("success", true);
            responseObj.put("message", "密码修改成功");
        } catch (Exception e) {
            logger.error("changePassword error: " + e.getMessage(), e);
            responseObj.put("success", false);
            responseObj.put("message", "密码修改失败: " + e.getMessage());
            // 记录修改密码失败日志 - 异常
            String username = optionalUser.isPresent() ? optionalUser.get().getSysusername() : null;
            if (currentUser != null && currentUser.isPresent()) {
                SysUser admin = currentUser.get();
                if (logOperationService != null) {
                    logOperationService.logFail(
                            admin.getId(),
                            admin.getSysusername(),
                            admin.getSysuserrole().intValue(),
                            com.example.demo20250620.entity.LogOperation.TYPE_USER_CHANGE_PASSWORD,
                            com.example.demo20250620.entity.LogOperation.MODULE_USER,
                            "用户【" + admin.getSysusername() + "】修改密码失败：" + e.getMessage(),
                            null,
                            userId,
                            username,
                            e.getMessage(),
                            request);
                }
            }
        }
        return responseObj;
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody SysUser sysUser) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<SysUser> existingUser = sysUserRepository.findBySysusername(sysUser.getSysusername());
            if (existingUser.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "用户名已存在");
                return responseObj;
            }

            if (sysUser.getSysuserrole() == null) {
                sysUser.setSysuserrole(2L); // 默认注册为操作员
            }

            // 使用BCrypt加密密码
            sysUser.setSysuserpassword(passwordEncoder.encode(sysUser.getSysuserpassword()));

            sysUserRepository.save(sysUser);
            responseObj.put("success", true);
            responseObj.put("message", "注册成功");
        } catch (Exception e) {
            logger.error("register error: " + e.getMessage(), e);
            responseObj.put("success", false);
            responseObj.put("message", "注册失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 忘记密码 - 生成重置令牌
     */
    @PostMapping("/forgotPassword")
    public Map<String, Object> forgotPassword(@RequestBody Map<String, String> requestBody) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            String sysusername = requestBody.get("sysusername");
            Optional<SysUser> optionalUser = sysUserRepository.findBySysusername(sysusername);

            if (!optionalUser.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "用户不存在");
                return responseObj;
            }

            SysUser user = optionalUser.get();

            // 删除用户之前的令牌
            passwordResetTokenRepository.deleteByUserId(user.getId());

            // 生成6位数字验证码
            String token = String.format("%06d", (int)(Math.random() * 900000) + 100000);

            // 保存令牌，有效期5分钟
            PasswordResetToken resetToken = new PasswordResetToken(token, user,
                java.time.LocalDateTime.now().plusMinutes(5));
            passwordResetTokenRepository.save(resetToken);

            // 输出验证码到控制台（实际项目中应发送邮件/短信）
            logger.info("密码重置验证码 - 用户: " + sysusername + ", 验证码: " + token);

            responseObj.put("success", true);
            responseObj.put("message", "验证码已发送");
        } catch (Exception e) {
            logger.error("forgotPassword error: " + e.getMessage(), e);
            responseObj.put("success", false);
            responseObj.put("message", "操作失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPassword")
    public Map<String, Object> resetPassword(@RequestBody Map<String, String> requestBody) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            String token = requestBody.get("token");
            String newPassword = requestBody.get("newPassword");

            Optional<PasswordResetToken> optionalResetToken = passwordResetTokenRepository.findByToken(token);

            if (!optionalResetToken.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "验证码无效");
                return responseObj;
            }

            PasswordResetToken resetToken = optionalResetToken.get();

            if (resetToken.getUsed()) {
                responseObj.put("success", false);
                responseObj.put("message", "验证码已使用");
                return responseObj;
            }

            if (resetToken.isExpired()) {
                responseObj.put("success", false);
                responseObj.put("message", "验证码已过期");
                return responseObj;
            }

            // 更新用户密码
            SysUser user = resetToken.getUser();
            user.setSysuserpassword(passwordEncoder.encode(newPassword));
            sysUserRepository.save(user);

            // 标记令牌已使用
            resetToken.setUsed(true);
            passwordResetTokenRepository.save(resetToken);

            responseObj.put("success", true);
            responseObj.put("message", "密码重置成功");
        } catch (Exception e) {
            logger.error("resetPassword error: " + e.getMessage(), e);
            responseObj.put("success", false);
            responseObj.put("message", "操作失败: " + e.getMessage());
        }
        return responseObj;
    }

    /**
     * 管理员重置用户密码
     */
    @PostMapping("/adminResetPassword")
    public Map<String, Object> adminResetPassword(@RequestParam Long userId) {
        Map<String, Object> responseObj = new HashMap<>();
        try {
            Optional<SysUser> optionalUser = sysUserRepository.findById(userId);

            if (!optionalUser.isPresent()) {
                responseObj.put("success", false);
                responseObj.put("message", "用户不存在");
                return responseObj;
            }

            SysUser user = optionalUser.get();
            // 默认密码：Aa123456!
            user.setSysuserpassword(passwordEncoder.encode("Aa123456!"));
            sysUserRepository.save(user);

            responseObj.put("success", true);
            responseObj.put("message", "密码已重置为默认密码：Aa123456!");
        } catch (Exception e) {
            logger.error("adminResetPassword error: " + e.getMessage(), e);
            responseObj.put("success", false);
            responseObj.put("message", "操作失败: " + e.getMessage());
        }
        return responseObj;
    }
}
