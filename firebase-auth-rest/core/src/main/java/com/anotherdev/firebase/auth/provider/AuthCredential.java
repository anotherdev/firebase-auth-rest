package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

public interface AuthCredential {

    @NonNull
    String getProvider();

    @NonNull
    String getSignInMethod();

    @NonNull
    String getRequestUri();

    @NonNull
    String getPostBody();

    default boolean returnSecureToken() {
        return true;
    }

    default boolean returnIdpCredential() {
        return true;
    }
}
