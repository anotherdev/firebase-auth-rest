package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface SignInRequest {

    @Value.Default
    @SerializedName("returnSecureToken")
    default boolean returnSecureToken() {
        return true;
    }


    static ImmutableSignInRequest.Builder builder() {
        return ImmutableSignInRequest.builder();
    }
}
