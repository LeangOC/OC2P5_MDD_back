package com.openclassrooms.mddapi.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration de la politique CORS (Cross-Origin Resource Sharing)
 * de l'application.
 *
 * <p>
 * Cette configuration autorise les requêtes provenant du frontend Angular
 * afin de permettre les échanges entre le client et l'API REST.
 * </p>
 *
 * <p>
 * Les méthodes HTTP autorisées sont :
 * </p>
 * <ul>
 *     <li>GET</li>
 *     <li>POST</li>
 *     <li>PUT</li>
 *     <li>DELETE</li>
 *     <li>OPTIONS</li>
 * </ul>
 *
 * @author LCH
 * @since 1.0
 */
@Configuration
public class CorsConfig {
    /**
     * Crée un configurateur CORS personnalisé.
     *
     * @return une instance de {@link WebMvcConfigurer} configurée pour
     * autoriser les requêtes provenant du frontend Angular
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {

        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(
                    CorsRegistry registry) {

                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:4200","http://192.168.0.16:4200")
                        .allowedMethods(
                                "GET",
                                "POST",
                                "PUT",
                                "DELETE",
                                "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}