package com.lobanmating.budget_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // Builder to create user without requiring id
    @Builder
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Factory method to create user with only id
    public static User withId(Long id) {
        User user = new User();
        user.id = id;
        return user;
    }
}
