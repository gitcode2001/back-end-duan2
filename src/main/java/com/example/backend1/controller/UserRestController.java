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

    @Autowired
    private IUserService userService;

    @Autowired
    private EmailService emailService;


    @GetMapping("/admin")
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(defaultValue = "") String search) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userService.findAllUser(pageable, search);
        return ResponseEntity.ok(users);
    }

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
    public ResponseEntity<?> addUser(@RequestBody User user) {
        if (user.getAccount() == null || user.getFullName() == null || user.getEmail() == null) {
            return ResponseEntity.badRequest().body("Thông tin người dùng không hợp lệ.");
        }
        if (user.getAccount().getUserName() == null || user.getAccount().getPassword() == null) {
            return ResponseEntity.badRequest().body("Tài khoản hoặc mật khẩu không được để trống.");
        }
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email đã được sử dụng.");
        }
        if (userService.existsByUsername(user.getAccount().getUserName())) {
            return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại.");
        }
        userService.save(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/information")
    public ResponseEntity<User> getUserInformation(@RequestParam("username") String username) {
        User user = userService.getUserByUsername(username);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.notFound().build();
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.findById(id);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không tìm thấy người dùng với ID: " + id);
        }

        userService.update(id, user);
        return ResponseEntity.ok(userService.findById(id));
    }
}
