package com.example.backend1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

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

    // Chuyển fullName thành name trong JSON
    @Column(name = "full_name", nullable = false, columnDefinition = "VARCHAR(100)")
    @JsonProperty("name")
    private String fullName;

    @Column(name = "address", columnDefinition = "VARCHAR(255)")
    private String address;

    @Column(name = "gender", columnDefinition = "BOOLEAN")
    private Boolean gender;

    @Column(name = "phone_number", columnDefinition = "VARCHAR(10)")
    private String phoneNumber;

    @Column(name = "birth_date", columnDefinition = "DATE")
    private LocalDate birthDate;

    @Column(name = "email", columnDefinition = "VARCHAR(100)", unique = true)
    private String email;

    // Thêm quan hệ với Cart
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cart> carts;
}
