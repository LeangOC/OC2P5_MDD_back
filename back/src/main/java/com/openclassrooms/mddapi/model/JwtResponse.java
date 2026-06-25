package com.openclassrooms.mddapi.model;

import java.io.Serializable;

/**
 * Contient le jeton JWT généré après
 * une authentification réussie.
 */
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;

    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public String getToken() {
        return this.jwttoken;
    }
}