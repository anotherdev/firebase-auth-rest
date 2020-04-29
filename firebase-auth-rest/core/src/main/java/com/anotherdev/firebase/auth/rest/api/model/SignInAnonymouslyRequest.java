package com.anotherdev.firebase.auth.rest.api.model;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface SignInAnonymouslyRequest extends SignInRequest {

    static ImmutableSignInAnonymouslyRequest.Builder builder() {
        return ImmutableSignInAnonymouslyRequest.builder();
    }
}
