package com.anotherdev.firebase.auth.rest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.common.FirebaseAuth;
import com.anotherdev.firebase.auth.data.Data;
import com.anotherdev.firebase.auth.provider.IdpAuthCredential;
import com.anotherdev.firebase.auth.rest.api.RestAuthApi;
import com.anotherdev.firebase.auth.rest.api.model.ExchangeTokenRequest;
import com.anotherdev.firebase.auth.rest.api.model.ImmutableSignInWithIdpRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInResponse;
import com.anotherdev.firebase.auth.rest.api.model.SignInWithEmailPasswordRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInWithIdpRequest;
import com.anotherdev.firebase.auth.util.IdTokenParser;
import com.anotherdev.firebase.auth.util.RxUtil;
import com.f2prateek.rx.preferences2.Preference;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.internal.IdTokenListener;
import com.google.firebase.internal.InternalTokenResult;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.internal.functions.Functions;
import timber.log.Timber;

public class RestAuthProvider implements FirebaseAuth {

    private static final FirebaseUser SIGNED_OUT = FirebaseUser.from("SIGNED_OUT", null, null);

    private final FirebaseApp app;
    private final Preference<FirebaseUser> user;

    private final List<IdTokenListener> listeners = new ArrayList<>();


    public RestAuthProvider(FirebaseApp app) {
        this.app = app;
        user = Data.from(app.getApplicationContext()).getCurrentUser(SIGNED_OUT);
    }

    @NonNull
    @Override
    public FirebaseApp getApp() {
        return app;
    }

    @Nullable
    @Override
    public FirebaseUser getCurrentUser() {
        return user.isSet() ? user.get() : null;
    }

    @Override
    public boolean isSignedIn() {
        return user.isSet();
    }

    @NonNull
    @Override
    public Observable<FirebaseAuth> authStateChanges() {
        return RxJavaBridge.toV3Observable(user.asObservable())
                .map(firebaseUser -> RestAuthProvider.this);
    }

    @NonNull
    @Override
    public Single<SignInResponse> signInAnonymously() {
        return RestAuthApi.auth()
                .signInAnonymously(SignInRequest.builder().build())
                .map(saveAnonymousUser);
    }

    @NonNull
    @Override
    public Single<SignInResponse> createUserWithEmailAndPassword(@NonNull String email, @NonNull String password) {
        SignInWithEmailPasswordRequest request = SignInWithEmailPasswordRequest.builder()
                .email(email)
                .password(password)
                .build();
        return RestAuthApi.auth()
                .createUserWithEmailAndPassword(request)
                .map(saveAnonymousUser);
    }

    @NonNull
    @Override
    public Single<SignInResponse> signInWithEmailAndPassword(@NonNull String email, @NonNull String password) {
        SignInWithEmailPasswordRequest request = SignInWithEmailPasswordRequest.builder()
                .email(email)
                .password(password)
                .build();
        return RestAuthApi.auth()
                .signInWithEmailAndPassword(request)
                .map(saveAnonymousUser);
    }

    @NonNull
    @Override
    public Single<SignInResponse> signInWithCredential(@NonNull IdpAuthCredential credential) {
        ImmutableSignInWithIdpRequest.Builder builder = SignInWithIdpRequest.builder();
        return performSignInWithCredential(builder, credential);
    }

    @NonNull
    @Override
    public Single<SignInResponse> linkWithCredential(@NonNull FirebaseUser user, @NonNull IdpAuthCredential credential) {
        String idToken = user.getIdToken();
        ImmutableSignInWithIdpRequest.Builder builder = SignInWithIdpRequest.builder().idToken(idToken);
        return performSignInWithCredential(builder, credential);
    }

    private Single<SignInResponse> performSignInWithCredential(ImmutableSignInWithIdpRequest.Builder builder,
                                                               @NonNull IdpAuthCredential credential ) {
        SignInWithIdpRequest request = builder
                .requestUri(credential.getRequestUri(this))
                .postBody(credential.getPostBody())
                .build();
        return RestAuthApi.auth()
                .signInWithCredential(request)
                .map(saveAnonymousUser);
    }

    @Override
    public void signOut() {
        user.delete();
    }

    private GetTokenResult getTokenResultLocal(@NonNull FirebaseUser user) {
        String idToken = user.getIdToken();
        Map<String, Object> map = IdTokenParser.parseIdTokenToMap(idToken);
        return new GetTokenResult(idToken, map);
    }

    @NonNull
    @Override
    public Task<GetTokenResult> getAccessToken(boolean forceRefresh) {
        Timber.d("getAccessToken(%s)", forceRefresh);
        TaskCompletionSource<GetTokenResult> source = new TaskCompletionSource<>();
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            boolean needRefresh = forceRefresh || user.isExpired();
            if (!needRefresh) {
                source.trySetResult(getTokenResultLocal(user));
            } else {
                ExchangeTokenRequest request = ExchangeTokenRequest.builder()
                        .refreshToken(user.getRefreshToken())
                        .build();

                //noinspection ResultOfMethodCallIgnored
                RestAuthApi.token()
                        .exchangeToken(request)
                        .map(response -> {
                            saveCurrentUser(response.getIdToken(), response.getRefreshToken());
                            return response;
                        })
                        .doOnSuccess(response -> source.trySetResult(getTokenResultLocal(user)))
                        .doOnError(e -> source.trySetException(new Exception(e)))
                        .subscribe(Functions.emptyConsumer(), RxUtil.ON_ERROR_LOG_V3);
            }
        } else {
            source.trySetException(new FirebaseNoSignedInUserException("Please sign in before trying to get a token."));
        }
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
        user.set(FirebaseUser.from(app.getName(), idToken, refreshToken));

        for (IdTokenListener l : listeners) {
            l.onIdTokenChanged(new InternalTokenResult(idToken));
        }
    }


    private final Function<SignInResponse, SignInResponse> saveAnonymousUser = response -> {
        String idToken = response.getIdToken();
        String refreshToken = response.getRefreshToken();
        saveCurrentUser(idToken, refreshToken);
        return response;
    };
}
