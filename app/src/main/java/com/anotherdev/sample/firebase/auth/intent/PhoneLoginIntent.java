package com.anotherdev.sample.firebase.auth.intent;

import android.content.Context;

import com.anotherdev.sample.firebase.auth.PhoneLoginActivity;

public class PhoneLoginIntent extends BaseIntent {

    public PhoneLoginIntent(Context context) {
        super(context, PhoneLoginActivity.class);
    }
}
