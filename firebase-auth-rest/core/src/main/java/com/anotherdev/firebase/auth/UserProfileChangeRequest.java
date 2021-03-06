package com.anotherdev.firebase.auth;

import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.rest.api.model.OptionalIdTokenRequest;
import com.anotherdev.firebase.auth.rest.api.model.SignInRequest;
import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface UserProfileChangeRequest extends SignInRequest, OptionalIdTokenRequest {

    enum Attribute {
        DISPLAY_NAME,
        PHOTO_URL
    }

    @Nullable
    @SerializedName("displayName")
    String getDisplayName();

    @Nullable
    @SerializedName("photoUrl")
    String getPhotoUrl();

    @Nullable
    @SerializedName("deleteAttribute")
    List<Attribute> getDeleteAttribute();


    static ImmutableUserProfileChangeRequest.Builder builder() {
        return ImmutableUserProfileChangeRequest.builder();
    }
}
