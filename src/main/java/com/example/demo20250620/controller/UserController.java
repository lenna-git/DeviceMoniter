package com.example.demo20250620.controller;

import com.example.demo20250620.entity.SysUser;
import com.example.demo20250620.entity.User;
import com.example.demo20250620.service.UserService;
import com.example.demo20250620.util.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/useraction/")
public class UserController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private final HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    public UserController(HttpServletRequest request) {
        this.request = request;
    }

//    @CrossOrigin(origins = "http://127.0.0.1:8080")

    @GetMapping("/demo")
    public String demo(){
        HttpSession session = request.getSession();
        Optional<SysUser> loginUser = (Optional<SysUser>) session.getAttribute("SYS_USER");
        logger.info("当前登录用户名："+loginUser.get().getSysusername());
        System.out.println("demo called");
        return "hello demo";
        
    }

    @GetMapping("/allusers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("getuserbyid/{id}")
    public Optional<User> getUserbyid(@PathVariable long id){
        return userService.getUserbyid(id);
    }

    @PutMapping("updateuserbyid/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> user1 = userService.getUserbyid(id);
        User user2= user1.orElseGet(() -> new User());
        user2.setUsername(user.getUsername());
        user2.setEmail(user.getEmail());
        userService.saveUser(user2);
    }

    @PostMapping("/createuser")
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }


    @DeleteMapping("/delusers/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.delUserbyid(id);
    }

}
