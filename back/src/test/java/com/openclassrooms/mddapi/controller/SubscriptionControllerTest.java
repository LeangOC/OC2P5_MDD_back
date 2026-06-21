package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @Test
    void isSubscribed_ShouldReturnTrue() {

        when(subscriptionService.isSubscribed(1L, 2L))
                .thenReturn(true);

        ResponseEntity<Boolean> response =
                subscriptionController.isSubscribed(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());

        verify(subscriptionService)
                .isSubscribed(1L, 2L);
    }

    @Test
    void isSubscribed_ShouldReturnFalse() {

        when(subscriptionService.isSubscribed(1L, 2L))
                .thenReturn(false);

        ResponseEntity<Boolean> response =
                subscriptionController.isSubscribed(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody());

        verify(subscriptionService)
                .isSubscribed(1L, 2L);
    }

    @Test
    void subscribeToSubject_ShouldReturnCreated() {

        Map<String, Long> request = new HashMap<>();
        request.put("subjectId", 1L);
        request.put("userId", 2L);

        ResponseEntity<Map<String, String>> response =
                subscriptionController.subscribeToSubject(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(
                "Subscribed successfully",
                response.getBody().get("message")
        );

        verify(subscriptionService)
                .subscribeToSubject(1L, 2L);
    }

    @Test
    void unsubscribeSubject_ShouldReturnOk() {

        ResponseEntity<Map<String, String>> response =
                subscriptionController.unsubscribeSubject(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertNotNull(response.getBody());
        assertEquals(
                "Unsubscribed successfully",
                response.getBody().get("message")
        );

        verify(subscriptionService)
                .unsubscribeSubject(1L, 2L);
    }

    @Test
    void subscribeToSubject_ShouldPropagateException() {

        Map<String, Long> request = new HashMap<>();
        request.put("subjectId", 1L);
        request.put("userId", 2L);

        doThrow(new RuntimeException("Database error"))
                .when(subscriptionService)
                .subscribeToSubject(1L, 2L);

        assertThrows(
                RuntimeException.class,
                () -> subscriptionController.subscribeToSubject(request)
        );
    }

    @Test
    void unsubscribeSubject_ShouldPropagateException() {

        doThrow(new RuntimeException("Database error"))
                .when(subscriptionService)
                .unsubscribeSubject(1L, 2L);

        assertThrows(
                RuntimeException.class,
                () -> subscriptionController.unsubscribeSubject(1L, 2L)
        );
    }
}