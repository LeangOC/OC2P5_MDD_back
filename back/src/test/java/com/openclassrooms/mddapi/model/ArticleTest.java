package com.openclassrooms.mddapi.model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    @Test
    void createNewArticle_ShouldCreateArticleCorrectly() {

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Java");

        User author = new User();
        author.setId(2L);
        author.setUsername("john");

        Article article = Article.createNewArticle(
                "Mon titre",
                "Mon contenu",
                subject,
                author
        );

        assertNotNull(article);
        assertEquals("Mon titre", article.getTitle());
        assertEquals("Mon contenu", article.getContent());
        assertEquals(subject, article.getSubject());
        assertEquals(author, article.getAuthor());
    }

    @Test
    void onCreate_ShouldSetPublishedAt() throws Exception {

        Article article = new Article();

        assertNull(article.getPublishedAt());

        Method method = Article.class.getDeclaredMethod("onCreate");
        method.setAccessible(true);
        method.invoke(article);

        assertNotNull(article.getPublishedAt());
    }

    @Test
    void lombokGettersAndSetters_ShouldWork() {

        User author = new User();
        Subject subject = new Subject();
        Date date = new Date();

        Article article = new Article();

        article.setId(1L);
        article.setAuthor(author);
        article.setSubject(subject);
        article.setTitle("Titre");
        article.setContent("Contenu");
        article.setPublishedAt(date);

        assertEquals(1L, article.getId());
        assertEquals(author, article.getAuthor());
        assertEquals(subject, article.getSubject());
        assertEquals("Titre", article.getTitle());
        assertEquals("Contenu", article.getContent());
        assertEquals(date, article.getPublishedAt());
    }

    @Test
    void allArgsConstructor_ShouldCreateArticle() {

        User author = new User();
        Subject subject = new Subject();
        Date date = new Date();

        Article article = new Article(
                1L,
                author,
                subject,
                "Titre",
                "Contenu",
                date
        );

        assertEquals(1L, article.getId());
        assertEquals(author, article.getAuthor());
        assertEquals(subject, article.getSubject());
        assertEquals("Titre", article.getTitle());
        assertEquals("Contenu", article.getContent());
        assertEquals(date, article.getPublishedAt());
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyArticle() {

        Article article = new Article();

        assertNotNull(article);
        assertNull(article.getId());
        assertNull(article.getAuthor());
        assertNull(article.getSubject());
        assertNull(article.getTitle());
        assertNull(article.getContent());
        assertNull(article.getPublishedAt());
    }
}