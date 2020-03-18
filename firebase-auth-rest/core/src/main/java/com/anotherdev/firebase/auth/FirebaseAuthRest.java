package com.anotherdev.firebase.auth;

import com.anotherdev.firebase.auth.common.FirebaseAuth;
import com.anotherdev.firebase.auth.rest.RestAuthProvider;
import com.google.firebase.FirebaseApp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FirebaseAuthRest {

    private static final Map<String, FirebaseAuth> APP_AUTH_PROVIDER_MAP = new ConcurrentHashMap<>();


    public static FirebaseAuth getInstance(FirebaseApp app) {
        return new RestAuthProvider(app);
    }
}
