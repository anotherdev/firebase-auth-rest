package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

public class FacebookAuthProvider {

    private FacebookAuthProvider() {
    }

    @NonNull
    public static AuthCredential getCredential(@NonNull String token) {
        return new FacebookAuthCredential(token);
    }
}
