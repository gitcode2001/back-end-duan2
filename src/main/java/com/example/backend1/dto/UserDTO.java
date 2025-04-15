package com.example.backend1.dto;

import com.example.backend1.model.User;
import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;

    public UserDTO(User user) {
        if (user != null) {
            this.id = user.getId();
            this.name = user.getFullName();
            this.email = user.getEmail();
        }
    }
}
