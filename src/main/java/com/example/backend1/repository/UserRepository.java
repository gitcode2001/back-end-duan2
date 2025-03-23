package com.example.backend1.repository;


import com.example.backend1.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.account.isLocked = false " +
            "AND (:search IS NULL OR :search = '' " +
            "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "OR LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<User> findAllActiveUsers(Pageable pageable, @Param("search") String search);
    boolean existsByAccount_UserName(String username);
    boolean existsByEmail(String email);
    User findByAccount_UserName(String username);
    User findByEmail(String email);
}
