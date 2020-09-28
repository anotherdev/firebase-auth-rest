package com.anotherdev.firebase.auth.rest.api.model;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface UnlinkProviderRequest extends IdTokenRequest {

    @SerializedName("deleteProvider")
    List<String> getProviders();


    static ImmutableUnlinkProviderRequest.Builder builder() {
        return ImmutableUnlinkProviderRequest.builder();
    }
}
