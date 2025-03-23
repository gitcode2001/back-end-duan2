package com.example.backend1.service.implement;

import com.example.backend1.model.Role;
import com.example.backend1.repository.RoleRepository;
import com.example.backend1.service.IRoleService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleService implements IRoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }
}
