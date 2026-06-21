package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.ArticleWithCommentsDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.exception.ArticleNotFoundException;
import com.openclassrooms.mddapi.exception.InvalidArticleDataException;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleControllerTest {

    @Mock
    private ArticleService articleService;

    @Mock
    private UserService userService;

    @InjectMocks
    private ArticleController articleController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                articleController,
                "userService",
                userService
        );
    }

    @Test
    void subscribedArticles_ShouldReturnArticles() {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(100L);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);

        when(authentication.getName()).thenReturn("john");
        when(securityContext.getAuthentication()).thenReturn(authentication);

        try (MockedStatic<SecurityContextHolder> mocked =
                     mockStatic(SecurityContextHolder.class)) {

            mocked.when(SecurityContextHolder::getContext)
                    .thenReturn(securityContext);

            when(userService.getUserByName("john"))
                    .thenReturn(userDTO);

            when(articleService.getSubscribedArticlesForUser(1L))
                    .thenReturn(List.of(articleDTO));

            List<ArticleDTO> result =
                    articleController.subscribedArticles();

            assertEquals(1, result.size());
        }
    }

    @Test
    void getArticleById_ShouldReturnArticle() {

        ArticleWithCommentsDTO dto =
                new ArticleWithCommentsDTO();

        when(articleService.getArticleById(1L))
                .thenReturn(dto);

        ResponseEntity<ArticleWithCommentsDTO> response =
                articleController.getArticleById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getArticleById_ShouldThrowException() {

        when(articleService.getArticleById(1L))
                .thenReturn(null);

        assertThrows(
                ArticleNotFoundException.class,
                () -> articleController.getArticleById(1L)
        );
    }

    @Test
    void createArticle_ShouldReturnCreated() {

        ArticleDTO dto = new ArticleDTO();
        dto.setContent("content");
        dto.setTitle("title");

        when(articleService.createArticle(dto))
                .thenReturn(new Article());

        ResponseEntity<String> response =
                articleController.createArticle(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createArticle_ShouldThrowInvalidDataException() {

        ArticleDTO dto = new ArticleDTO();
        dto.setContent("");

        assertThrows(
                InvalidArticleDataException.class,
                () -> articleController.createArticle(dto)
        );
    }

    @Test
    void updateArticle_ShouldReturnOk() {

        Long id = 1L;

        ArticleDTO dto = new ArticleDTO();
        dto.setContent("updated");
        dto.setTitle("title");

        ArticleWithCommentsDTO existing =
                new ArticleWithCommentsDTO();

        when(articleService.getArticleById(id))
                .thenReturn(existing);

        when(articleService.updateArticle(id, dto))
                .thenReturn(new Article());

        ResponseEntity<String> response =
                articleController.updateArticle(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateArticle_ShouldThrowNotFound() {

        Long id = 1L;

        ArticleDTO dto = new ArticleDTO();
        dto.setContent("updated");

        when(articleService.getArticleById(id))
                .thenReturn(null);

        assertThrows(
                ArticleNotFoundException.class,
                () -> articleController.updateArticle(id, dto)
        );
    }

    @Test
    void deleteArticle_ShouldReturnOk() {

        Long id = 1L;

        ArticleWithCommentsDTO dto =
                new ArticleWithCommentsDTO();

        when(articleService.getArticleById(id))
                .thenReturn(dto);

        ResponseEntity<String> response =
                articleController.deleteArticleById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(articleService)
                .deleteArticleById(id);
    }

    @Test
    void deleteArticle_ShouldThrowNotFound() {

        when(articleService.getArticleById(1L))
                .thenReturn(null);

        assertThrows(
                ArticleNotFoundException.class,
                () -> articleController.deleteArticleById(1L)
        );
    }
}