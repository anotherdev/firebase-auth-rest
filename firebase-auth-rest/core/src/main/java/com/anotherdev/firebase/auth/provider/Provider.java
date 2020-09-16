package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

public enum Provider {

    EMAIL (
            "password",
            "password"
    ),

    EMAIL_LINK (
            "password",
            "emailLink"
    ),

    FACEBOOK (
            "facebook.com",
            "facebook.com"
    ),

    GITHUB (
            "github.com",
            "github.com"
    ),

    GOOGLE (
            "google.com",
            "google.com"
    ),

    PHONE (
            "phone",
            "phone"
    ),

    PLAY_GAMES (
            "playgames.google.com",
            "playgames.google.com"
    ),

    TWITTER (
            "twitter.com",
            "twitter.com"
    );


    @NonNull
    private final String providerId;
    @NonNull
    private final String signInMethod;


    Provider(@NonNull String providerId, @NonNull String signInMethod) {
        this.providerId = providerId;
        this.signInMethod = signInMethod;
    }

    @NonNull
    public String getProviderId() {
        return providerId;
    }

    @NonNull
    public String getSignInMethod() {
        return signInMethod;
    }
}
