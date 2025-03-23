package com.example.backend1.service.implement;


import com.example.backend1.model.Account;
import com.example.backend1.model.User;
import com.example.backend1.repository.AccountRepository;
import com.example.backend1.repository.UserRepository;
import com.example.backend1.service.IAccountService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class AccountService implements IAccountService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Map<String, Object> validateLogin(String username, String password) {
        Account account = accountRepository.findByUserName(username);

        if (account == null || account.isLocked()) {
            return Map.of("success", false, "message", "Tài khoản không tồn tại hoặc đã bị khóa");
        }

        // Kiểm tra mật khẩu nhập vào có khớp với mật khẩu đã được mã hoá trong database hay không
        if (!passwordEncoder.matches(password, account.getPassword())) {
            return Map.of("success", false, "message", "Mật khẩu không đúng");
        }

        return Map.of("success", true, "message", "Đăng nhập thành công");
    }
    @Override
    public boolean changePassword(String userName, String oldPassword, String newPassword, String oldPasswordRaw) {
        Account account = accountRepository.findByUserName(userName);
        if (account == null) {
            return false;
        }
        if (oldPasswordRaw != null && oldPasswordRaw.equals(oldPassword)) {
        } else {
            if (!passwordEncoder.matches(oldPassword, account.getPassword())) {
                return false;
            }
        }
        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        return true;
    }
    @Override
    public String getRoleIdByUsername(String username) {
        Account account = accountRepository.findByUserName(username);
        if (account != null) {
            return account.getRole().getNameRoles();
        }
        return null;
    }
    @Override
    public Map<String, Object> forgotPassword(String emailOrUsername) {
        boolean usernameOpt = userRepository.existsByAccount_UserName(emailOrUsername);
        boolean emailOpt = userRepository.existsByEmail(emailOrUsername);
        if(!usernameOpt && !emailOpt){
            return Map.of("success", false, "message", "Không tìm thấy tài khoản");
        }
        User user = usernameOpt ? userRepository.findByAccount_UserName(emailOrUsername) : userRepository.findByEmail(emailOrUsername);
        Account account = user.getAccount();
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpService.saveOtp(emailOrUsername, otp);
        emailService.sendOtpEmail(user.getFullName(), user.getEmail(), otp);
        return Map.of("success", true, "message", "Mã OTP đã được gửi đến email của bạn");
    }

    @Override
    public Map<String, Object> verifyOtp(String emailOrUsername, String otp) {
        if(otpService.validateOtp(emailOrUsername, otp)){
            return Map.of("success", true, "message", "OTP hợp lệ, bạn có thể đổi mật khẩu");
        }
        return Map.of("success", false, "message", "OTP không hợp lệ hoặc đã hết hạn");
    }

    @Override
    public Map<String,Object> newPassword(String emailOrUsername, String password) {
        boolean usernameExists = userRepository.existsByAccount_UserName(emailOrUsername);
        boolean emailExists = userRepository.existsByEmail(emailOrUsername);

        if (!usernameExists && !emailExists) {
            return Map.of("success", false, "message", "Không tìm thấy tài khoản");
        }
        User user = usernameExists
                ? userRepository.findByAccount_UserName(emailOrUsername)
                : userRepository.findByEmail(emailOrUsername);
        Account account = user.getAccount();
        account.setPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
        return Map.of("success", true, "message", "Mật khẩu đã được cập nhật thành công");
    }

    @Override
    public Map<String, Object> lockAccount(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return Map.of("success", false, "message", "Người dùng không tồn tại");
        }

        User user = optionalUser.get();
        Account account = user.getAccount(); // Lấy account từ user

        if (account == null) {
            return Map.of("success", false, "message", "Tài khoản không tồn tại");
        }

        // Cập nhật trạng thái khóa
        account.setLocked(true);
        accountRepository.save(account);

        return Map.of("success", true, "message", "Tài khoản đã bị khóa");
    }

    @Override
    public Account findAccountByUsername(String username) {
        return accountRepository.findByUserName(username);
    }


}
