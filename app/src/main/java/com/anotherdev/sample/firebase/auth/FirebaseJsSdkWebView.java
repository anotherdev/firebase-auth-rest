package com.anotherdev.sample.firebase.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.webkit.WebViewCompat;

public class FirebaseJsSdkWebView {

    private static final String TAG = FirebaseJsSdkWebView.class.getName();

    private final WebView webView;


    public FirebaseJsSdkWebView(@NonNull Context context) {
        webView = new WebView(context);
        init(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(@NonNull Context context) {
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        CookieManager.getInstance().setAcceptCookie(true);

        webView.loadUrl("file:///android_asset/phone_login.html");

        PackageInfo wv = WebViewCompat.getCurrentWebViewPackage(context);
        String info = wv != null ? String.format("WebView: %s, versionName: %s", wv.packageName, wv.versionName) : "Unknown";
        Log.i(TAG, info);
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
