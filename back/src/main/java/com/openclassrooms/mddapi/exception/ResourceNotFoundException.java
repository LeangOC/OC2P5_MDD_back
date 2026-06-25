package com.openclassrooms.mddapi.exception;
/**
 * Exception levée lorsqu'une ressource demandée
 * n'existe pas dans le système.
 *
 * <p>
 * Cette exception est généralement utilisée lors
 * de la recherche d'un article, d'un utilisateur,
 * d'un commentaire ou d'un sujet inexistant.
 * </p>
 *
 * @author Nicolas
 * @since 1.0
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Crée une exception avec un message personnalisé.
     *
     * @param message description de l'erreur
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}