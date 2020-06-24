package com.anotherdev.firebase.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.provider.EmailAuthCredential;
import com.anotherdev.firebase.auth.provider.IdpAuthCredential;
import com.anotherdev.firebase.auth.rest.api.model.SendPasswordResetEmailRequest;
import com.anotherdev.firebase.auth.rest.api.model.SendPasswordResetEmailResponse;
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
    Single<SignInResponse> signInWithEmailAndPassword(@NonNull EmailAuthCredential credential);

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> signInWithCredential(@NonNull IdpAuthCredential credential);

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> signInWithCustomToken(@NonNull String customToken);

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> linkWithCredential(@NonNull FirebaseUser user, @NonNull EmailAuthCredential credential);

    @NonNull
    @CheckReturnValue
    Single<SignInResponse> linkWithCredential(@NonNull FirebaseUser user, @NonNull IdpAuthCredential credential);

    @NonNull
    @CheckReturnValue
    Single<SendPasswordResetEmailResponse> sendPasswordResetEmail(SendPasswordResetEmailRequest request);

    void signOut();
}
