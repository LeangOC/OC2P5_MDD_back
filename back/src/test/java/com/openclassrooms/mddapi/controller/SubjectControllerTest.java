package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.SubjectDTO;
import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.exception.*;
import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.service.SubjectService;
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
class SubjectControllerTest {

    @Mock
    private SubjectService subjectService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SubjectController subjectController;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(
                subjectController,
                "userService",
                userService
        );
    }

    @Test
    void getAllSubjects_ShouldReturnSubjects() {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setId(10L);
        subjectDTO.setName("Java");

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

            when(subjectService.getAllSubjects(1L))
                    .thenReturn(List.of(subjectDTO));

            List<SubjectDTO> result =
                    subjectController.getAllSubjects();

            assertEquals(1, result.size());
            assertEquals("Java", result.get(0).getName());
        }
    }

    @Test
    void getSubjectById_ShouldReturnSubject() {

        SubjectDTO dto = new SubjectDTO();
        dto.setId(1L);
        dto.setName("Spring");

        when(subjectService.getSubjectById(1L))
                .thenReturn(dto);

        ResponseEntity<SubjectDTO> response =
                subjectController.getSubjectById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getSubjectById_ShouldThrowException() {

        when(subjectService.getSubjectById(1L))
                .thenReturn(null);

        assertThrows(
                SubjectNotFoundException.class,
                () -> subjectController.getSubjectById(1L)
        );
    }

    @Test
    void createSubject_ShouldReturnCreated() {

        SubjectDTO dto = new SubjectDTO();
        dto.setName("Java");

        when(subjectService.subjectExists("Java"))
                .thenReturn(false);

        when(subjectService.createSubject(dto))
                .thenReturn(new Subject());

        ResponseEntity<String> response =
                subjectController.createSubject(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Subject created", response.getBody());
    }

    @Test
    void createSubject_ShouldThrowInvalidDataException() {

        SubjectDTO dto = new SubjectDTO();
        dto.setName("");

        assertThrows(
                InvalidSubjectDataException.class,
                () -> subjectController.createSubject(dto)
        );
    }

    @Test
    void createSubject_ShouldThrowAlreadyExistsException() {

        SubjectDTO dto = new SubjectDTO();
        dto.setName("Java");

        when(subjectService.subjectExists("Java"))
                .thenReturn(true);

        assertThrows(
                SubjectAlreadyExistsException.class,
                () -> subjectController.createSubject(dto)
        );
    }

    @Test
    void updateSubject_ShouldReturnOk() {

        Long id = 1L;

        SubjectDTO dto = new SubjectDTO();
        dto.setName("Spring Boot");

        SubjectDTO existing = new SubjectDTO();
        existing.setId(id);

        when(subjectService.getSubjectById(id))
                .thenReturn(existing);

        when(subjectService.updateSubject(id, dto))
                .thenReturn(new Subject());

        ResponseEntity<String> response =
                subjectController.updateSubject(id, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Subject updated", response.getBody());
    }

    @Test
    void updateSubject_ShouldThrowNotFoundException() {

        Long id = 1L;

        SubjectDTO dto = new SubjectDTO();
        dto.setName("Spring Boot");

        when(subjectService.getSubjectById(id))
                .thenReturn(null);

        assertThrows(
                SubjectNotFoundException.class,
                () -> subjectController.updateSubject(id, dto)
        );
    }

    @Test
    void updateSubject_ShouldThrowInvalidDataException() {

        SubjectDTO dto = new SubjectDTO();
        dto.setName("");

        assertThrows(
                InvalidSubjectDataException.class,
                () -> subjectController.updateSubject(1L, dto)
        );
    }

    @Test
    void deleteSubject_ShouldReturnOk() {

        Long id = 1L;

        SubjectDTO dto = new SubjectDTO();
        dto.setId(id);

        when(subjectService.getSubjectById(id))
                .thenReturn(dto);

        ResponseEntity<String> response =
                subjectController.deleteSubjectById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(subjectService)
                .deleteSubjectById(id);
    }

    @Test
    void deleteSubject_ShouldThrowNotFoundException() {

        when(subjectService.getSubjectById(1L))
                .thenReturn(null);

        assertThrows(
                SubjectNotFoundException.class,
                () -> subjectController.deleteSubjectById(1L)
        );
    }

    @Test
    void deleteSubject_ShouldThrowDeleteException() {

        Long id = 1L;

        SubjectDTO dto = new SubjectDTO();
        dto.setId(id);

        when(subjectService.getSubjectById(id))
                .thenReturn(dto);

        doThrow(new RuntimeException("DB Error"))
                .when(subjectService)
                .deleteSubjectById(id);

        assertThrows(
                DeleteSubjectException.class,
                () -> subjectController.deleteSubjectById(id)
        );
    }
}