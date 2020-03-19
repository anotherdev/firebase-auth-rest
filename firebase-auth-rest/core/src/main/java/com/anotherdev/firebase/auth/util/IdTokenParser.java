package com.anotherdev.firebase.auth.util;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.android.gms.common.util.Base64Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.nio.charset.Charset;

public class IdTokenParser {

    private static final Gson GSON = FarGson.get();


    public static JsonObject parseIdToken(@Nullable String idToken) {
        if (TextUtils.isEmpty(idToken)) {
            return new JsonObject();
        }

        String[] parts = idToken.split("\\.");
        if (parts.length < 2) {
            return new JsonObject();
        }

        String encodedToken = parts[1];
        String decodedToken = new String(
                Base64Utils.decodeUrlSafeNoPadding(encodedToken),
                Charset.defaultCharset()
        );
        return GSON.fromJson(decodedToken, JsonObject.class);
    }

    private IdTokenParser() { /* Util */ }
}
