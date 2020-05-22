package com.anotherdev.sample.firebase.auth;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import butterknife.BindView;

public class JsPhoneLoginActivity extends BaseActivity {

    @BindView(R.id.webview) WebView webView;

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_js_phone_login;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("JavaScript Phone Login");

        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);

        webView.loadUrl("file:///android_asset/phone_login.html");
    }


    private final WebViewClient webViewClient = new WebViewClient() {
    };


    private final WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public boolean onConsoleMessage(ConsoleMessage msg) {
            final String className = getClass().getName();
            final int line = msg.lineNumber();
            final ConsoleMessage.MessageLevel level = msg.messageLevel();
            final String message = msg.message();
            final String sourceId = msg.sourceId();
            String error = String.format("class: %s level: %s line: %s msg: %s sourceId: %s", className, level, line, message, sourceId);
            Log.i(TAG, error);
            return false;
        }
    };
}
