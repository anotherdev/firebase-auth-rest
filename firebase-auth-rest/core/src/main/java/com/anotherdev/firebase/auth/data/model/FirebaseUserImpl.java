package com.anotherdev.firebase.auth.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.common.FirebaseAuth;
import com.anotherdev.firebase.auth.provider.AuthCredential;
import com.anotherdev.firebase.auth.provider.IdpAuthCredential;
import com.anotherdev.firebase.auth.rest.api.model.SignInResponse;
import com.anotherdev.firebase.auth.util.IdTokenParser;
import com.google.firebase.FirebaseApp;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.reactivex.rxjava3.core.Single;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class FirebaseUserImpl implements FirebaseUser {

    @NonNull
    String appName;
    @Nullable
    String idToken;
    @Nullable
    String refreshToken;
    @NonNull
    JsonObject userInfo;


    FirebaseUserImpl(@NonNull String appName, @Nullable String idToken, @Nullable String refreshToken) {
        this.appName = appName;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        userInfo = IdTokenParser.parseIdToken(idToken);
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

    @Nullable
    private String getAsString(String key) {
        try {
            JsonElement element = userInfo.get(key);
            return element != null ? element.getAsString() : null;
        } catch (RuntimeException e) {
            Timber.e(e);
            return null;
        }
    }

    @Nullable
    @Override
    public String getUid() {
        return getAsString("user_id");
    }

    @Nullable
    @Override
    public String getDisplayName() {
        return null;
    }

    @Nullable
    @Override
    public String getEmail() {
        return getAsString("email");
    }

    @NonNull
    @Override
    public Single<SignInResponse> linkWithCredential(AuthCredential credential) {
        FirebaseApp app = FirebaseApp.getInstance(appName);
        FirebaseAuth auth = FirebaseAuthRest.getInstance(app);

        if (credential instanceof IdpAuthCredential) {
            IdpAuthCredential idp = (IdpAuthCredential) credential;
            return auth.linkWithCredential(this, idp);
        } else {
            String credentialClassName = credential.getClass().getSimpleName();
            String error = String.format("AuthCredential: %s not supported yet.", credentialClassName);
            return Single.error(new UnsupportedOperationException(error));
        }
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


    public static FirebaseUserImpl from(@NonNull String appName,
                                        @Nullable String idToken,
                                        @Nullable String refreshToken) {
        return new FirebaseUserImpl(appName, idToken, refreshToken);
    }
}
