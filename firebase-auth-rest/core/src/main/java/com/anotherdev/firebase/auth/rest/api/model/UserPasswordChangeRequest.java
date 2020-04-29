package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface UserPasswordChangeRequest extends SignInRequest, IdTokenRequest {

    @SerializedName("password")
    String getPassword();


    static ImmutableUserPasswordChangeRequest.Builder builder() {
        return ImmutableUserPasswordChangeRequest.builder();
    }
}
