package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.exception.*;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.service.SubjectService;
import com.openclassrooms.mddapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * Contrôleur REST responsable de la gestion des sujets.
 *
 * <p>
 * Les sujets représentent les thématiques
 * auxquelles les utilisateurs peuvent s'abonner.
 * </p>
 *
 * @author LCH
 * @since 1.0
 */
@RestController
@RequestMapping("/api/subject")
@Tag(name = "Sujets", description = "Gestion des sujets")
public class SubjectController {
    private final SubjectService subjectService;

    @Autowired
    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Autowired
    private UserService userService;

    @Operation(summary = "Lister les sujets")
    @GetMapping
    public List<SubjectDTO> getAllSubjects() {
        // Récupérer l'utilisateur actuellement connecté
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UserDTO userDTO = userService.getUserByName(username);

        return subjectService.getAllSubjects(userDTO.getId());
    }

    @Operation(summary = "Consulter un sujet")
    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> getSubjectById(@PathVariable Long id) {
        SubjectDTO subjectDTO = subjectService.getSubjectById(id);
        if (subjectDTO == null) {
            throw new SubjectNotFoundException("Subject with ID " + id + " not found");
        }
        return ResponseEntity.ok(subjectDTO);
    }

    @Operation(summary = "Créer un sujet")
    @PostMapping
    public ResponseEntity<String> createSubject(@RequestBody SubjectDTO subjectDTO) {
        if (subjectDTO.getName() == null || subjectDTO.getName().trim().isEmpty()) {
            throw new InvalidSubjectDataException("Invalid Subject data");
        }

        // Vous pouvez vérifier si le sujet existe déjà ici et lever une exception si c'est le cas
        if (subjectService.subjectExists(subjectDTO.getName())) {
            throw new SubjectAlreadyExistsException("Subject with name " + subjectDTO.getName() + " already exists");
        }

        Subject createdSubject = subjectService.createSubject(subjectDTO);
        if (createdSubject != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("New Subject created");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create Subject");
        }
    }


    @Operation(summary = "Modifier un sujet")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSubject(@PathVariable Long id, @RequestBody SubjectDTO subjectDTO) {
        if (subjectDTO.getName() == null || subjectDTO.getName().trim().isEmpty()) {
            throw new InvalidSubjectDataException("Invalid Subject data");
        }

        SubjectDTO existingSubject = subjectService.getSubjectById(id);
        if (existingSubject == null) {
            throw new SubjectNotFoundException("Subject with ID " + id + " not found"); // 404: not found
        }
    
        Subject updatedSubject = subjectService.updateSubject(id, subjectDTO);
        if (updatedSubject != null) {
            return ResponseEntity.ok().body("Subject updated");   // 200: ok
        } else {
            throw new UpdateSubjectException("Failed to update Subject");    // Custom exception for failure to update
        }
    }


    @Operation(summary = "Supprimer un sujet")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubjectById(@PathVariable Long id) {
        SubjectDTO subjectDTO = subjectService.getSubjectById(id);
        if (subjectDTO == null) {
            throw new SubjectNotFoundException("Subject with ID " + id + " not found"); // 404: not found
        }
    
        try {
            this.subjectService.deleteSubjectById(id);
            return ResponseEntity.ok().body("{\"message\": \"Subject deleted successfully\"}");
        } catch (Exception e) {
            throw new DeleteSubjectException("Failed to delete Subject with ID " + id); // Custom exception for failure to delete
        }
    }

}
