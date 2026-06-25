package com.openclassrooms.mddapi.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Configuration principale de Spring Security.
 *
 * <p>
 * Cette configuration met en place :
 * </p>
 * <ul>
 *     <li>l'authentification JWT ;</li>
 *     <li>la désactivation des sessions HTTP ;</li>
 *     <li>la protection des routes REST ;</li>
 *     <li>l'encodage sécurisé des mots de passe.</li>
 * </ul>
 *
 * <p>
 * Les routes d'authentification sont accessibles sans
 * authentification tandis que toutes les autres routes
 * de l'API nécessitent un jeton JWT valide.
 * </p>
 *
 * @author Nicolas
 * @since 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    /**
     * Configure la chaîne de filtres de sécurité de l'application.
     *
     * @param http configuration Spring Security
     * @param jwtFilter filtre JWT personnalisé
     *
     * @return chaîne de filtres configurée
     *
     * @throws Exception si une erreur survient lors de la configuration
     */
    @Bean
    public SecurityFilterChain apiSecurity(
            HttpSecurity http,
            JwtRequestFilter jwtFilter) throws Exception {

        http
                .cors(cors -> {})
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    /**
     * Fournit l'encodeur BCrypt utilisé pour le stockage sécurisé
     * des mots de passe.
     *
     * @return encodeur BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * Expose le gestionnaire d'authentification Spring Security.
     *
     * @param authConfig configuration d'authentification
     * @return gestionnaire d'authentification
     *
     * @throws Exception si le gestionnaire ne peut être créé
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig)
            throws Exception {

        return authConfig.getAuthenticationManager();
    }
}