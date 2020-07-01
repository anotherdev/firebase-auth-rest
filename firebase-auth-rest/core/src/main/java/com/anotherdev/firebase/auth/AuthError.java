package com.anotherdev.firebase.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.util.FarGson;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import retrofit2.HttpException;
import retrofit2.Response;
import timber.log.Timber;

@SuppressWarnings("WeakerAccess")
public class AuthError {

    public static final AuthError UNKNOWN = new AuthError();

    private static final String TAG = AuthError.class.getName();

    private static final int ERROR_CLIENT = 400;


    int code = 418;
    String message = "Unknown Error";
    @Nullable
    Throwable cause;


    AuthError() {
    }

    AuthError(int code, @Nullable Throwable cause) {
        this.code = code;
        if (cause != null) {
            this.cause = cause.getCause();
            this.message = cause.getMessage();
        }
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Nullable
    public Throwable getCause() {
        return cause;
    }

    void setCause(@Nullable Throwable cause) {
        this.cause = cause;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("code: %s, message: %s, cause: %s", code, message, cause);
    }

    @NonNull
    public static AuthError fromThrowable(@Nullable Throwable t) {
        Gson gson = FarGson.get();
        Throwable cause = t;
        while (cause != null) {
            Timber.tag(TAG).e("error: %s message: %s", cause.getClass().getName(), cause.getMessage());
            if (isClientSideNetworkError(t)) {
                return UNKNOWN;
            }

            String msg = cause.getMessage();
            try {
                if (cause instanceof HttpException) {
                    HttpException retrofitError = (HttpException) cause;
                    Response<?> response = retrofitError.response();
                    if (response != null && response.errorBody() != null) {
                        msg = response.errorBody().string();
                        JsonObject json = gson.fromJson(msg, JsonObject.class);
                        msg = json.get("error").toString();
                        Timber.tag(TAG).e("OkHttp ResponseBody: %s", msg);
                    }
                }

                AuthError authError = gson.fromJson(msg, AuthError.class);
                if (authError != null) {
                    authError.setCause(cause);
                    return authError;
                }
            } catch (JsonSyntaxException ignored) {
                // Ignore this exception and continue to find the root cause
                // that has valid error json returned from server.
            } catch (IOException ignored) {
            }
            cause = cause.getCause();
        }
        return new AuthError(ERROR_CLIENT, t);
    }

    private static boolean isClientSideNetworkError(@NonNull Throwable t) {
        return false;
    }
}
