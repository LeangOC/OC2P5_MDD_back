package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
/**
 * Objet de transfert représentant un article
 * accompagné de l'ensemble de ses commentaires.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleWithCommentsDTO {
    private Long id;

    private Long userId;

    private String userName; // Nom de l'auteur

    private Long subjectId;

    private String subjectName;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 5000)
    private String content;

    private Date publishedAt;

    private List<CommentDTO> comments;

}
