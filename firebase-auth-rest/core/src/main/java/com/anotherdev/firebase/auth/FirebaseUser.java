package com.anotherdev.firebase.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.provider.AuthCredential;
import com.anotherdev.firebase.auth.rest.api.model.SignInResponse;

import io.reactivex.rxjava3.core.Single;

public interface FirebaseUser {

    @Nullable
    String getIdToken();

    @Nullable
    String getUid();

    @NonNull
    Single<SignInResponse> linkWithCredential(AuthCredential credential);
}
