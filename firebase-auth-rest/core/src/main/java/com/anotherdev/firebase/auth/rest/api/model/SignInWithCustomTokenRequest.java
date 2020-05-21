package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface SignInWithCustomTokenRequest extends SignInRequest {

    @SerializedName("token")
    String getCustomToken();


    static ImmutableSignInWithCustomTokenRequest.Builder builder() {
        return ImmutableSignInWithCustomTokenRequest.builder();
    }
}
