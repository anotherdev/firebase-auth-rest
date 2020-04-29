package com.anotherdev.firebase.auth;

import com.anotherdev.firebase.auth.rest.RestAuthProvider;
import com.anotherdev.firebase.auth.rest.RestAuthTokenRefresher;
import com.google.firebase.FirebaseApp;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FirebaseAuthRest {

    private static final Map<String, RestAuthTokenRefresher> TOKEN_REFRESHER_MAP = new ConcurrentHashMap<>();
    private static final Collection<RestAuthTokenRefresher> TOKEN_REFRESHER_VIEW =
            Collections.unmodifiableCollection(TOKEN_REFRESHER_MAP.values());


    public static FirebaseAuth getInstance(FirebaseApp app) {
        final String name = app.getName();
        final RestAuthProvider auth = new RestAuthProvider(app);
        if (!TOKEN_REFRESHER_MAP.containsKey(name)) {
            TOKEN_REFRESHER_MAP.put(name, new RestAuthTokenRefresher(auth));
        }
        return auth;
    }

    public static Collection<RestAuthTokenRefresher> getTokenRefreshers() {
        return TOKEN_REFRESHER_VIEW;
    }
}
