package com.anotherdev.firebase.auth.util;

import com.google.android.gms.common.util.Base64Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.nio.charset.Charset;

public class IdTokenParser {

    private static final Gson GSON = FarGson.get();


    public static JsonObject parseIdToken(String idToken) {
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
