package com.example.backend1.service.implement;

import com.example.backend1.model.Account;
import com.example.backend1.model.User;
import com.example.backend1.repository.AccountRepository;
import com.example.backend1.repository.UserRepository;
import com.example.backend1.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void save(User entity) {
        if (entity.getAccount() == null) {
            entity.setAccount(new Account());
        }

        LocalDateTime now = LocalDateTime.now();
        String rawPassword = generateAndStorePassword(entity.getAccount().getUserName());
        String encodedPassword = passwordEncoder.encode(rawPassword);

        entity.getAccount().setPassword(encodedPassword);
        entity.getAccount().setDateCreatePassWord(now);

        Account savedAccount = accountRepository.save(entity.getAccount());
        entity.setAccount(savedAccount);

        userRepository.save(entity);

        emailService.sendPasswordEmail(
                entity.getFullName(),
                entity.getEmail(),
                rawPassword,
                entity.getAccount().getUserName(),
                entity.getId()
        );

        // Debug: In thông tin Role
        if (entity.getAccount() != null && entity.getAccount().getRole() != null) {
            System.out.println("Role từ entity: " + entity.getAccount().getRole().getNameRoles());
        } else {
            System.out.println("Role bị null!");
        }
    }

    @Override
    public void update(Long id, User entity) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            // Cập nhật các trường của User
            if (entity.getAddress() != null) {
                existingUser.setAddress(entity.getAddress());
            }
            if (entity.getPhoneNumber() != null) {
                existingUser.setPhoneNumber(entity.getPhoneNumber());
            }
            if (entity.getGender() != null) {
                existingUser.setGender(entity.getGender());
            }
            if (entity.getFullName() != null) {
                existingUser.setFullName(entity.getFullName());
            }
            if (entity.getEmail() != null) {
                existingUser.setEmail(entity.getEmail());
            }
            if (entity.getBirthDate() != null) {
                existingUser.setBirthDate(entity.getBirthDate());
            }
            // Cập nhật thông tin Account
            if (entity.getAccount() != null) {
                Account existingAccount = existingUser.getAccount();
                if (existingAccount == null) {
                    existingAccount = new Account();
                    existingUser.setAccount(existingAccount);
                }
                if (entity.getAccount().getUserName() != null) {
                    existingAccount.setUserName(entity.getAccount().getUserName());
                }
                // Nếu có mật khẩu mới (không rỗng) thì cập nhật
                if (entity.getAccount().getPassword() != null && !entity.getAccount().getPassword().isEmpty()) {
                    String newPassword = entity.getAccount().getPassword();
                    String hashedPassword = passwordEncoder.encode(newPassword);
                    existingAccount.setPassword(hashedPassword);
                }
                // Cập nhật trạng thái locked
                existingAccount.setLocked(entity.getAccount().isLocked());
                // Cập nhật role nếu có
                if (entity.getAccount().getRole() != null) {
                    existingAccount.setRole(entity.getAccount().getRole());
                }
                existingAccount.setDateCreatePassWord(LocalDateTime.now());
            }
            userRepository.save(existingUser);
        }
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByAccount_UserName(username);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByAccount_UserName(username);
    }

    // Sửa lại phương thức findAllUser để trả về tất cả user (active và locked)
    @Override
    public Page<User> findAllUser(Pageable pageable, String search) {
        return userRepository.findAllUsers(
                search,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").ascending())
        );
    }

    // Sinh ra mật khẩu ngẫu nhiên và lưu vào session (nếu cần)
    private String generateAndStorePassword(String username) {
        String rawPassword = RandomStringUtils.randomAlphanumeric(8);
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpSession session = attributes.getRequest().getSession();
            session.setAttribute("rawPassword_" + username, rawPassword);
        }
        return rawPassword;
    }
}
