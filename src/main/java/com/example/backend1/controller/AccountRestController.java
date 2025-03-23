package com.example.backend1.controller;


import com.example.backend1.dto.ChangePasswordRequest;
import com.example.backend1.dto.ForGotPassWordDTO;
import com.example.backend1.dto.VerifyOtpDTO;
import com.example.backend1.service.IAccountService;
import com.example.caseduan1.dto.ResetPasswordDTO;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("api/login")
public class AccountRestController {

    @Value("${jwt.secret}")
    private String secretKey;

    private final IAccountService accountService;

    public AccountRestController(IAccountService accountService) {
        this.accountService = accountService;
    }

    // Đăng nhập: kiểm tra thông tin đăng nhập, tạo JWT token nếu thành công
    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            // Kiểm tra đăng nhập qua service
            Map<String, Object> loginResult = accountService.validateLogin(username, password);
            boolean success = (boolean) loginResult.get("success");

            if (success) {
                String token = createJwtToken(username);
                String role = accountService.getRoleIdByUsername(username);
                // Lưu username vào session nếu cần
                request.getSession().setAttribute("username", username);
                response.put("success", true);
                response.put("message", "Đăng nhập thành công");
                response.put("role", role);
                response.put("token", token);
                response.put("username", username);
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

    // Tạo JWT token với thời gian hết hạn là 1 ngày
    private String createJwtToken(String username) {
        long expirationTime = 1000 * 60 * 60 * 24; // 24 giờ
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // Khóa tài khoản dựa theo id
    @PutMapping("/lock/{id}")
    public ResponseEntity<Map<String, Object>> lockAccount(@PathVariable Long id) {
        Map<String, Object> response = accountService.lockAccount(id);
        boolean success = (boolean) response.get("success");
        if (success) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Đổi mật khẩu cho tài khoản đã đăng nhập
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest changePasswordRequest,
            HttpServletRequest request,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bạn chưa đăng nhập!");
        }

        String username = principal.getName();
        System.out.println("👤 Tài khoản đang thực hiện: " + username);

        try {
            boolean isChanged = accountService.changePassword(
                    username,
                    changePasswordRequest.getOldPassword(),
                    changePasswordRequest.getNewPassword(),
                    null  // Không cần rawPasswordInSession nữa
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

    // Quên mật khẩu: gửi yêu cầu khôi phục mật khẩu
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForGotPassWordDTO requestDTO) {
        Map<String, Object> response = accountService.forgotPassword(requestDTO.getEmailOrUsername());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Xác thực OTP gửi về email hoặc username
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpDTO requestDTO) {
        Map<String, Object> response = accountService.verifyOtp(requestDTO.getEmailOrUsername(), requestDTO.getOtp());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Đặt lại mật khẩu mới
    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO requestDTO) {
        Map<String, Object> response = accountService.newPassword(requestDTO.getEmailOrUsername(), requestDTO.getNewPassword());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
