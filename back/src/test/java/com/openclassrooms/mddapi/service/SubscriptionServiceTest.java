package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Subject;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubjectRepository;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    @Test
    void isSubscribed_ShouldReturnTrue() {

        when(subscriptionRepository.existsByUserIdAndSubjectId(1L, 10L))
                .thenReturn(true);

        boolean result = subscriptionService.isSubscribed(10L, 1L);

        assertTrue(result);

        verify(subscriptionRepository)
                .existsByUserIdAndSubjectId(1L, 10L);
    }

    @Test
    void isSubscribed_ShouldReturnFalse() {

        when(subscriptionRepository.existsByUserIdAndSubjectId(1L, 10L))
                .thenReturn(false);

        boolean result = subscriptionService.isSubscribed(10L, 1L);

        assertFalse(result);

        verify(subscriptionRepository)
                .existsByUserIdAndSubjectId(1L, 10L);
    }

    @Test
    void subscribeToSubject_ShouldCreateSubscription() {

        User user = new User();
        user.setId(1L);

        Subject subject = new Subject();
        subject.setId(10L);

        Subscription subscription = new Subscription();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(subjectRepository.findById(10L))
                .thenReturn(Optional.of(subject));

        try (MockedStatic<Subscription> mockedStatic =
                     mockStatic(Subscription.class)) {

            mockedStatic.when(
                    () -> Subscription.createNewSubscription(user, subject)
            ).thenReturn(subscription);

            subscriptionService.subscribeToSubject(10L, 1L);

            verify(subscriptionRepository).save(subscription);
        }
    }

    @Test
    void subscribeToSubject_ShouldThrowException_WhenUserNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> subscriptionService.subscribeToSubject(10L, 1L)
        );

        verify(subjectRepository, never()).findById(anyLong());
        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void subscribeToSubject_ShouldThrowException_WhenSubjectNotFound() {

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(subjectRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> subscriptionService.subscribeToSubject(10L, 1L)
        );

        verify(subscriptionRepository, never()).save(any());
    }

    @Test
    void unsubscribeSubject_ShouldDeleteSubscription() {

        Subscription subscription = new Subscription();

        when(subscriptionRepository.findByUserIdAndSubjectId(1L, 10L))
                .thenReturn(Optional.of(subscription));

        subscriptionService.unsubscribeSubject(10L, 1L);

        verify(subscriptionRepository).delete(subscription);
    }

    @Test
    void unsubscribeSubject_ShouldThrowException_WhenSubscriptionNotFound() {

        when(subscriptionRepository.findByUserIdAndSubjectId(1L, 10L))
                .thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> subscriptionService.unsubscribeSubject(10L, 1L)
        );

        verify(subscriptionRepository, never()).delete(any());
    }
}