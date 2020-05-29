package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface SendPasswordResetEmailRequest {

    @SerializedName("email")
    String getEmail();

    @Value.Default
    @SerializedName("requestType")
    default String getRequestType() {
        return "PASSWORD_RESET";
    }


    static ImmutableSendPasswordResetEmailRequest.Builder builder() {
        return ImmutableSendPasswordResetEmailRequest.builder();
    }
}
