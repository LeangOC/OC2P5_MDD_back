package com.openclassrooms.mddapi.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Un sujet possède plusieurs articles
    @OneToMany(mappedBy = "topic")
    private List<Post> posts = new ArrayList<>();

    // Un sujet possède plusieurs abonnements
    @OneToMany(mappedBy = "topic")
    private List<Subscription> subscriptions = new ArrayList<>();
}