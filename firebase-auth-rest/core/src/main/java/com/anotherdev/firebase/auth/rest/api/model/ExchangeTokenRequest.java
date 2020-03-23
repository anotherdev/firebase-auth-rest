package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface ExchangeTokenRequest {

    @SerializedName("grant_type")
    default String getGrantType() {
        return "refresh_token";
    }

    @SerializedName("refresh_token")
    String getRefreshToken();


    static ImmutableExchangeTokenRequest.Builder builder() {
        return ImmutableExchangeTokenRequest.builder();
    }
}
