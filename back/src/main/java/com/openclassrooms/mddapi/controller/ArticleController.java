package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDTO;
import com.openclassrooms.mddapi.dto.ArticleWithCommentsDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.exception.ArticleNotFoundException;
import com.openclassrooms.mddapi.exception.DeleteArticleException;
import com.openclassrooms.mddapi.exception.InvalidArticleDataException;
import com.openclassrooms.mddapi.exception.UpdateArticleException;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST responsable de la gestion des articles.
 *
 * <p>
 * Fournit les opérations permettant :
 * </p>
 * <ul>
 *     <li>de consulter les articles des sujets suivis ;</li>
 *     <li>de consulter un article avec ses commentaires ;</li>
 *     <li>de créer un article ;</li>
 *     <li>de modifier un article ;</li>
 *     <li>de supprimer un article.</li>
 * </ul>
 *
 * @author LCH
 * @since 1.0
 */
@RestController
@RequestMapping("/api/article")
@Tag(name = "Articles", description = "Gestion des articles")
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    private UserService userService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    /**
     * Récupère l'ensemble des articles appartenant aux sujets
     * auxquels l'utilisateur authentifié est abonné.
     *
     * @return liste des articles accessibles à l'utilisateur
     */
    @Operation(
            summary = "Lister les articles abonnés",
            description = "Retourne les articles des sujets suivis par l'utilisateur connecté."
    )
    @GetMapping
    public List<ArticleDTO> subscribedArticles() {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.getUserByName(username);

        List<ArticleDTO> subscribedArticles =  articleService.getSubscribedArticlesForUser(userDTO.getId());

        return subscribedArticles;
    }

    /**
     * Récupère un article ainsi que l'ensemble de ses commentaires.
     *
     * @param id identifiant de l'article
     * @return article complet avec commentaires
     */
    @Operation(summary = "Consulter un article")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Article trouvé"),
            @ApiResponse(responseCode = "404", description = "Article introuvable")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ArticleWithCommentsDTO> getArticleById(@PathVariable Long id) {
        ArticleWithCommentsDTO articleWithCommentsDTO = articleService.getArticleById(id);
        if (articleWithCommentsDTO == null) {
            throw new ArticleNotFoundException("Article with ID " + id + " not found");
        }
        return ResponseEntity.ok(articleWithCommentsDTO);
    }

    /**
     * Crée un nouvel article.
     *
     * @param articleDTO données de l'article à créer
     * @return message de confirmation
     */
    @Operation(summary = "Créer un article")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Article créé"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping
    public ResponseEntity<String> createArticle(@RequestBody ArticleDTO articleDTO) {
        if (articleDTO.getContent() == null || articleDTO.getContent().trim().isEmpty()) {
            throw new InvalidArticleDataException("Invalid article data");
        }

        Article createdArticle = articleService.createArticle(articleDTO);
        if (createdArticle != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("New Article created");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Article");
        }
    }

    /**
     * Met à jour un article existant.
     *
     * @param id identifiant de l'article
     * @param articleDTO nouvelles données
     * @return message de confirmation
     */
    @Operation(summary = "Modifier un article")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Article modifié"),
            @ApiResponse(responseCode = "404", description = "Article introuvable")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateArticle(@PathVariable Long id, @RequestBody ArticleDTO articleDTO) {
        if (articleDTO.getContent() == null || articleDTO.getContent().trim().isEmpty()) {
            throw new InvalidArticleDataException("Invalid article data");
        }

        ArticleWithCommentsDTO existingArticle = articleService.getArticleById(id);
        if (existingArticle == null) {
            throw new ArticleNotFoundException("Article with ID " + id + " not found"); // 404: not found
        }

        Article updatedArticle = articleService.updateArticle(id, articleDTO);
        if (updatedArticle != null) {
            return ResponseEntity.ok().body("Article updated");
        } else {
            throw new UpdateArticleException("Failed to update Article");
        }
    }

    /**
     * Supprime un article existant.
     *
     * @param id identifiant de l'article
     * @return message de confirmation
     */
    @Operation(summary = "Supprimer un article")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Article supprimé"),
            @ApiResponse(responseCode = "404", description = "Article introuvable")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticleById(@PathVariable Long id) {
        ArticleWithCommentsDTO articleWithCommentsDTO = articleService.getArticleById(id);
        if (articleWithCommentsDTO == null) {
            throw new ArticleNotFoundException("Article with ID " + id + " not found"); // 404: not found
        }

        try {
            this.articleService.deleteArticleById(id);
            return ResponseEntity.ok().body("{\"message\": \"Article deleted successfully\"}");
        } catch (Exception e) {
            throw new DeleteArticleException("Failed to delete Article with ID " + id);
        }
    }
}
