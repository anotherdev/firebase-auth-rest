package com.anotherdev.firebase.auth.rest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.common.FirebaseAuth;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.internal.IdTokenListener;

public class RestAuthProvider implements FirebaseAuth {

    private final FirebaseApp app;


    public RestAuthProvider(FirebaseApp app) {
        this.app = app;
    }

    @Override
    public void signInAnonymously() {
    }

    @NonNull
    @Override
    public Task<GetTokenResult> getAccessToken(boolean forceRefresh) {
        return null;
    }

    @Nullable
    @Override
    public String getUid() {
        return null;
    }

    @Override
    public void addIdTokenListener(@NonNull IdTokenListener idTokenListener) {
    }

    @Override
    public void removeIdTokenListener(@NonNull IdTokenListener idTokenListener) {
    }
}
