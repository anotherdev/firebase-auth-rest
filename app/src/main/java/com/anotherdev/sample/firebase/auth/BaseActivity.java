package com.anotherdev.sample.firebase.auth;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.common.FirebaseAuth;
import com.anotherdev.sample.firebase.auth.intent.LoginIntent;
import com.github.florent37.inlineactivityresult.rx.RxInlineActivityResult;
import com.google.firebase.FirebaseApp;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.internal.functions.Functions;

public abstract class BaseActivity extends AppCompatActivity {

    private final CompositeDisposable onDestroyV2 = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    protected void requestAuthIfNeeded() {
        FirebaseApp app = FirebaseApp.getInstance();
        FirebaseAuth auth = FirebaseAuthRest.getInstance(app);
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            onDestroyV2.add(new RxInlineActivityResult(this)
                    .request(new LoginIntent(this))
                    .subscribe(Functions.emptyConsumer(), Rx2Util.ON_ERROR_LOG));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroyV2.clear();
    }
}
