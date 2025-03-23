package com.example.backend1.service;



import com.example.backend1.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService extends IService<User, Long>{
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    User getUserByUsername(String username);
    Page<User> findAllUser(Pageable pageable, String search);

}
