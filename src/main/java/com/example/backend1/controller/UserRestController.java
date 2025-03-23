package com.example.backend1.controller;

import com.example.backend1.model.Account;
import com.example.backend1.model.User;
import com.example.backend1.service.IUserService;
import com.example.backend1.service.implement.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class UserRestController {

    private final IUserService userService;
    private final EmailService emailService;

    @Autowired
    public UserRestController(IUserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    // Lấy danh sách người dùng với phân trang và tìm kiếm
    @GetMapping("/admin")
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(defaultValue = "") String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.findAllUser(pageable, search);
        return ResponseEntity.ok(users);
    }

    // Kiểm tra tồn tại email hoặc username
    @GetMapping("/admin/check_account")
    public ResponseEntity<Map<String, Boolean>> checkAccount(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username) {
        boolean existsEmail = email != null && userService.existsByEmail(email);
        boolean existsUsername = username != null && userService.existsByUsername(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("existsEmail", existsEmail);
        response.put("existsUsername", existsUsername);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        if (user.getAccount() == null) {
            user.setAccount(new Account());
        }
        userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/information")
    public ResponseEntity<User> getUserInformation(@RequestParam("username") String username) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    // Cập nhật thông tin người dùng theo id
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        } else {
            userService.update(id, user);
            return ResponseEntity.ok(user);
        }
    }
}
