package com.example.backend1.controller;

import com.example.backend1.dto.ChangePasswordRequest;
import com.example.backend1.dto.ForGotPassWordDTO;
import com.example.backend1.dto.ResetPasswordDTO;
import com.example.backend1.dto.VerifyOtpDTO;
import com.example.backend1.model.User;
import com.example.backend1.service.IAccountService;
import com.example.backend1.service.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
@CrossOrigin("*")
public class AccountRestController {

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private IUserService userService;

    private String createJwtToken(String username) {
        long expirationTime = 1000 * 60 * 60 * 24;
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            Map<String, Object> loginResult = accountService.validateLogin(username, password);
            boolean success = (boolean) loginResult.get("success");

            if (success) {
                String token = createJwtToken(username);
                String role = accountService.getRoleIdByUsername(username);
                User user = userService.getUserByUsername(username);

                response.put("success", true);
                response.put("message", "Đăng nhập thành công");
                response.put("token", token);
                response.put("username", username);
                response.put("role", role != null ? role.toLowerCase() : null);
                response.put("userId", user != null ? user.getId() : null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("success", false);
                response.put("message", loginResult.get("message"));
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi xử lý yêu cầu");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/lock/{id}")
    public ResponseEntity<Map<String, Object>> lockAccount(@PathVariable Long id) {
        Map<String, Object> response = accountService.lockAccount(id);
        boolean success = (boolean) response.get("success");
        return success ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }

        String username = principal.getName();
        System.out.println("👤 Tài khoản đang thực hiện: " + username);

        try {
            if (changePasswordRequest.getNewPassword().length() < 8) {
                return ResponseEntity.badRequest().body("Mật khẩu mới phải có ít nhất 8 ký tự.");
            }

            boolean isChanged = accountService.changePassword(
                    username,
                    changePasswordRequest.getOldPassword(),
                    changePasswordRequest.getNewPassword(),
                    null
            );

            if (isChanged) {
                return ResponseEntity.ok("Mật khẩu đã được thay đổi thành công!");
            } else {
                return ResponseEntity.badRequest().body("Mật khẩu cũ không đúng hoặc có lỗi xảy ra.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi trong quá trình thay đổi mật khẩu.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForGotPassWordDTO request) {
        Map<String, Object> response = accountService.forgotPassword(request.getEmailOrUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpDTO request) {
        Map<String, Object> response = accountService.verifyOtp(request.getEmailOrUsername(), request.getOtp());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO request) {
        if (request.getNewPassword().length() < 8) {
            return ResponseEntity.badRequest().body("Mật khẩu mới phải có ít nhất 8 ký tự.");
        }

        Map<String, Object> response = accountService.newPassword(request.getEmailOrUsername(), request.getNewPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
