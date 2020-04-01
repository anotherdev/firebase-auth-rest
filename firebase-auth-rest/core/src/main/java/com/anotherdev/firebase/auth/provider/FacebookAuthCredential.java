package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

class FacebookAuthCredential implements AuthCredential {

    @NonNull
    private final String token;


    FacebookAuthCredential(@NonNull String token) {
        this.token = token;
    }

    @NonNull
    @Override
    public String getProvider() {
        return "facebook.com";
    }

    @NonNull
    @Override
    public String getSignInMethod() {
        return "facebook.com";
    }

    @NonNull
    @Override
    public String getRequestUri() {
        return "";
    }

    @NonNull
    @Override
    public String getPostBody() {
        return String.format("access_token=%s&providerId=facebook.com", token);
    }
}
