package com.anotherdev.sample.firebase.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.anotherdev.firebase.auth.AuthError;
import com.anotherdev.firebase.auth.FirebaseAuth;
import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.firebase.auth.FirebaseUser;
import com.anotherdev.firebase.auth.util.RxUtil;
import com.anotherdev.sample.firebase.auth.intent.LoginIntent;
import com.github.florent37.inlineactivityresult.rx.RxInlineActivityResult;
import com.google.android.gms.common.SupportErrorDialogFragment;
import com.google.firebase.FirebaseApp;
import com.yarolegovich.lovelydialog.LovelyInfoDialog;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.akarnokd.rxjava3.bridge.RxJavaBridge;
import io.reactivex.internal.functions.Functions;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

public abstract class BaseActivity extends AppCompatActivity {

    protected final String TAG = getClass().getSimpleName();

    protected final CompositeDisposable onDestroy = new CompositeDisposable();

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;


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
        onDestroy.add(hideGmsDialog());
    }

    @Nullable
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @CallSuper
    protected void setContentView() {
        if (isLayoutProvided()) {
            setContentView(getActivityLayoutRes());
            ButterKnife.bind(this);

            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }
        }
    }

    protected boolean isLayoutProvided() {
        return getActivityLayoutRes() != 0;
    }

    private Disposable hideGmsDialog() {
        Window window = getWindow();
        if (window != null) {
            window.getDecorView()
                    .getViewTreeObserver()
                    .addOnWindowFocusChangeListener(hasFocus -> {
                        Log.d(TAG, String.format("onWindowFocusChanged: hasFocus %s, isFinishing: %s", hasFocus, isFinishing()));
                        if (!hasFocus && !isFinishing()) {
                            findAndDismissGmsDialog();
                        }
                    });

            return Flowable.interval(25, TimeUnit.MILLISECONDS)
                    .take(10)
                    .doOnNext(tick -> findAndDismissGmsDialog())
                    .subscribe(io.reactivex.rxjava3.internal.functions.Functions.emptyConsumer(), RxUtil.ON_ERROR_LOG_V3);
        } else {
            return Flowable.empty().subscribe();
        }
    }

    private boolean findAndDismissGmsDialog() {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        Log.d(TAG, String.format("Found %s fragments: %s", fragments.size(), fragments));
        for (Fragment f : fragments) {
            String fragmentClassName = f.getClass().getName();
            Log.d(TAG, String.format("Fragment class: %s obj: %s", fragmentClassName, f));
            if (f instanceof SupportErrorDialogFragment && "GooglePlayServicesErrorDialog".equals(f.getTag())) {
                SupportErrorDialogFragment d = (SupportErrorDialogFragment) f;
                d.dismiss();
                Log.w(TAG, String.format("Dismissed fragment: %s", fragmentClassName));
                return true;
            }
        }
        return false;
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

    protected void dialog(Throwable e) {
        AuthError ae = AuthError.fromThrowable(e);
        dialog("Error", String.format("code: %s\nmessage: %s\ncause: %s", ae.getCode(), ae.getMessage(), ae.getCause()));
    }

    protected void dialog(String title, String text) {
        new LovelyInfoDialog(this)
                .setTopColorRes(R.color.colorPrimary)
                .setTopTitle(title)
                .setTopTitleColor(getResources().getColor(android.R.color.white))
                .setMessage(text)
                .show();
    }

    protected void toast(Throwable e) {
        toast(AuthError.fromThrowable(e).toString());
    }

    protected void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onDestroy.clear();
    }
}
