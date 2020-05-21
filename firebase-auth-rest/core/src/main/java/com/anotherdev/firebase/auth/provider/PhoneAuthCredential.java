package com.anotherdev.firebase.auth.provider;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(strictBuilder = true)
@Gson.TypeAdapters
public abstract class PhoneAuthCredential implements AuthCredential {

    @SerializedName("verificationId")
    public abstract String getVerificationId();

    @SerializedName("smsCode")
    public abstract String getSmsCode();

    @NonNull
    @Override
    public final String getProvider() {
        return "phone";
    }

    @NonNull
    @Override
    public final String getSignInMethod() {
        return "phone";
    }


    public static ImmutablePhoneAuthCredential.Builder builder() {
        return ImmutablePhoneAuthCredential.builder();
    }
}
