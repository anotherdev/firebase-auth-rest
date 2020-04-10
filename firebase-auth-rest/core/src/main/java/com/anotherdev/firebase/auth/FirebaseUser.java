package com.anotherdev.firebase.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.util.IdTokenParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@SuppressWarnings("WeakerAccess")
public class FirebaseUser {

    @NonNull
    String appName;
    @Nullable
    String idToken;
    @Nullable
    String refreshToken;
    @NonNull
    JsonObject userInfo;


    FirebaseUser(@NonNull String appName, @Nullable String idToken, @Nullable String refreshToken) {
        this.appName = appName;
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        userInfo = IdTokenParser.parseIdToken(idToken);
    }

    @Nullable
    public String getIdToken() {
        return idToken;
    }

    @Nullable
    public String getRefreshToken() {
        return refreshToken;
    }

    @Nullable
    public String getUid() {
        JsonElement uid = userInfo.get("user_id");
        return uid != null ? uid.getAsString() : null;
    }

    public long getExpirationTime() {
        JsonElement exp = userInfo.get("exp");
        return exp != null ? exp.getAsLong() : 0;
    }

    public boolean isExpired() {
        return expiresInSeconds() <= 0;
    }

    long expiresInSeconds() {
        return getExpirationTime() - (System.currentTimeMillis() / 1000);
    }


    public static FirebaseUser from(@NonNull String appName,
                                    @Nullable String idToken,
                                    @Nullable String refreshToken) {
        return new FirebaseUser(appName, idToken, refreshToken);
    }
}
