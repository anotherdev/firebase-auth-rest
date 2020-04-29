package com.anotherdev.firebase.auth;

@SuppressWarnings("WeakerAccess")
public class SignInResponse {

    String idToken;
    String email;
    String refreshToken;
    String expiresIn;
    String localId;


    public String getIdToken() {
        return idToken;
    }

    public String getEmail() {
        return email;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getExpiresIn() {
        return expiresIn;
    }

    public String getLocalId() {
        return localId;
    }
}
