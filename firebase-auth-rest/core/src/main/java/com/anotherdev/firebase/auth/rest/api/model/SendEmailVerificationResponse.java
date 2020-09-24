package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface SendEmailVerificationResponse {

    @SerializedName("email")
    String getEmail();


    static ImmutableSendEmailVerificationResponse.Builder builder() {
        return ImmutableSendEmailVerificationResponse.builder();
    }
}
