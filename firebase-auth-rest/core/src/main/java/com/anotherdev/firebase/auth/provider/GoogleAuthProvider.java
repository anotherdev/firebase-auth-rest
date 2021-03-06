package com.anotherdev.firebase.auth.provider;

import androidx.annotation.Nullable;

public class GoogleAuthProvider {

    private GoogleAuthProvider() {
    }

    public static IdpAuthCredential getCredential(@Nullable String idToken) {
        return new GoogleAuthCredential(idToken);
    }
}
