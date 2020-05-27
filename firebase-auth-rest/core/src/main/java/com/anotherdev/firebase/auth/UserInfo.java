package com.anotherdev.firebase.auth;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(
        strictBuilder = true,
        visibility = Value.Style.ImplementationVisibility.PACKAGE
)
@Gson.TypeAdapters
public interface UserInfo {

    @SerializedName("displayName")
    String getDisplayName();

    @SerializedName("email")
    String getEmail();

    @SerializedName("photoUrl")
    String getPhotoUrl();

    @SerializedName("providerId")
    String getProviderId();

    @SerializedName("rawId")
    String getUid();
}
