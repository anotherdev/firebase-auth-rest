package com.anotherdev.firebase.auth.common;

import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.provider.AuthCredential;
import com.anotherdev.firebase.auth.rest.api.model.SignInResponse;
import com.google.firebase.auth.internal.InternalAuthProvider;

import io.reactivex.rxjava3.annotations.CheckReturnValue;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface FirebaseAuth extends InternalAuthProvider {

    @Nullable
    FirebaseUser getCurrentUser();

    boolean isSignedIn();

    @CheckReturnValue
    Observable<FirebaseAuth> authStateChanges();

    @CheckReturnValue
    Single<SignInResponse> signInAnonymously();

    @CheckReturnValue
    Single<SignInResponse> signInWithCredential(AuthCredential credential);

    void signOut();
}
