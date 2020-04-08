package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface SignInWithEmailPasswordRequest extends SignInRequest {

    @SerializedName("email")
    String getEmail();

    @SerializedName("password")
    String getPassword();


    static ImmutableSignInWithEmailPasswordRequest.Builder builder() {
        return ImmutableSignInWithEmailPasswordRequest.builder();
    }
}
