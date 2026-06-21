package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubjectService subjectService;

    @Test
    void getAllSubjects_ShouldReturnEmptyList() {

        when(subjectRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<SubjectDTO> result = subjectService.getAllSubjects(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(subjectRepository).findAll();
        verifyNoInteractions(subscriptionRepository);
    }

    @Test
    void getAllSubjects_ShouldSetFollowedToTrue() {

        Long userId = 1L;

        Subject subject = new Subject("Java");
        subject.setId(10L);
        subject.setDescription("Java description");

        when(subjectRepository.findAll())
                .thenReturn(List.of(subject));

        when(subscriptionRepository.existsByUserIdAndSubjectId(userId, 10L))
                .thenReturn(true);

        List<SubjectDTO> result = subjectService.getAllSubjects(userId);

        assertEquals(1, result.size());
        assertEquals("Java", result.get(0).getName());
        assertTrue(result.get(0).isFollowed());

        verify(subscriptionRepository)
                .existsByUserIdAndSubjectId(userId, 10L);
    }

    @Test
    void getAllSubjects_ShouldSetFollowedToFalse() {

        Long userId = 1L;

        Subject subject = new Subject("Spring");
        subject.setId(20L);
        subject.setDescription("Spring description");

        when(subjectRepository.findAll())
                .thenReturn(List.of(subject));

        when(subscriptionRepository.existsByUserIdAndSubjectId(userId, 20L))
                .thenReturn(false);

        List<SubjectDTO> result = subjectService.getAllSubjects(userId);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isFollowed());

        verify(subscriptionRepository)
                .existsByUserIdAndSubjectId(userId, 20L);
    }

    @Test
    void getSubjectById_ShouldReturnSubjectDTO_WhenSubjectExists() {

        Subject subject = new Subject("Java");
        subject.setId(1L);
        subject.setDescription("Description");

        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));

        SubjectDTO result = subjectService.getSubjectById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java", result.getName());

        verify(subjectRepository).findById(1L);
    }

    @Test
    void getSubjectById_ShouldReturnNull_WhenSubjectDoesNotExist() {

        when(subjectRepository.findById(1L))
                .thenReturn(Optional.empty());

        SubjectDTO result = subjectService.getSubjectById(1L);

        assertNull(result);

        verify(subjectRepository).findById(1L);
    }

    @Test
    void toDTO_ShouldConvertSubjectToDTO() {

        Subject subject = new Subject("Java");
        subject.setId(1L);
        subject.setDescription("Description");

        SubjectDTO dto = subjectService.toDTO(subject);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Java", dto.getName());
        assertEquals("Description", dto.getDescription());
    }

    @Test
    void createSubject_ShouldSaveSubject() {

        SubjectDTO dto = new SubjectDTO();
        dto.setName("Java");

        Subject savedSubject = new Subject("Java");
        savedSubject.setId(1L);

        when(subjectRepository.save(any(Subject.class)))
                .thenReturn(savedSubject);

        Subject result = subjectService.createSubject(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Java", result.getName());

        verify(subjectRepository).save(any(Subject.class));
    }

    @Test
    void updateSubject_ShouldSaveUpdatedSubject() {

        SubjectDTO dto = new SubjectDTO();
        dto.setName("Spring Boot");

        Subject updatedSubject = new Subject("Spring Boot");
        updatedSubject.setId(1L);

        when(subjectRepository.save(any(Subject.class)))
                .thenReturn(updatedSubject);

        Subject result = subjectService.updateSubject(1L, dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Spring Boot", result.getName());

        verify(subjectRepository).save(any(Subject.class));
    }

    @Test
    void deleteSubjectById_ShouldCallRepository() {

        doNothing().when(subjectRepository).deleteById(1L);

        subjectService.deleteSubjectById(1L);

        verify(subjectRepository).deleteById(1L);
    }

    @Test
    void subjectExists_ShouldReturnTrue() {

        Subject subject = new Subject("Java");

        when(subjectRepository.findByName("Java"))
                .thenReturn(Optional.of(subject));

        boolean result = subjectService.subjectExists("Java");

        assertTrue(result);

        verify(subjectRepository).findByName("Java");
    }

    @Test
    void subjectExists_ShouldReturnFalse() {

        when(subjectRepository.findByName("Java"))
                .thenReturn(Optional.empty());

        boolean result = subjectService.subjectExists("Java");

        assertFalse(result);

        verify(subjectRepository).findByName("Java");
    }
}