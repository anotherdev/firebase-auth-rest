package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.common.FirebaseAuth;

class GoogleAuthCredential implements IdpAuthCredential {

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
    public String getRequestUri(FirebaseAuth auth) {
        return "http://localhost";
    }

    @NonNull
    @Override
    public String getPostBody() {
        return String.format("id_token=%s&providerId=%s", idToken, getProvider());
    }
}
