package com.anotherdev.firebase.auth.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.util.Base64Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.functions.Function;
import timber.log.Timber;

public class IdTokenParser {

    private static final Gson GSON = FarGson.get();


    @NonNull
    public static JsonObject parseIdToken(@Nullable String idToken) {
        return parse(idToken, new JsonObject(), decodedToken -> GSON.fromJson(decodedToken, JsonObject.class));
    }

    public static Map<String, Object> parseIdTokenToMap(@Nullable String idToken) {
        return parse(idToken, Collections.emptyMap(), decodedToken -> {
            if (TextUtils.isEmpty(decodedToken)) {
                return null;
            }
            return toMap(new JSONObject(decodedToken));
        });
    }

    private static Map<String, Object> toMap(JSONObject json) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = json.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = json.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof  JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof  JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    private static <T> T parse(@Nullable String idToken, T defaultValue, Function<String,T> creator) {
        if (TextUtils.isEmpty(idToken)) {
            return defaultValue;
        }

        String[] parts = idToken.split("\\.");
        if (parts.length < 2) {
            return defaultValue;
        }

        T result = null;
        try {
            String encodedToken = parts[1];
            String decodedToken = new String(
                    Base64Utils.decodeUrlSafeNoPadding(encodedToken),
                    Charset.defaultCharset()
            );
            result = creator.apply(decodedToken);
        } catch (Throwable e) {
            Timber.e(e);
            return defaultValue;
        }
        return result != null ? result : defaultValue;
    }


    private IdTokenParser() { /* Util */ }
}
