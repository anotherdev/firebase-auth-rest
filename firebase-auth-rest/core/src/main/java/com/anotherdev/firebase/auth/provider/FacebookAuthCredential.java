package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

import com.anotherdev.firebase.auth.common.FirebaseAuth;

class FacebookAuthCredential implements IdpAuthCredential {

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
    public String getRequestUri(FirebaseAuth auth) {
        String projectId = auth.getApp().getOptions().getProjectId();
        return String.format("https://%s.firebaseapp.com/__/auth/handler", projectId);
    }

    @NonNull
    @Override
    public String getPostBody() {
        return String.format("access_token=%s&providerId=%s", token, getProvider());
    }
}
