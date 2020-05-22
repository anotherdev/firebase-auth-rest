package com.anotherdev.sample.firebase.auth;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import butterknife.BindView;

public class JsPhoneLoginActivity extends BaseActivity {

    @BindView(R.id.webview) WebView webView;

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_js_phone_login;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView.loadUrl("file:///android_asset/phone_login.html");
    }
}
