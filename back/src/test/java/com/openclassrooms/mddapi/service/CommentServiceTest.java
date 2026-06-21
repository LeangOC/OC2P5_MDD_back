package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.exception.ArticleNotFoundException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    void getAllComments_ShouldReturnEmptyList() {

        when(commentRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<CommentDTO> result = commentService.getAllComments();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(commentRepository).findAll();
    }

    @Test
    void getAllComments_ShouldReturnComments() {

        User author = new User();
        author.setId(1L);
        author.setUsername("john");

        Article article = new Article();
        article.setId(10L);

        Comment comment = new Comment();
        comment.setId(100L);
        comment.setAuthor(author);
        comment.setArticle(article);
        comment.setContent("My comment");
        comment.setCreatedAt(new Date());

        when(commentRepository.findAll())
                .thenReturn(List.of(comment));

        List<CommentDTO> result = commentService.getAllComments();

        assertEquals(1, result.size());
        assertEquals("john", result.get(0).getAuthorName());

        verify(commentRepository).findAll();
    }

    @Test
    void getCommentById_ShouldReturnCommentDTO_WhenFound() {

        User author = new User();
        author.setId(1L);
        author.setUsername("john");

        Article article = new Article();
        article.setId(10L);

        Comment comment = new Comment();
        comment.setId(100L);
        comment.setAuthor(author);
        comment.setArticle(article);
        comment.setContent("My comment");
        comment.setCreatedAt(new Date());

        when(commentRepository.findById(100L))
                .thenReturn(Optional.of(comment));

        CommentDTO result = commentService.getCommentById(100L);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("john", result.getAuthorName());

        verify(commentRepository).findById(100L);
    }

    @Test
    void getCommentById_ShouldReturnNull_WhenNotFound() {

        when(commentRepository.findById(100L))
                .thenReturn(Optional.empty());

        CommentDTO result = commentService.getCommentById(100L);

        assertNull(result);

        verify(commentRepository).findById(100L);
    }

    @Test
    void createComment_ShouldThrowUserNotFoundException() {

        CommentDTO dto = new CommentDTO();
        dto.setUserId(1L);
        dto.setArticleId(10L);
        dto.setContent("Comment");

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> commentService.createComment(dto)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_ShouldThrowArticleNotFoundException() {

        User user = new User();
        user.setId(1L);

        CommentDTO dto = new CommentDTO();
        dto.setUserId(1L);
        dto.setArticleId(10L);
        dto.setContent("Comment");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(articleRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ArticleNotFoundException.class,
                () -> commentService.createComment(dto)
        );

        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_ShouldSaveComment() {

        User user = new User();
        user.setId(1L);

        Article article = new Article();
        article.setId(10L);

        CommentDTO dto = new CommentDTO();
        dto.setUserId(1L);
        dto.setArticleId(10L);
        dto.setContent("Comment");

        Comment comment = new Comment();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(articleRepository.findById(10L))
                .thenReturn(Optional.of(article));

        try (MockedStatic<Comment> mocked =
                     mockStatic(Comment.class)) {

            mocked.when(() ->
                            Comment.createNewComment(
                                    "Comment",
                                    article,
                                    user))
                    .thenReturn(comment);

            when(commentRepository.save(comment))
                    .thenReturn(comment);

            Comment result = commentService.createComment(dto);

            assertNotNull(result);

            verify(commentRepository).save(comment);
        }
    }

    @Test
    void updateComment_ShouldSaveUpdatedComment() {

        User user = new User();
        user.setId(1L);

        Article article = new Article();
        article.setId(10L);

        CommentDTO dto = new CommentDTO();
        dto.setUserId(1L);
        dto.setArticleId(10L);
        dto.setContent("Updated comment");

        Comment comment = new Comment();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(articleRepository.findById(10L))
                .thenReturn(Optional.of(article));

        try (MockedStatic<Comment> mocked =
                     mockStatic(Comment.class)) {

            mocked.when(() ->
                            Comment.createNewComment(
                                    "Updated comment",
                                    article,
                                    user))
                    .thenReturn(comment);

            when(commentRepository.save(any(Comment.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Comment result =
                    commentService.updateComment(5L, dto);

            assertEquals(5L, result.getId());

            verify(commentRepository).save(any(Comment.class));
        }
    }

    @Test
    void deleteCommentById_ShouldCallRepository() {

        commentService.deleteCommentById(1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    void toDTO_ShouldConvertCommentToDTO() {

        User author = new User();
        author.setId(1L);
        author.setUsername("john");

        Article article = new Article();
        article.setId(10L);

        Date createdAt = new Date();

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setArticle(article);
        comment.setContent("My comment");
        comment.setCreatedAt(createdAt);

        CommentDTO dto = commentService.toDTO(comment);

        assertNotNull(dto);
        assertEquals(1L, dto.getUserId());
        assertEquals("john", dto.getAuthorName());
        assertEquals(10L, dto.getArticleId());
        assertEquals("My comment", dto.getContent());
        assertEquals(createdAt, dto.getCreatedAt());
    }
}