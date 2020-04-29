package com.anotherdev.firebase.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.provider.AuthCredential;

import io.reactivex.rxjava3.core.Completable;
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
    Single<SignInResponse> linkWithCredential(@NonNull AuthCredential credential);

    @NonNull
    Completable updateProfile(@NonNull UserProfileChangeRequest request);

    @NonNull
    Completable updatePassword(@NonNull String newPassword);
}
