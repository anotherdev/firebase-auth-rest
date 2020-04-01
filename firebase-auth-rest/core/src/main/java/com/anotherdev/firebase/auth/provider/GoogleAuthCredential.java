package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

class GoogleAuthCredential implements AuthCredential {

    @Nullable
    private final String idToken;


    GoogleAuthCredential(@Nullable String idToken) {
        this.idToken = idToken;
    }

    @NonNull
    @Override
    public String getProvider() {
        return "google.com";
    }

    @NonNull
    @Override
    public String getSignInMethod() {
        return "google.com";
    }

    @NonNull
    @Override
    public String getRequestUri() {
        return "http://localhost";
    }

    @NonNull
    @Override
    public String getPostBody() {
        return String.format("id_token=%s&providerId=%s", idToken, getProvider());
    }
}
