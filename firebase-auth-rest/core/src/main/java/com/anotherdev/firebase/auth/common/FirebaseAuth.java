package com.anotherdev.firebase.auth.common;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.provider.AuthCredential;
import com.anotherdev.firebase.auth.rest.api.model.SignInResponse;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.internal.InternalAuthProvider;

import io.reactivex.rxjava3.annotations.CheckReturnValue;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface FirebaseAuth extends InternalAuthProvider {

    @NonNull
    FirebaseApp getApp();

    @Nullable
    FirebaseUser getCurrentUser();

    boolean isSignedIn();

    @NonNull
    @CheckReturnValue
    Observable<FirebaseAuth> authStateChanges();

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> signInAnonymously();

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> createUserWithEmailAndPassword(String email, String password);

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> signInWithEmailAndPassword(String email, String password);

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> signInWithCredential(AuthCredential credential);

    void signOut();
}
