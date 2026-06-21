package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.exception.CommentNotFoundException;
import com.openclassrooms.mddapi.exception.InvalidCommentDataException;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.service.CommentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    @Test
    void getAllComments_ShouldReturnComments() {

        CommentDTO comment = new CommentDTO(
                1L,
                "john",
                10L,
                "Mon commentaire",
                new Date()
        );

        when(commentService.getAllComments())
                .thenReturn(List.of(comment));

        List<CommentDTO> result =
                commentController.getAllComments();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getCommentById_ShouldReturnComment() {

        CommentDTO comment = new CommentDTO(
                1L,
                "john",
                10L,
                "Commentaire",
                new Date()
        );

        when(commentService.getCommentById(1L))
                .thenReturn(comment);

        ResponseEntity<CommentDTO> response =
                commentController.getCommentById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(comment, response.getBody());
    }

    @Test
    void getCommentById_ShouldThrowException() {

        when(commentService.getCommentById(1L))
                .thenReturn(null);

        assertThrows(
                CommentNotFoundException.class,
                () -> commentController.getCommentById(1L)
        );
    }

    @Test
    void createComment_ShouldReturnCreated() {

        CommentDTO dto = new CommentDTO();
        dto.setContent("Nouveau commentaire");

        when(commentService.createComment(dto))
                .thenReturn(new Comment());

        ResponseEntity<Map<String, String>> response =
                commentController.createComment(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertEquals(
                "New Comment created",
                response.getBody().get("message")
        );
    }

    @Test
    void createComment_ShouldThrowInvalidDataException() {

        CommentDTO dto = new CommentDTO();
        dto.setContent("");

        assertThrows(
                InvalidCommentDataException.class,
                () -> commentController.createComment(dto)
        );
    }

    @Test
    void updateComment_ShouldReturnOk() {

        Long id = 1L;

        CommentDTO dto = new CommentDTO();
        dto.setContent("Commentaire modifié");

        CommentDTO existing = new CommentDTO();
        existing.setContent("Ancien commentaire");

        when(commentService.getCommentById(id))
                .thenReturn(existing);

        when(commentService.updateComment(id, dto))
                .thenReturn(new Comment());

        ResponseEntity<String> response =
                commentController.updateComment(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Comment updated", response.getBody());
    }

    @Test
    void updateComment_ShouldThrowCommentNotFoundException() {

        Long id = 1L;

        CommentDTO dto = new CommentDTO();
        dto.setContent("Commentaire modifié");

        when(commentService.getCommentById(id))
                .thenReturn(null);

        assertThrows(
                CommentNotFoundException.class,
                () -> commentController.updateComment(id, dto)
        );
    }

    @Test
    void deleteComment_ShouldReturnOk() {

        Long id = 1L;

        CommentDTO existing = new CommentDTO();
        existing.setContent("Commentaire");

        when(commentService.getCommentById(id))
                .thenReturn(existing);

        ResponseEntity<String> response =
                commentController.deleteCommentById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(commentService)
                .deleteCommentById(id);
    }

    @Test
    void deleteComment_ShouldThrowCommentNotFoundException() {

        when(commentService.getCommentById(1L))
                .thenReturn(null);

        assertThrows(
                CommentNotFoundException.class,
                () -> commentController.deleteCommentById(1L)
        );
    }

    @Test
    void deleteComment_ShouldThrowDeleteCommentException() {

        Long id = 1L;

        CommentDTO existing = new CommentDTO();
        existing.setContent("Commentaire");

        when(commentService.getCommentById(id))
                .thenReturn(existing);

        doThrow(new RuntimeException("DB Error"))
                .when(commentService)
                .deleteCommentById(id);

        assertThrows(
                com.openclassrooms.mddapi.exception.DeleteCommentException.class,
                () -> commentController.deleteCommentById(id)
        );
    }
}