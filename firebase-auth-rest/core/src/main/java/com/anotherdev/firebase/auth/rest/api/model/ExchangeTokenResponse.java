package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface ExchangeTokenResponse {

    @SerializedName("expires_in")
    String expiresIn();

    @SerializedName("token_type")
    String getTokenType();

    @SerializedName("refresh_token")
    String getRefreshToken();

    @SerializedName("id_token")
    String getIdToken();

    @SerializedName("user_id")
    String getUserId();

    @SerializedName("project_id")
    String getProjectId();


    static ImmutableExchangeTokenRequest.Builder builder() {
        return ImmutableExchangeTokenRequest.builder();
    }
}
