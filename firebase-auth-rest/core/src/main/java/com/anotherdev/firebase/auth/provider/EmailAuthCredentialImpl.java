package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

class EmailAuthCredentialImpl implements EmailAuthCredential {

    @NonNull
    private final String email;
    @NonNull
    private final String password;


    EmailAuthCredentialImpl(@NonNull String email, @NonNull String password) {
        this.email = email;
        this.password = password;
    }

    @NonNull
    @Override
    public String getEmail() {
        return email;
    }

    @NonNull
    @Override
    public String getPassword() {
        return password;
    }
}
