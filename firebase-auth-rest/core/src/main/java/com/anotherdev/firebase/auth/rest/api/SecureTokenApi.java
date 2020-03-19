package com.anotherdev.firebase.auth.rest.api;

public interface SecureTokenApi {

    // Workaround colon issue. https://github.com/square/retrofit/issues/3080
    String BASE_URL = "https://securetoken.googleapis.com/";
}
