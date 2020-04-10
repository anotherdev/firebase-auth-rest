package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

public interface AuthCredential {

    @NonNull
    String getProvider();

    @NonNull
    String getSignInMethod();

    default boolean returnSecureToken() {
        return true;
    }
}
