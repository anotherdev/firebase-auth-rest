package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

import com.anotherdev.firebase.auth.common.FirebaseAuth;

public interface AuthCredential {

    @NonNull
    String getProvider();

    @NonNull
    String getSignInMethod();

    @NonNull
    String getRequestUri(FirebaseAuth auth);

    @NonNull
    String getPostBody();

    default boolean returnSecureToken() {
        return true;
    }

    default boolean returnIdpCredential() {
        return true;
    }
}
