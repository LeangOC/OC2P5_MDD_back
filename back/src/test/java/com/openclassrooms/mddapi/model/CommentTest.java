package com.openclassrooms.mddapi.model;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void createNewComment_ShouldCreateCommentCorrectly() {

        User author = new User();
        author.setId(1L);
        author.setUsername("john");

        Article article = new Article();
        article.setId(10L);

        Comment comment = Comment.createNewComment(
                "Très bon article",
                article,
                author
        );

        assertNotNull(comment);
        assertEquals("Très bon article", comment.getContent());
        assertEquals(article, comment.getArticle());
        assertEquals(author, comment.getAuthor());
    }

    @Test
    void onCreate_ShouldSetCreatedAt() throws Exception {

        Comment comment = new Comment();

        assertNull(comment.getCreatedAt());

        Method method = Comment.class.getDeclaredMethod("onCreate");
        method.setAccessible(true);
        method.invoke(comment);

        assertNotNull(comment.getCreatedAt());
    }

    @Test
    void lombokGettersAndSetters_ShouldWork() {

        User author = new User();
        Article article = new Article();
        Date date = new Date();

        Comment comment = new Comment();

        comment.setId(1L);
        comment.setAuthor(author);
        comment.setArticle(article);
        comment.setContent("Contenu");
        comment.setCreatedAt(date);

        assertEquals(1L, comment.getId());
        assertEquals(author, comment.getAuthor());
        assertEquals(article, comment.getArticle());
        assertEquals("Contenu", comment.getContent());
        assertEquals(date, comment.getCreatedAt());
    }

    @Test
    void allArgsConstructor_ShouldCreateComment() {

        User author = new User();
        Article article = new Article();
        Date date = new Date();

        Comment comment = new Comment(
                1L,
                author,
                article,
                "Contenu",
                date
        );

        assertEquals(1L, comment.getId());
        assertEquals(author, comment.getAuthor());
        assertEquals(article, comment.getArticle());
        assertEquals("Contenu", comment.getContent());
        assertEquals(date, comment.getCreatedAt());
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyComment() {

        Comment comment = new Comment();

        assertNotNull(comment);
        assertNull(comment.getId());
        assertNull(comment.getAuthor());
        assertNull(comment.getArticle());
        assertNull(comment.getContent());
        assertNull(comment.getCreatedAt());
    }
}