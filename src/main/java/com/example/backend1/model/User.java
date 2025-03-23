package com.example.backend1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private Account account;

    @Column(name = "full_name", nullable = false, columnDefinition = "VARCHAR(100)")
    private String fullName;

    @Column(name = "address", columnDefinition = "VARCHAR(255)")
    private String address;
    @Column(name = "gender",columnDefinition = "BOOLEAN")
    private Boolean gender;

    @Column(name = "phone_number",columnDefinition = "VARCHAR(10)")
    private String phoneNumber;

    @Column(name = "birth_date",columnDefinition = "DATE")
    private LocalDate birthDate;

    @Column(name = "email",columnDefinition = "VARCHAR(100)",unique = true)
    private String email;
}