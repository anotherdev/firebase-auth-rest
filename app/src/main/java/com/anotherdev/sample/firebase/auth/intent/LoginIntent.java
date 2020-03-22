package com.anotherdev.sample.firebase.auth.intent;

import android.content.Context;

import com.anotherdev.sample.firebase.auth.LoginActivity;

public class LoginIntent extends BaseIntent {

    public LoginIntent(Context context) {
        super(context, LoginActivity.class);
    }
}
