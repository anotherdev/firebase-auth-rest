package com.anotherdev.firebase.auth;

import com.anotherdev.firebase.auth.rest.RestAuthProvider;
import com.anotherdev.firebase.auth.rest.RestAuthTokenRefresher;
import com.google.firebase.FirebaseApp;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class FirebaseAuthRest {

    private static final ConcurrentHashMap<String, RestAuthTokenRefresher> TOKEN_REFRESHER_MAP = new ConcurrentHashMap<>();
    private static final Collection<RestAuthTokenRefresher> TOKEN_REFRESHER_VIEW =
            Collections.unmodifiableCollection(TOKEN_REFRESHER_MAP.values());


    public static FirebaseAuth getInstance(FirebaseApp app) {
        final String name = app.getName();
        RestAuthTokenRefresher tokenRefresher = TOKEN_REFRESHER_MAP.get(name);
        if (tokenRefresher == null) {
            RestAuthProvider newAuth = new RestAuthProvider(app);
            RestAuthTokenRefresher newTokenRefresher = new RestAuthTokenRefresher(newAuth);
            RestAuthTokenRefresher current = TOKEN_REFRESHER_MAP.putIfAbsent(name, newTokenRefresher);
            return current != null ? current.getAuth() : newTokenRefresher.getAuth();
        } else {
            return tokenRefresher.getAuth();
        }
    }

    public static Collection<RestAuthTokenRefresher> getTokenRefreshers() {
        return TOKEN_REFRESHER_VIEW;
    }
}
