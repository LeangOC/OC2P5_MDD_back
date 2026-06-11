package com.openclassrooms.mddapi.entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    // Plusieurs commentaires écrits par un utilisateur
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    // Plusieurs commentaires liés à un article
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post article;
}