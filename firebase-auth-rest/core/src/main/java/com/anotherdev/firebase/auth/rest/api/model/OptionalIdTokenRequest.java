package com.anotherdev.firebase.auth.rest.api.model;

import androidx.annotation.Nullable;

public interface OptionalIdTokenRequest extends IdTokenRequest {

    @Nullable
    @Override
    String getIdToken();
}
