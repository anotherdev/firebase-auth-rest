package com.anotherdev.firebase.auth.provider;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.concurrent.TimeUnit;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public interface PhoneAuthOptions {

    @SerializedName("phoneNumber")
    String getPhoneNumber();

    @Value.Default
    @SerializedName("timeout")
    default long getTimeout() {
        return 60;
    }

    @Value.Default
    @SerializedName("timeoutUnit")
    default TimeUnit returnIdpCredential() {
        return TimeUnit.SECONDS;
    }
}
