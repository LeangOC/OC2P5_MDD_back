package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Objet de transfert utilisé pour les échanges
 * relatifs aux articles entre l'API REST
 * et le frontend Angular.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ArticleDTO {
    private Long id;

    private Long userId;

    private String userName;

    private Long subjectId;

    private String subjectName;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    @Size(max = 5000)
    private String content;

    private Date publishedAt;

}
