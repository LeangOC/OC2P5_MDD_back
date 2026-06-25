package com.openclassrooms.mddapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Représente un article publié sur la plateforme.
 *
 * <p>
 * Un article est associé :
 * </p>
 * <ul>
 *     <li>à un auteur ;</li>
 *     <li>à un sujet ;</li>
 *     <li>à une date de création ;</li>
 *     <li>à un ensemble de commentaires.</li>
 * </ul>
 *
 * @author LCH
 * @since 1.0
 */
@Entity
@Table(name = "articles", uniqueConstraints = {
    @UniqueConstraint(columnNames = "title")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false, length = 5000)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishedAt;

    @PrePersist
    protected void onCreate() {
        publishedAt = new Date();
    }

    public static Article createNewArticle(String title, String content, Subject subject, User author) {
        Article article = new Article();
        article.setTitle(title);
        article.setContent(content);
        article.setSubject(subject);
        article.setAuthor(author);
        return article;
    }







}
