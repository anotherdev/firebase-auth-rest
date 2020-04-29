package com.anotherdev.firebase.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.provider.AuthCredential;
import com.anotherdev.firebase.auth.rest.api.model.SignInResponse;

import io.reactivex.rxjava3.core.Single;

public interface FirebaseUser {

    boolean isSignedIn();

    @Nullable
    String getIdToken();

    @Nullable
    String getUid();

    @Nullable
    String getDisplayName();

    @Nullable
    String getEmail();

    @NonNull
    Single<SignInResponse> linkWithCredential(AuthCredential credential);

    @NonNull
    Single<SignInResponse> updateProfile(UserProfileChangeRequest request);
}
