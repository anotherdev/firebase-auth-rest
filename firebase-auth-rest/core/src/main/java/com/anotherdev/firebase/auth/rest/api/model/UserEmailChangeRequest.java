package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface UserEmailChangeRequest extends SignInRequest, IdTokenRequest {

    @SerializedName("email")
    String getEmail();


    static ImmutableUserEmailChangeRequest.Builder builder() {
        return ImmutableUserEmailChangeRequest.builder();
    }
}
