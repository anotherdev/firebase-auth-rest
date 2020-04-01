package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

class FacebookAuthCredential implements AuthCredential {

    @NonNull
    private final String token;

    @NonNull
    private final String oAuthRedirectUri;


    FacebookAuthCredential(@NonNull String token, @NonNull String oAuthRedirectUri) {
        this.token = token;
        this.oAuthRedirectUri = oAuthRedirectUri;
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
        return oAuthRedirectUri;
    }

    @NonNull
    @Override
    public String getPostBody() {
        return String.format("access_token=%s&providerId=%s", token, getProvider());
    }
}
