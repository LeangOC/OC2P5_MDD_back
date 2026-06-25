package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service métier responsable de la gestion des sujets.
 *
 * <p>
 * Les sujets représentent les thématiques auxquelles
 * les utilisateurs peuvent s'abonner.
 * </p>
 *
 * <p>
 * Ce service gère :
 * </p>
 *
 * <ul>
 *     <li>la création des sujets ;</li>
 *     <li>la récupération des sujets disponibles ;</li>
 *     <li>la modification ;</li>
 *     <li>la suppression.</li>
 * </ul>
 *
 * @author LCH
 * @since 1.0
 */
@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository, SubscriptionRepository subscriptionRepository) {

        this.subjectRepository = subjectRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Retourne l'ensemble des sujets disponibles.
     *
     * <p>
     * La réponse indique également si l'utilisateur connecté
     * suit déjà chaque sujet.
     * </p>
     *
     * @param userId identifiant utilisateur
     *
     * @return liste des sujets
     */
    public List<SubjectDTO> getAllSubjects(Long userId) {
        List<Subject> subjects = subjectRepository.findAll();
        return subjects.stream().map(subject -> {
            SubjectDTO dto = this.toDTO(subject);
            dto.setFollowed(subscriptionRepository.existsByUserIdAndSubjectId(userId, subject.getId()));
            return dto;
        }).collect(Collectors.toList());
    }

    public SubjectDTO getSubjectById(Long id) {
        Optional<Subject> optionalSubject = subjectRepository.findById(id);
        return optionalSubject.map(this::toDTO).orElse(null);
    }

    public SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(subject.getId(), subject.getName(), subject.getDescription());
    }

    /**
     * Crée un nouveau sujet.
     *
     * @param subjectDTO informations du sujet
     *
     * @return sujet créé
     */
    public Subject createSubject(SubjectDTO subjectDTO) {
        Subject subject = new Subject(subjectDTO.getName());
        return subjectRepository.save(subject);
    }

    /**
     * Supprime un sujet.
     *
     * @param id identifiant du sujet
     */
    public void deleteSubjectById(Long id) {
        subjectRepository.deleteById(id);
    }

    public Subject updateSubject(Long id, SubjectDTO subjectDTO) {
        Subject subject = new Subject(subjectDTO.getName());
        subject.setId(id);


        return subjectRepository.save(subject);
    }

    public boolean subjectExists(String name) {
         return subjectRepository.findByName(name).isPresent();
    }

}
