package com.openclassrooms.mddapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objet de transfert représentant un sujet.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {

    private Long id;
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Size(max = 5000)
    private String description;
    private boolean isFollowed;

    public SubjectDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isFollowed = false;
    }
}
