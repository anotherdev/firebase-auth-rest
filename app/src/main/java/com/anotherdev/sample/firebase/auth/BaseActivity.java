package com.anotherdev.sample.firebase.auth;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.common.FirebaseAuth;
import com.anotherdev.sample.firebase.auth.intent.LoginIntent;
import com.github.florent37.inlineactivityresult.rx.RxInlineActivityResult;
import com.google.firebase.FirebaseApp;

import butterknife.ButterKnife;
import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.internal.functions.Functions;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public abstract class BaseActivity extends AppCompatActivity {

    protected final CompositeDisposable onDestroy = new CompositeDisposable();


    @LayoutRes
    protected int getActivityLayoutRes() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        setContentView();
    }

    @CallSuper
    protected void setContentView() {
        if (isLayoutProvided()) {
            setContentView(getActivityLayoutRes());
            ButterKnife.bind(this);
        }
    }

    protected boolean isLayoutProvided() {
        return getActivityLayoutRes() != 0;
    }

    protected void requestAuthIfNeeded() {
        FirebaseApp app = FirebaseApp.getInstance();
        FirebaseAuth auth = FirebaseAuthRest.getInstance(app);
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            onDestroy.add(RxJavaBridge
                    .toV3Disposable(new RxInlineActivityResult(this)
                            .request(new LoginIntent(this))
                            .subscribe(Functions.emptyConsumer(), RxUtil.ON_ERROR_LOG_V2)));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroy.clear();
    }
}
