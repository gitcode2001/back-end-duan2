package com.example.backend1.controller;

import com.example.backend1.model.Role;
import com.example.backend1.service.IRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/roles")
public class RoleRestController {
    @Autowired
    private IRoleService roleService;
    @GetMapping
    public ResponseEntity<List<Role>> getAll(){
        List<Role> roles = roleService.getAll();
        System.out.println("📢 Dữ liệu roles từ DB: " + roles);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
}
