package com.anotherdev.firebase.auth.common;

import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.rest.api.model.SignInAnonymouslyResponse;
import com.google.firebase.auth.internal.InternalAuthProvider;

import io.reactivex.rxjava3.annotations.CheckReturnValue;
import io.reactivex.rxjava3.core.Single;

public interface FirebaseAuth extends InternalAuthProvider {

    @Nullable
    FirebaseUser getCurrentUser();

    @CheckReturnValue
    Single<SignInAnonymouslyResponse> signInAnonymously();

    void signOut();
}
