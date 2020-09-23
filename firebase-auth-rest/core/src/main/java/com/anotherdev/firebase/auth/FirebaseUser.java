package com.anotherdev.firebase.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.provider.AuthCredential;
import com.anotherdev.firebase.auth.provider.Provider;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.rxjava3.annotations.CheckReturnValue;
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

    boolean isSignedInWith(@NonNull Provider provider);

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> linkWithCredential(@NonNull AuthCredential credential);

    @NonNull
    @CheckReturnValue
    Completable unlink(@NonNull String provider);

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> reauthenticate(@NonNull AuthCredential credential);

    @NonNull
    @CheckReturnValue
    Completable reload();

    @NonNull
    @CheckReturnValue
    Completable updateProfile(@NonNull UserProfileChangeRequest request);

    @NonNull
    @CheckReturnValue
    Completable updateEmail(@NonNull String newEmail);

    @NonNull
    @CheckReturnValue
    Completable updatePassword(@NonNull String newPassword);
}
