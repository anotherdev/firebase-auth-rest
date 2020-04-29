package com.anotherdev.firebase.auth.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.provider.IdpAuthCredential;
import com.anotherdev.firebase.auth.rest.api.model.SignInResponse;
import com.google.firebase.FirebaseApp;

import io.reactivex.rxjava3.annotations.CheckReturnValue;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface FirebaseAuth {

    @NonNull
    FirebaseApp getApp();

    @Nullable
    @CheckReturnValue
    FirebaseUser getCurrentUser();

    @NonNull
    @CheckReturnValue
    Observable<FirebaseUser> currentUser();

    boolean isSignedIn();

    @NonNull
    @CheckReturnValue
    Observable<FirebaseAuth> authStateChanges();

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> signInAnonymously();

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> createUserWithEmailAndPassword(@NonNull String email, @NonNull String password);

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> signInWithEmailAndPassword(@NonNull String email, @NonNull String password);

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> signInWithCredential(@NonNull IdpAuthCredential credential);

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> linkWithCredential(@NonNull FirebaseUser user, @NonNull IdpAuthCredential credential);

    void signOut();
}
