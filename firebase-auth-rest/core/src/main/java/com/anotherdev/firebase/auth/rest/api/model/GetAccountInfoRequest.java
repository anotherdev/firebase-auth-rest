package com.anotherdev.firebase.auth.rest.api.model;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface GetAccountInfoRequest extends IdTokenRequest {

    static ImmutableGetAccountInfoRequest.Builder builder() {
        return ImmutableGetAccountInfoRequest.builder();
    }
}
