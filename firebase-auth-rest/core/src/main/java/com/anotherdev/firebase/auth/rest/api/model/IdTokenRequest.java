package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface IdTokenRequest {

    @SerializedName("idToken")
    String getIdToken();


    static ImmutableIdTokenRequest.Builder builder() {
        return ImmutableIdTokenRequest.builder();
    }
}
