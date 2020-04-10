package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

public interface EmailAuthCredential extends AuthCredential {

    @NonNull
    @Override
    default String getProvider() {
        return "password";
    }

    @NonNull
    @Override
    default String getSignInMethod() {
        // TODO refactor this when support Email Link feature
        // !TextUtils.isEmpty(<SOME FIELD>) ? "password" : "emailLink";
        return "password";
    }

    @NonNull
    String getEmail();

    @NonNull
    String getPassword();
}
