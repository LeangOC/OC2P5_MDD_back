package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.ArticleWithCommentsDTO;
import com.openclassrooms.mddapi.dto.CommentDTO;
import com.openclassrooms.mddapi.exception.SubjectNotFoundException;
import com.openclassrooms.mddapi.exception.UserNotFoundException;
import com.openclassrooms.mddapi.model.*;
import com.openclassrooms.mddapi.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentService commentService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test
    void getAllArticles_ShouldReturnEmptyList() {

        when(articleRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<ArticleDTO> result = articleService.getAllArticles();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(articleRepository).findAll();
    }

    @Test
    void getAllArticles_ShouldReturnArticles() {

        User author = new User();
        author.setId(1L);
        author.setUsername("john");

        Subject subject = new Subject();
        subject.setId(10L);
        subject.setName("Java");

        Article article = new Article();
        article.setId(100L);
        article.setAuthor(author);
        article.setSubject(subject);
        article.setTitle("Article Java");
        article.setContent("Content");
        article.setPublishedAt(new Date());

        when(articleRepository.findAll())
                .thenReturn(List.of(article));

        List<ArticleDTO> result = articleService.getAllArticles();

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getId());

        verify(articleRepository).findAll();
    }

    @Test
    void getArticleById_ShouldReturnNull_WhenArticleNotFound() {

        when(articleRepository.findById(1L))
                .thenReturn(Optional.empty());

        ArticleWithCommentsDTO result =
                articleService.getArticleById(1L);

        assertNull(result);
    }

    @Test
    void getArticleById_ShouldReturnArticle_WhenFound() {

        User author = new User();
        author.setId(1L);
        author.setUsername("john");

        Subject subject = new Subject();
        subject.setId(10L);
        subject.setName("Java");

        Article article = new Article();
        article.setId(100L);
        article.setAuthor(author);
        article.setSubject(subject);
        article.setTitle("Article");
        article.setContent("Content");
        article.setPublishedAt(new Date());

        when(articleRepository.findById(100L))
                .thenReturn(Optional.of(article));

        when(commentRepository.findByArticleId(eq(100L), any(Sort.class)))
                .thenReturn(Collections.emptyList());

        ArticleWithCommentsDTO result =
                articleService.getArticleById(100L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
    }

    @Test
    void createArticle_ShouldThrowUserNotFoundException() {

        ArticleDTO dto = new ArticleDTO();
        dto.setUserId(1L);
        dto.setSubjectId(10L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> articleService.createArticle(dto)
        );

        verify(articleRepository, never()).save(any());
    }

    @Test
    void createArticle_ShouldThrowSubjectNotFoundException() {

        User user = new User();
        user.setId(1L);

        ArticleDTO dto = new ArticleDTO();
        dto.setUserId(1L);
        dto.setSubjectId(10L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(subjectRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                SubjectNotFoundException.class,
                () -> articleService.createArticle(dto)
        );

        verify(articleRepository, never()).save(any());
    }

    @Test
    void createArticle_ShouldSaveArticle() {

        User user = new User();
        user.setId(1L);

        Subject subject = new Subject();
        subject.setId(10L);

        ArticleDTO dto = new ArticleDTO();
        dto.setUserId(1L);
        dto.setSubjectId(10L);
        dto.setTitle("Title");
        dto.setContent("Content");

        Article article = new Article();
        article.setTitle("Title");

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(subjectRepository.findById(10L))
                .thenReturn(Optional.of(subject));

        try (MockedStatic<Article> mocked =
                     mockStatic(Article.class)) {

            mocked.when(() ->
                            Article.createNewArticle(
                                    "Title",
                                    "Content",
                                    subject,
                                    user))
                    .thenReturn(article);

            when(articleRepository.save(article))
                    .thenReturn(article);

            Article result =
                    articleService.createArticle(dto);

            assertNotNull(result);

            verify(articleRepository).save(article);
        }
    }

    @Test
    void updateArticle_ShouldSaveArticle() {

        User user = new User();
        user.setId(1L);

        Subject subject = new Subject();
        subject.setId(10L);

        ArticleDTO dto = new ArticleDTO();
        dto.setUserId(1L);
        dto.setSubjectId(10L);
        dto.setTitle("Title");
        dto.setContent("Content");

        Article article = new Article();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(subjectRepository.findById(10L))
                .thenReturn(Optional.of(subject));

        try (MockedStatic<Article> mocked =
                     mockStatic(Article.class)) {

            mocked.when(() ->
                            Article.createNewArticle(
                                    "Title",
                                    "Content",
                                    subject,
                                    user))
                    .thenReturn(article);

            when(articleRepository.save(any(Article.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Article result =
                    articleService.updateArticle(5L, dto);

            assertEquals(5L, result.getId());

            verify(articleRepository).save(any(Article.class));
        }
    }

    @Test
    void deleteArticleById_ShouldCallRepository() {

        articleService.deleteArticleById(1L);

        verify(articleRepository).deleteById(1L);
    }

    @Test
    void getCommentsByArticleId_ShouldReturnEmptyList() {

        when(commentRepository.findByArticleId(eq(10L), any(Sort.class)))
                .thenReturn(Collections.emptyList());

        List<CommentDTO> result =
                articleService.getCommentsByArticleId(10L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getCommentsByArticleId_ShouldReturnComments() {

        User author = new User();
        author.setId(1L);
        author.setUsername("john");

        Article article = new Article();
        article.setId(10L);

        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setArticle(article);

        CommentDTO dto = new CommentDTO();

        when(commentRepository.findByArticleId(eq(10L), any(Sort.class)))
                .thenReturn(List.of(comment));

        when(commentService.toDTO(comment))
                .thenReturn(dto);

        List<CommentDTO> result =
                articleService.getCommentsByArticleId(10L);

        assertEquals(1, result.size());
    }

    @Test
    void getSubscribedArticlesForUser_ShouldReturnEmptyList_WhenNoSubscriptions() {

        when(articleRepository.findAll())
                .thenReturn(Collections.emptyList());

        when(subscriptionRepository.findByUserId(1L))
                .thenReturn(Collections.emptyList());

        List<ArticleDTO> result =
                articleService.getSubscribedArticlesForUser(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void getSubscribedArticlesForUser_ShouldFilterArticles() {

        Long userId = 1L;

        Subject java = new Subject();
        java.setId(1L);
        java.setName("Java");

        Subject spring = new Subject();
        spring.setId(2L);
        spring.setName("Spring");

        User author = new User();
        author.setId(1L);
        author.setUsername("john");

        Article article1 = new Article();
        article1.setId(100L);
        article1.setAuthor(author);
        article1.setSubject(java);

        Article article2 = new Article();
        article2.setId(200L);
        article2.setAuthor(author);
        article2.setSubject(spring);

        Subscription subscription = new Subscription();
        subscription.setSubject(java);

        when(articleRepository.findAll())
                .thenReturn(List.of(article1, article2));

        when(subscriptionRepository.findByUserId(userId))
                .thenReturn(List.of(subscription));

        List<ArticleDTO> result =
                articleService.getSubscribedArticlesForUser(userId);

        assertEquals(1, result.size());
        assertEquals(100L, result.get(0).getId());
    }
}