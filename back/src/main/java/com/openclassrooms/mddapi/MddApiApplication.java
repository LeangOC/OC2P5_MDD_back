package com.openclassrooms.mddapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * Point d'entrée principal de l'application MDD API.
 *
 * <p>
 * Cette classe initialise le contexte Spring Boot
 * et démarre l'ensemble des composants de l'application.
 * </p>
 *
 * <p>
 * L'application expose une API REST sécurisée par JWT
 * destinée à être consommée par un frontend Angular.
 * </p>
 *
 * @author LCH
 * @since 1.0
 */
@SpringBootApplication
public class MddApiApplication {
	/**
	 * Lance l'application Spring Boot.
	 *
	 * @param args arguments de démarrage
	 */
	public static void main(String[] args) {
		SpringApplication.run(MddApiApplication.class, args);
	}

}
