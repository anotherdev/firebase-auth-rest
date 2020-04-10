package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

import com.anotherdev.firebase.auth.common.FirebaseAuth;

public interface IdpAuthCredential extends AuthCredential {

    @NonNull
    String getRequestUri(FirebaseAuth auth);

    @NonNull
    String getPostBody();

    default boolean returnIdpCredential() {
        return true;
    }
}
