package com.anotherdev.firebase.auth.internal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.anotherdev.firebase.auth.util.Strings;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class CustomFirebaseExceptions {

    @NonNull
    public static HttpException createCustom(int code, @Nullable String message) {
        JsonObject error = createFirebaseExceptionJson(code, message);
        MediaType mediaType = MediaType.parse("application/json; charset=UTF-8");
        okhttp3.Response rawResponse = new okhttp3.Response.Builder()
                .code(code)
                .message(Strings.nullToEmpty(message))
                .protocol(Protocol.QUIC)
                .request(new Request.Builder().url("http://localhost/").build())
                .build();
        ResponseBody errorBody = ResponseBody.create(error.toString(), mediaType);
        Response<?> errorResponse = Response.error(errorBody, rawResponse);
        return new HttpException(errorResponse);
    }

    @NonNull
    private static JsonObject createFirebaseExceptionJson(int code, @Nullable String message) {
        JsonObject error = new JsonObject();
        error.addProperty("code", code);
        error.addProperty("message", message);
        JsonObject json = new JsonObject();
        json.add("error", error);
        return json;
    }
}
