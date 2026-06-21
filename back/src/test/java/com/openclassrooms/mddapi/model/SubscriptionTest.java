package com.openclassrooms.mddapi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubscriptionTest {

    @Test
    void createNewSubscription_ShouldCreateSubscriptionCorrectly() {

        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        Subject subject = new Subject();
        subject.setId(10L);
        subject.setName("Java");

        Subscription subscription =
                Subscription.createNewSubscription(user, subject);

        assertNotNull(subscription);
        assertEquals(user, subscription.getUser());
        assertEquals(subject, subscription.getSubject());
    }

    @Test
    void lombokGettersAndSetters_ShouldWork() {

        User user = new User();
        Subject subject = new Subject();

        Subscription subscription = new Subscription();

        subscription.setId(1L);
        subscription.setUser(user);
        subscription.setSubject(subject);

        assertEquals(1L, subscription.getId());
        assertEquals(user, subscription.getUser());
        assertEquals(subject, subscription.getSubject());
    }

    @Test
    void allArgsConstructor_ShouldCreateSubscription() {

        User user = new User();
        Subject subject = new Subject();

        Subscription subscription = new Subscription(
                1L,
                user,
                subject
        );

        assertEquals(1L, subscription.getId());
        assertEquals(user, subscription.getUser());
        assertEquals(subject, subscription.getSubject());
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptySubscription() {

        Subscription subscription = new Subscription();

        assertNotNull(subscription);
        assertNull(subscription.getId());
        assertNull(subscription.getUser());
        assertNull(subscription.getSubject());
    }
}