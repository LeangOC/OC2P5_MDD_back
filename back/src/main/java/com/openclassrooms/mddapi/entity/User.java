package com.openclassrooms.mddapi.entity;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // Un utilisateur crée plusieurs articles
    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();

    // Un utilisateur écrit plusieurs commentaires
    @OneToMany(mappedBy = "author")
    private List<Comment> comments = new ArrayList<>();

    // Un utilisateur possède plusieurs abonnements
    @OneToMany(mappedBy = "user")
    private List<Subscription> subscriptions = new ArrayList<>();
}