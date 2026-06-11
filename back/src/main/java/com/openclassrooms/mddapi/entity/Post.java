package com.openclassrooms.mddapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime publishedAt;

    // Plusieurs articles appartiennent à un utilisateur
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Plusieurs articles appartiennent à un sujet
    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    // Un article possède plusieurs commentaires
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}