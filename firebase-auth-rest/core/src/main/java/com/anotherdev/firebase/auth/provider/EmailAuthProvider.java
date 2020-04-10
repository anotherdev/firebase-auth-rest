package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

public class EmailAuthProvider {

    private EmailAuthProvider() {
    }

    @NonNull
    public static EmailAuthCredential getCredential(@NonNull String email, @NonNull String password) {
        return new EmailAuthCredentialImpl(email, password);
    }
}
