package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST chargé de la gestion
 * des abonnements aux sujets.
 *
 * <p>
 * Permet à un utilisateur de suivre
 * ou de ne plus suivre un sujet.
 * </p>
 *
 * @author LCH
 * @since 1.0
 */
@RestController
@RequestMapping("/api/subscription")
@Tag(name = "Abonnements", description = "Gestion des abonnements aux sujets")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Vérifie si un utilisateur est abonné à un sujet.
     *
     * @param subjectId identifiant du sujet
     * @param userId identifiant utilisateur
     * @return true si l'abonnement existe
     */
    @Operation(summary = "Vérifier un abonnement")
    @GetMapping("/{subjectId}/{userId}")
    public ResponseEntity<Boolean> isSubscribed(@PathVariable Long subjectId, @PathVariable Long userId) {
        boolean isSubscribed = subscriptionService.isSubscribed(subjectId, userId);
        return ResponseEntity.ok(isSubscribed);
    }

    /**
     * Abonne un utilisateur à un sujet.
     *
     * @param request contient l'identifiant utilisateur
     *                et l'identifiant du sujet
     * @return message de confirmation
     */
    @Operation(summary = "S'abonner à un sujet")
    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, String>> subscribeToSubject(@RequestBody Map<String, Long> request) {
        Long subjectId = request.get("subjectId");
        Long userId = request.get("userId");
        subscriptionService.subscribeToSubject(subjectId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Subscribed successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Supprime un abonnement existant.
     *
     * @param subjectId identifiant du sujet
     * @param userId identifiant utilisateur
     * @return message de confirmation
     */
    @Operation(summary = "Se désabonner d'un sujet")
    @DeleteMapping("/unsubscribe/{subjectId}/{userId}")
    public ResponseEntity<Map<String, String>> unsubscribeSubject(@PathVariable Long subjectId, @PathVariable Long userId) {
        subscriptionService.unsubscribeSubject(subjectId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Unsubscribed successfully");

        return ResponseEntity.ok(response);

    }
}
