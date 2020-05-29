package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface SendPasswordResetEmailResponse {

    @SerializedName("email")
    String getEmail();


    static ImmutableSendPasswordResetEmailResponse.Builder builder() {
        return ImmutableSendPasswordResetEmailResponse.builder();
    }
}
