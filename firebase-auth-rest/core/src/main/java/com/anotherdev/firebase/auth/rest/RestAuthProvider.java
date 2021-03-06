package com.anotherdev.firebase.auth.rest;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.core.util.ObjectsCompat;

import com.anotherdev.firebase.auth.FirebaseAuth;
import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.SignInResponse;
import com.anotherdev.firebase.auth.data.Data;
import com.anotherdev.firebase.auth.data.model.FirebaseUserImpl;
import com.anotherdev.firebase.auth.data.model.UserProfile;
import com.anotherdev.firebase.auth.internal.CustomFirebaseExceptions;
import com.anotherdev.firebase.auth.provider.EmailAuthCredential;
import com.anotherdev.firebase.auth.provider.EmailAuthProvider;
import com.anotherdev.firebase.auth.provider.IdpAuthCredential;
import com.anotherdev.firebase.auth.provider.Provider;
import com.anotherdev.firebase.auth.rest.api.RestAuthApi;
import com.anotherdev.firebase.auth.rest.api.model.ExchangeTokenRequest;
import com.anotherdev.firebase.auth.rest.api.model.ExchangeTokenResponse;
import com.anotherdev.firebase.auth.rest.api.model.GetAccountInfoRequest;
import com.anotherdev.firebase.auth.rest.api.model.GetAccountInfoResponse;
import com.anotherdev.firebase.auth.rest.api.model.ImmutableSignInWithEmailPasswordRequest;
import com.anotherdev.firebase.auth.rest.api.model.ImmutableSignInWithIdpRequest;
import com.anotherdev.firebase.auth.rest.api.model.SendPasswordResetEmailRequest;
import com.anotherdev.firebase.auth.rest.api.model.SendPasswordResetEmailResponse;
import com.anotherdev.firebase.auth.rest.api.model.SignInAnonymouslyRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInWithCustomTokenRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInWithEmailPasswordRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInWithIdpRequest;
import com.anotherdev.firebase.auth.rest.api.model.UnlinkProviderRequest;
import com.anotherdev.firebase.auth.util.IdTokenParser;
import com.anotherdev.firebase.auth.util.RxUtil;
import com.anotherdev.firebase.auth.util.Strings;
import com.f2prateek.rx.preferences2.Preference;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.internal.IdTokenListener;
import com.google.firebase.auth.internal.InternalAuthProvider;
import com.google.firebase.internal.InternalTokenResult;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.internal.functions.Functions;
import timber.log.Timber;

public class RestAuthProvider implements FirebaseAuth, InternalAuthProvider {

    private static final FirebaseUserImpl SIGNED_OUT = FirebaseUserImpl.from(null, null, null);

    private final FirebaseApp app;
    private final Preference<FirebaseUserImpl> userStore;

    private final List<IdTokenListener> listeners = new ArrayList<>();


    public RestAuthProvider(FirebaseApp app) {
        this.app = app;
        userStore = Data.from(app.getApplicationContext()).getCurrentUser(SIGNED_OUT);
        initIdTokenBroadcast();
    }

    @SuppressLint("CheckResult")
    private void initIdTokenBroadcast() {
        //noinspection ResultOfMethodCallIgnored
        userStore.asObservable()
                .skip(1)
                .subscribe(user -> {
                    for (IdTokenListener listener : listeners) {
                        listener.onIdTokenChanged(new InternalTokenResult(user.getIdToken()));
                    }
                }, RxUtil.ON_ERROR_LOG_V2);
    }

    @NonNull
    @Override
    public FirebaseApp getApp() {
        return app;
    }

    @Nullable
    @Override
    public FirebaseUserImpl getCurrentUser() {
        return userStore.isSet() ? userStore.get() : null;
    }

    @NonNull
    @Override
    public Observable<FirebaseUser> currentUser() {
        return RxJavaBridge.toV3Observable(userStore.asObservable().map(user -> user));
    }

    @Override
    public boolean isSignedIn() {
        return userStore.isSet();
    }

    @Override
    public boolean isSignedInWith(Provider provider) {
        return isSignedIn() && userStore.get().isSignedInWith(provider);
    }

    @NonNull
    @Override
    public Observable<FirebaseAuth> authStateChanges() {
        return RxJavaBridge.toV3Observable(userStore.asObservable())
                .map(firebaseUser -> RestAuthProvider.this);
    }

    @NonNull
    @Override
    public Single<SignInResponse> signInAnonymously() {
        return RestAuthApi.auth()
                .signInAnonymously(SignInAnonymouslyRequest.builder().build())
                .map(this::saveCurrentUser)
                .map(this::getAccountInfo);
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
                .map(this::saveCurrentUser)
                .map(this::getAccountInfo);
    }

    @NonNull
    @Override
    public Single<SignInResponse> signInWithEmailAndPassword(@NonNull String email, @NonNull String password) {
        EmailAuthCredential credential = EmailAuthProvider.getCredential(email, password);
        return signInWithEmailAndPassword(credential);
    }

    @NonNull
    @Override
    public Single<SignInResponse> signInWithEmailAndPassword(@NonNull EmailAuthCredential credential) {
        ImmutableSignInWithEmailPasswordRequest.Builder builder = SignInWithEmailPasswordRequest.builder();
        return performSignInWithEmailAndPassword(builder, credential);
    }

    @NonNull
    @Override
    public Single<SignInResponse> signInWithCredential(@NonNull IdpAuthCredential credential) {
        ImmutableSignInWithIdpRequest.Builder builder = SignInWithIdpRequest.builder();
        return performSignInWithCredential(builder, credential);
    }

    @NonNull
    @Override
    public Single<SignInResponse> signInWithCustomToken(@NonNull String customToken) {
        SignInWithCustomTokenRequest request = SignInWithCustomTokenRequest.builder()
                .customToken(customToken)
                .build();
        return RestAuthApi.auth()
                .signInWithCustomToken(request)
                .map(this::saveCurrentUser)
                .map(this::getAccountInfo);
    }

    @NonNull
    @Override
    public Single<SignInResponse> linkWithCredential(@NonNull FirebaseUser user, @NonNull EmailAuthCredential credential) {
        ImmutableSignInWithEmailPasswordRequest.Builder builder = SignInWithEmailPasswordRequest.builder()
                .idToken(user.getIdToken());
        return performSignInWithEmailAndPassword(builder, credential);
    }

    private Single<SignInResponse> performSignInWithEmailAndPassword(@NonNull ImmutableSignInWithEmailPasswordRequest.Builder builder,
                                                                     @NonNull EmailAuthCredential credential) {
        SignInWithEmailPasswordRequest request = builder
                .email(credential.getEmail())
                .password(credential.getPassword())
                .build();
        return RestAuthApi.auth()
                .signInWithEmailAndPassword(request)
                .map(this::saveCurrentUser)
                .map(this::getAccountInfo);
    }

    @NonNull
    @Override
    public Single<SignInResponse> linkWithCredential(@NonNull FirebaseUser user, @NonNull IdpAuthCredential credential) {
        ImmutableSignInWithIdpRequest.Builder builder = SignInWithIdpRequest.builder()
                .idToken(user.getIdToken());
        return performSignInWithCredential(builder, credential);
    }

    private Single<SignInResponse> performSignInWithCredential(@NonNull ImmutableSignInWithIdpRequest.Builder builder,
                                                               @NonNull IdpAuthCredential credential ) {
        SignInWithIdpRequest request = builder
                .requestUri(credential.getRequestUri(this))
                .postBody(credential.getPostBody())
                .build();
        return RestAuthApi.auth()
                .signInWithCredential(request)
                .map(this::saveCurrentUser)
                .map(this::getAccountInfo)
                .onErrorResumeNext(e -> {
                    final String originalMessage = Strings.nullToEmpty(e.getMessage());
                    if (e instanceof IllegalStateException
                            && originalMessage.contains("required attributes are not set")
                            && originalMessage.contains("refreshToken")) {
                        Timber.i(e, "Firebase return 200 but without refresh token");
                        String baseErrorMessage = String.format("Sign in with %s failed.", credential.getProvider());
                        StringBuilder error = new StringBuilder(baseErrorMessage);
                        if (!isSignedIn()) {
                            error.append(" Maybe the account is not properly linked.");
                        }
                        return Single.error(CustomFirebaseExceptions.createCustom(400, error.toString()));
                    } else {
                        return Single.error(e);
                    }
                });
    }

    @NonNull
    @Override
    public Completable unlink(@NonNull FirebaseUser user, @NonNull String provider) {
        UnlinkProviderRequest request = UnlinkProviderRequest.builder()
                .idToken(user.getIdToken())
                .addProviders(provider)
                .build();
        return RestAuthApi.auth()
                .unlink(request)
                .flatMapCompletable(jsonObject -> Completable.complete());
    }

    @NonNull
    @Override
    public Single<SendPasswordResetEmailResponse> sendPasswordResetEmail(SendPasswordResetEmailRequest request) {
        return RestAuthApi.auth().sendPasswordResetEmail(request);
    }

    @Override
    public void signOut() {
        userStore.delete();
    }

    private GetTokenResult getTokenResultLocal(@NonNull FirebaseUserImpl user) {
        String idToken = user.getIdToken();
        Map<String, Object> map = IdTokenParser.parseIdTokenToMap(idToken);
        return new GetTokenResult(idToken, map);
    }

    @NonNull
    @Override
    public Task<GetTokenResult> getAccessToken(boolean forceRefresh) {
        Timber.d("getAccessToken(%s)", forceRefresh);
        TaskCompletionSource<GetTokenResult> source = new TaskCompletionSource<>();
        FirebaseUserImpl user = getCurrentUser();
        if (user != null) {
            final boolean needRefresh = forceRefresh || user.isExpired();
            final String currentRefreshToken = user.getRefreshToken();
            if (!needRefresh) {
                source.trySetResult(getTokenResultLocal(user));
            } else if (TextUtils.isEmpty(currentRefreshToken)) {
                source.trySetException(new FirebaseException("Current refresh token is null. Log out"));
                signOut();
            } else {
                //noinspection ResultOfMethodCallIgnored
                exchangeToken(user)
                        .doOnSuccess(response -> source.trySetResult(getTokenResultLocal(user)))
                        .doOnError(e -> source.trySetException(new Exception(e)))
                        .subscribe(Functions.emptyConsumer(), RxUtil.ON_ERROR_LOG_V3);
            }
        } else {
            source.trySetException(new FirebaseNoSignedInUserException("Please sign in before trying to get a token."));
        }
        return source.getTask();
    }

    public Single<ExchangeTokenResponse> exchangeToken() {
        return exchangeToken(userStore.get());
    }

    public Single<ExchangeTokenResponse> exchangeToken(@NonNull FirebaseUserImpl user) {
        return Single.fromCallable(user::getRefreshToken)
                .map(token -> ExchangeTokenRequest.builder().refreshToken(token).build())
                .flatMap(request -> RestAuthApi.token().exchangeToken(request))
                .doOnError(e -> Timber.tag("http").e(e))
                .map(response -> {
                    saveCurrentUser(response.getIdToken(), response.getRefreshToken());
                    return response;
                });
    }

    @Nullable
    @Override
    public String getUid() {
        return userStore.get().getUid();
    }

    @Override
    public void addIdTokenListener(@NonNull IdTokenListener idTokenListener) {
        listeners.add(idTokenListener);
    }

    @Override
    public void removeIdTokenListener(@NonNull IdTokenListener idTokenListener) {
        listeners.remove(idTokenListener);
    }

    public SignInResponse saveCurrentUser(SignInResponse response) throws FirebaseException {
        String idToken = response.getIdToken();
        String refreshToken = response.getRefreshToken();
        saveCurrentUser(idToken, refreshToken);
        return response;
    }

    private void saveCurrentUser(String idToken, String refreshToken) throws FirebaseException {
        if (TextUtils.isEmpty(idToken) || TextUtils.isEmpty(refreshToken)) {
            throw new FirebaseException(String.format("Input null. idToken: %s refreshToken: %s", idToken, refreshToken));
        }

        FirebaseUserImpl user = FirebaseUserImpl.from(app.getName(), idToken, refreshToken);
        userStore.set(user);
    }

    @WorkerThread
    private SignInResponse getAccountInfo(SignInResponse signInResponse) {
        getAccountInfo(signInResponse.getIdToken());
        return signInResponse;
    }

    public void getAccountInfo() {
        getAccountInfo(userStore.get().getIdToken());
    }

    @WorkerThread
    public void getAccountInfo(@NonNull String idToken) {
        GetAccountInfoRequest request = GetAccountInfoRequest.builder()
                .idToken(idToken)
                .build();
        GetAccountInfoResponse accountInfo = RestAuthApi.auth()
                .getAccounts(request)
                .blockingGet();

        final String currentUserId = userStore.get().getUid();

        Data dataStore = Data.from(app.getApplicationContext());
        for (UserProfile profile : accountInfo.getUsers()) {
            final String uid = profile.getLocalId();
            dataStore.getUserProfile(uid).set(profile);

            if (ObjectsCompat.equals(uid, currentUserId)) {
                try {
                    FirebaseUserImpl user = userStore.get();
                    user.getUserInfo().addProperty("far_updatedAt", System.currentTimeMillis());
                    userStore.set(user);
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        }
    }
}
