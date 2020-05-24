package com.anotherdev.sample.firebase.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.webkit.WebViewCompat;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.firebase.auth.SignInResponse;
import com.anotherdev.firebase.auth.rest.RestAuthProvider;
import com.anotherdev.firebase.auth.util.FarGson;
import com.anotherdev.firebase.auth.util.RxUtil;
import com.google.firebase.FirebaseApp;
import com.google.gson.JsonObject;

import io.reactivex.rxjava3.annotations.CheckReturnValue;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.internal.functions.Functions;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

public class FirebaseAuthJsSdk {

    private static final String TAG = FirebaseAuthJsSdk.class.getName();

    private final WebView webView;

    private final FirebaseAuthJs auth = new FirebaseAuthJs();


    public FirebaseAuthJsSdk(@NonNull Context context) {
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

        webView.addJavascriptInterface(auth, FirebaseAuthJs.NAME);

        webView.loadUrl("file:///android_asset/phone_login.html");

        PackageInfo wv = WebViewCompat.getCurrentWebViewPackage(context);
        String info = wv != null ? String.format("WebView: %s, versionName: %s", wv.packageName, wv.versionName) : "Unknown";
        Log.i(TAG, info);

        auth.authStateChanges()
                .doOnNext(response -> {
                    RestAuthProvider auth = (RestAuthProvider) FirebaseAuthRest.getInstance(FirebaseApp.getInstance());
                    auth.saveCurrentUser(response);
                })
                .subscribe(Functions.emptyConsumer(), RxUtil.ON_ERROR_LOG_V3);
    }

    public WebView getWebView() {
        return webView;
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


    private static class FirebaseAuthJs {

        static final String NAME = "FirebaseAuthJs";

        final BehaviorSubject<String> signInJson = BehaviorSubject.create();
        final Observable<SignInResponse> authStateChanges = signInJson.map(data -> {
            JsonObject json = FarGson.get().fromJson(data, JsonObject.class);
            JsonObject tokens = json.getAsJsonObject("stsTokenManager");
            return SignInResponse.builder()
                    .idToken(tokens.get("accessToken").getAsString())
                    .email(json.get("email").getAsString())
                    .refreshToken(tokens.get("refreshToken").getAsString())
                    .expiresIn(tokens.get("expirationTime").getAsString())
                    .localId(json.get("uid").getAsString())
                    .build();
        });

        @CheckReturnValue
        Observable<SignInResponse> authStateChanges() {
            return authStateChanges.hide();
        }

        @JavascriptInterface
        public void onAuthStateChanged(String data) {
            Log.d(TAG, "onAuthStateChanged: " + data);
            signInJson.onNext(data);
        }
    }
}
