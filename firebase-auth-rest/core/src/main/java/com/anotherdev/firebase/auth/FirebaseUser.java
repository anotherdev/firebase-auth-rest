package com.anotherdev.firebase.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.provider.AuthCredential;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

public interface FirebaseUser {

    boolean isSignedIn();

    @Nullable
    String getIdToken();

    @NonNull
    JsonObject getUserInfo();

    @Nullable
    String getUid();

    @Nullable
    String getDisplayName();

    @Nullable
    String getEmail();

    @NonNull
    List<UserInfo> getProviderData();

    boolean isAnonymous();

    @NonNull
    Single<SignInResponse> linkWithCredential(@NonNull AuthCredential credential);

    @NonNull
    Single<SignInResponse> reauthenticate(@NonNull AuthCredential credential);

    @NonNull
    Completable updateProfile(@NonNull UserProfileChangeRequest request);

    @NonNull
    Completable updatePassword(@NonNull String newPassword);
}
