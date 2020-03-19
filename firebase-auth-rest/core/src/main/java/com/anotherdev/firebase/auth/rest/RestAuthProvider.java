package com.anotherdev.firebase.auth.rest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.common.FirebaseAuth;
import com.anotherdev.firebase.auth.data.Data;
import com.anotherdev.firebase.auth.rest.api.RestAuthApi;
import com.anotherdev.firebase.auth.rest.api.model.SignInAnonymouslyRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInAnonymouslyResponse;
import com.f2prateek.rx.preferences2.Preference;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.internal.IdTokenListener;
import com.google.firebase.internal.InternalTokenResult;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import timber.log.Timber;

public class RestAuthProvider implements FirebaseAuth {

    private final FirebaseApp app;
    private final Preference<FirebaseUser> user;

    private final List<IdTokenListener> listeners = new ArrayList<>();


    public RestAuthProvider(FirebaseApp app) {
        this.app = app;
        user = Data.from(app.getApplicationContext()).getCurrentUser();
    }

    @Override
    public FirebaseUser getCurrentUser() {
        return user.get();
    }

    @Override
    public Single<SignInAnonymouslyResponse> signInAnonymously() {
        return RestAuthApi.auth()
                .signInAnonymously(new SignInAnonymouslyRequest())
                .map(saveAnonymousUser);
    }

    @Override
    public void signOut() {
        user.delete();
    }

    @NonNull
    @Override
    public Task<GetTokenResult> getAccessToken(boolean forceRefresh) {
        Timber.e("getAccessToken() called");
        TaskCompletionSource<GetTokenResult> source = new TaskCompletionSource<>();
        source.trySetException(new UnsupportedOperationException("Not implemented yet"));
        return source.getTask();
    }

    @Nullable
    @Override
    public String getUid() {
        return user.get().getUid();
    }

    @Override
    public void addIdTokenListener(@NonNull IdTokenListener idTokenListener) {
        listeners.add(idTokenListener);
    }

    @Override
    public void removeIdTokenListener(@NonNull IdTokenListener idTokenListener) {
        listeners.remove(idTokenListener);
    }


    private void saveCurrentUser(String idToken, String refreshToken) {
        user.set(FirebaseUser.from(idToken, refreshToken));

        for (IdTokenListener l : listeners) {
            l.onIdTokenChanged(new InternalTokenResult(idToken));
        }
    }

    private final Function<SignInAnonymouslyResponse, SignInAnonymouslyResponse> saveAnonymousUser = response -> {
        String idToken = response.getIdToken();
        String refreshToken = response.getRefreshToken();
        saveCurrentUser(idToken, refreshToken);
        return response;
    };
}
