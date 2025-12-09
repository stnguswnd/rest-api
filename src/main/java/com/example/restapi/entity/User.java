package com.example.restapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@NoArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, unique = true, length = 30)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 30)
    private String name;

    private LocalDateTime created_at;

    public User(String username, String password, String email, String name, String created_at) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.created_at = LocalDateTime.now();
    }
}
