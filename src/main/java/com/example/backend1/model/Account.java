package com.example.backend1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",columnDefinition = "BIGINT")
    private Long  id;

    @Column(name = "user_name", unique = true, nullable = false,columnDefinition = "VARCHAR(50)")
    private String userName;

    @Column(name = "pass_word", nullable = false,columnDefinition = "VARCHAR(255)")
    private String password;

    @Column(name = "date_create", columnDefinition = "DATETIME")
    private LocalDateTime dateCreatePassWord;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    @JsonIgnoreProperties("accounts")
    private Role role;
    @Column(name = "is_locked", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isLocked;
}