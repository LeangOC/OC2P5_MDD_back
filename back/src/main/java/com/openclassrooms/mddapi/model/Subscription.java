package com.openclassrooms.mddapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente l'abonnement d'un utilisateur à un sujet.
 *
 * <p>
 * Cette entité matérialise la relation entre
 * un utilisateur et un sujet.
 * </p>
 *
 * @author LCH
 * @since 1.0
 */
@Entity
@Table(name = "subscriptions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "subject_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public static Subscription createNewSubscription(User user, Subject subject) {
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setSubject(subject);
        return subscription;
    }
}
