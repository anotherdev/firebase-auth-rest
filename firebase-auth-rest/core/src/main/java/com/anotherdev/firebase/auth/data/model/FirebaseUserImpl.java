package com.anotherdev.firebase.auth.data.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.FirebaseAuth;
import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.ImmutableUserProfileChangeRequest;
import com.anotherdev.firebase.auth.SignInResponse;
import com.anotherdev.firebase.auth.UserProfileChangeRequest;
import com.anotherdev.firebase.auth.provider.AuthCredential;
import com.anotherdev.firebase.auth.provider.EmailAuthCredential;
import com.anotherdev.firebase.auth.provider.IdpAuthCredential;
import com.anotherdev.firebase.auth.rest.RestAuthProvider;
import com.anotherdev.firebase.auth.rest.api.RestAuthApi;
import com.anotherdev.firebase.auth.rest.api.model.UserPasswordChangeRequest;
import com.anotherdev.firebase.auth.util.FarGson;
import com.anotherdev.firebase.auth.util.IdTokenParser;
import com.google.firebase.FirebaseApp;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class FirebaseUserImpl implements FirebaseUser {

    @Nullable
    String appName;
    @Nullable
    String idToken;
    @Nullable
    String refreshToken;
    @NonNull
    JsonObject userInfo;

    @SuppressWarnings("NullableProblems")
    @NonNull
    FirebaseAuthData firebaseAuthData;

    @Nullable
    transient FirebaseAuth auth;


    FirebaseUserImpl() {
        userInfo = new JsonObject();
        firebaseAuthData = new FirebaseAuthData();
    }

    FirebaseUserImpl(@Nullable String appName, @Nullable String idToken, @Nullable String refreshToken) {
        this.appName = appName;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.userInfo = IdTokenParser.parseIdToken(idToken);

        try {
            firebaseAuthData = FarGson.get().fromJson(userInfo.get("firebase"), FirebaseAuthData.class);
        } catch (Exception e) {
            Timber.e(e);
        } finally {
            if (firebaseAuthData == null) {
                firebaseAuthData = new FirebaseAuthData();
            }
        }
    }

    @NonNull
    private FirebaseAuth getAuth() {
        if (auth == null) {
            FirebaseApp app = appName != null
                    ? FirebaseApp.getInstance(appName)
                    : FirebaseApp.getInstance();
            auth = FirebaseAuthRest.getInstance(app);
        }
        return auth;
    }

    @NonNull
    private RestAuthProvider getAuthInternal() throws ClassCastException {
        return (RestAuthProvider) getAuth();
    }

    @Override
    public boolean isSignedIn() {
        return !TextUtils.isEmpty(idToken);
    }

    @Nullable
    @Override
    public String getIdToken() {
        return idToken;
    }

    @Nullable
    public String getRefreshToken() {
        return refreshToken;
    }

    @NonNull
    public JsonObject getUserInfo() {
        return userInfo;
    }

    @Nullable
    private static String getAsString(JsonObject json, String key) {
        try {
            JsonElement element = json.get(key);
            return element != null ? element.getAsString() : null;
        } catch (RuntimeException e) {
            Timber.e(e);
            return null;
        }
    }

    @Nullable
    @Override
    public String getUid() {
        return getAsString(userInfo, "user_id");
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return getAsString(userInfo, "name");
    }

    @Nullable
    @Override
    public String getEmail() {
        return getAsString(userInfo, "email");
    }

    @Override
    public boolean isAnonymous() {
        return firebaseAuthData.getIdentities().isEmpty();
    }

    @NonNull
    @Override
    public Single<SignInResponse> linkWithCredential(@NonNull AuthCredential credential) {
        if (credential instanceof IdpAuthCredential) {
            IdpAuthCredential idp = (IdpAuthCredential) credential;
            return getAuth().linkWithCredential(this, idp);
        } else {
            String credentialClassName = credential.getClass().getSimpleName();
            String error = String.format("AuthCredential: %s not supported yet.", credentialClassName);
            return Single.error(new UnsupportedOperationException(error));
        }
    }

    @NonNull
    @Override
    public Single<SignInResponse> reauthenticate(@NonNull AuthCredential credential) {
        FirebaseAuth auth = getAuth();
        if (credential instanceof  IdpAuthCredential) {
            return auth.signInWithCredential((IdpAuthCredential) credential);
        } else if (credential instanceof EmailAuthCredential) {
            EmailAuthCredential eac = (EmailAuthCredential) credential;
            return auth.signInWithEmailAndPassword(eac.getEmail(), eac.getPassword());
        } else {
            String credentialClassName = credential.getClass().getSimpleName();
            String error = String.format("AuthCredential: %s not supported yet.", credentialClassName);
            return Single.error(new UnsupportedOperationException(error));
        }
    }

    @NonNull
    @Override
    public Completable updateProfile(@NonNull UserProfileChangeRequest request) {
        return Single.just(ImmutableUserProfileChangeRequest.copyOf(request))
                .map(req -> req.withIdToken(idToken))
                .flatMap(req -> RestAuthApi.auth().updateProfile(req))
                .doOnSuccess(response -> getAuthInternal().getAccessToken(true))
                .flatMapCompletable(ignored -> Completable.complete());
    }

    @NonNull
    @Override
    public Completable updatePassword(@NonNull String newPassword) {
        return Single.just(newPassword)
                .map(pass -> UserPasswordChangeRequest.builder()
                        .idToken(idToken)
                        .password(pass)
                        .build())
                .flatMap(req -> RestAuthApi.auth().updatePassword(req))
                .doOnSuccess(response -> getAuthInternal().saveCurrentUser(response))
                .flatMapCompletable(ignored -> Completable.complete());
    }

    public long getExpirationTime() {
        JsonElement exp = userInfo.get("exp");
        return exp != null ? exp.getAsLong() : 0;
    }

    public boolean isExpired() {
        return expiresInSeconds() <= 0;
    }

    public long expiresInSeconds() {
        return getExpirationTime() - (System.currentTimeMillis() / 1000);
    }


    public static FirebaseUserImpl from(@Nullable String appName,
                                        @Nullable String idToken,
                                        @Nullable String refreshToken) {
        return new FirebaseUserImpl(appName, idToken, refreshToken);
    }
}
