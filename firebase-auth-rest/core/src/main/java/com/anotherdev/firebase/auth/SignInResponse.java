package com.anotherdev.firebase.auth;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface SignInResponse {

    @SerializedName("idToken")
    String getIdToken();

    @SerializedName("email")
    String getEmail();

    @SerializedName("refreshToken")
    String getRefreshToken();

    @SerializedName("expiresIn")
    String getExpiresIn();

    @SerializedName("localId")
    String getLocalId();


    static ImmutableSignInResponse.Builder builder() {
        return ImmutableSignInResponse.builder();
    }
}
