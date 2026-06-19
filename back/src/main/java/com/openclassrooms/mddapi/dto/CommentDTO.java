package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long userId;

    private String authorName; // Nom de l'auteur

    private Long articleId;

    @NotBlank
    //@Size(max = 5000)
    private String content;

    private Date createdAt;

}
