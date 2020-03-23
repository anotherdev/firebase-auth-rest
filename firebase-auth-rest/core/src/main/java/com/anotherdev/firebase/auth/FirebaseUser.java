package com.anotherdev.firebase.auth;

import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.util.IdTokenParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class FirebaseUser {

    String idToken;
    String refreshToken;
    JsonObject userInfo;


    FirebaseUser(String idToken, String refreshToken) {
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

    boolean isExpired() {
        return expiresInSeconds() <= 0;
    }

    long expiresInSeconds() {
        return getExpirationTime() - (System.currentTimeMillis() / 1000);
    }

    public static FirebaseUser from(String idToken, String refreshToken) {
        return new FirebaseUser(idToken, refreshToken);
    }
}
