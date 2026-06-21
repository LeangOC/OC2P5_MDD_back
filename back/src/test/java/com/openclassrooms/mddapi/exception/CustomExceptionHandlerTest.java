package com.openclassrooms.mddapi.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class CustomExceptionHandlerTest {

    private CustomExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new CustomExceptionHandler();
    }

    @Test
    void handleUserAlreadyExistsException() {
        UserAlreadyExistsException ex =
                new UserAlreadyExistsException("User already exists");

        ResponseEntity<String> response =
                handler.handleUserAlreadyExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void handleUserNotFoundException() {
        UserNotFoundException ex =
                new UserNotFoundException("User not found");

        ResponseEntity<String> response =
                handler.handleUserNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    @Test
    void handleSubjectAlreadyExistsException() {
        SubjectAlreadyExistsException ex =
                new SubjectAlreadyExistsException("Subject exists");

        ResponseEntity<String> response =
                handler.handleSubjectAlreadyExistsException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Subject exists", response.getBody());
    }

    @Test
    void handleSubjectNotFoundException() {
        SubjectNotFoundException ex =
                new SubjectNotFoundException("Subject not found");

        ResponseEntity<String> response =
                handler.handleSubjectNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Subject not found", response.getBody());
    }

    @Test
    void handleUpdateSubjectException() {
        UpdateSubjectException ex =
                new UpdateSubjectException("Update failed");

        ResponseEntity<String> response =
                handler.handleUpdateSubjectException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Update failed", response.getBody());
    }

    @Test
    void handleDeleteSubjectException() {
        DeleteSubjectException ex =
                new DeleteSubjectException("Delete failed");

        ResponseEntity<String> response =
                handler.handleDeleteSubjectException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Delete failed", response.getBody());
    }

    @Test
    void handleInvalidSubjectDataException() {
        InvalidSubjectDataException ex =
                new InvalidSubjectDataException("Invalid subject");

        ResponseEntity<String> response =
                handler.handleInvalidSubjectDataException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid subject", response.getBody());
    }

    @Test
    void handleArticleNotFoundException() {
        ArticleNotFoundException ex =
                new ArticleNotFoundException("Article not found");

        ResponseEntity<String> response =
                handler.handleArticleNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Article not found", response.getBody());
    }

    @Test
    void handleUpdateArticleException() {
        UpdateArticleException ex =
                new UpdateArticleException("Update article failed");

        ResponseEntity<String> response =
                handler.handleUpdateArticleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Update article failed", response.getBody());
    }

    @Test
    void handleDeleteArticleException() {
        DeleteArticleException ex =
                new DeleteArticleException("Delete article failed");

        ResponseEntity<String> response =
                handler.handleDeleteArticleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Delete article failed", response.getBody());
    }

    @Test
    void handleInvalidArticleDataException() {
        InvalidArticleDataException ex =
                new InvalidArticleDataException("Invalid article");

        ResponseEntity<String> response =
                handler.handleInvalidArticleDataException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Invalid article", response.getBody());
    }

    @Test
    void handleCommentNotFoundException() {
        CommentNotFoundException ex =
                new CommentNotFoundException("Comment not found");

        ResponseEntity<String> response =
                handler.handleCommentNotFoundException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Comment not found", response.getBody());
    }

    @Test
    void handleDeleteCommentException() {
        DeleteCommentException ex =
                new DeleteCommentException("Delete comment failed");

        ResponseEntity<String> response =
                handler.handleDeleteCommentException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Delete comment failed", response.getBody());
    }

    @Test
    void handleInvalidCommentDataException() {
        InvalidCommentDataException ex =
                new InvalidCommentDataException("Invalid comment");

        ResponseEntity<String> response =
                handler.handleInvalidCommentDataException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Invalid comment", response.getBody());
    }

    @Test
    void handleUpdateCommentException() {
        UpdateCommentException ex =
                new UpdateCommentException("Update comment failed");

        ResponseEntity<String> response =
                handler.handleUpdateCommentException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Update comment failed", response.getBody());
    }
}