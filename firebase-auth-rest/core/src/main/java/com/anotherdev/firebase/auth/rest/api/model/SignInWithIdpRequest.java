package com.anotherdev.firebase.auth.rest.api.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface SignInWithIdpRequest extends SignInRequest, IdTokenRequest {

    @Nullable
    @Override
    String getIdToken();

    @SerializedName("requestUri")
    String getRequestUri();

    @SerializedName("postBody")
    String getPostBody();

    @Value.Default
    @SerializedName("returnIdpCredential")
    default boolean returnIdpCredential() {
        return false;
    }


    static ImmutableSignInWithIdpRequest.Builder builder() {
        return ImmutableSignInWithIdpRequest.builder();
    }
}
