package com.anotherdev.sample.firebase.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.firebase.auth.util.RxUtil;
import com.anotherdev.sample.firebase.auth.intent.LoginIntent;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import io.reactivex.rxjava3.internal.functions.Functions;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class MainActivity extends BaseActivity {

    @BindView(R.id.private_tfb) TextFieldBoxes privateTextFieldBoxes;
    @BindView(R.id.private_eet) ExtendedEditText privateEditText;

    @BindView(R.id.global_public_tfb) TextFieldBoxes globalPublicTextFieldBoxes;
    @BindView(R.id.global_public_eet) ExtendedEditText globalPublicEditText;

    @BindView(R.id.authenticated_tfb) TextFieldBoxes authenticatedTextFieldBoxes;
    @BindView(R.id.authenticated_eet) ExtendedEditText authenticatedEditText;

    DatabaseReference privatePath;
    DatabaseReference globalPublicPath;
    DatabaseReference authenticatedPath;

    ValueEventListener privateListener;
    ValueEventListener globalPublicListener;
    ValueEventListener authenticatedListener;


    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestAuthIfNeeded();

        FirebaseApp app = FirebaseApp.getInstance();
        Log.i(TAG, String.valueOf(app));

        FirebaseDatabase rtdb = FirebaseDatabase.getInstance();
        privatePath = rtdb.getReference("private");
        globalPublicPath = rtdb.getReference("global_public");
        authenticatedPath = rtdb.getReference("authenticated");

        privateListener = privatePath
                .addValueEventListener(new FarValueEventListener(privateTextFieldBoxes, privateEditText));
        globalPublicListener = globalPublicPath
                .addValueEventListener(new FarValueEventListener(globalPublicTextFieldBoxes, globalPublicEditText));

        onDestroy.add(FirebaseAuthRest.getInstance(app)
                .authStateChanges()
                .doOnNext(auth -> {
                    Log.w(TAG, "authStateChanges() isSignedIn: " + auth.isSignedIn());
                    rtdb.goOffline();
                    rtdb.goOnline();
                })
                .subscribe(Functions.emptyConsumer(), RxUtil.ON_ERROR_LOG_V3));
    }

    @Override
    protected void onResume() {
        super.onResume();
        authenticatedListener = authenticatedPath
                .addValueEventListener(new FarValueEventListener(authenticatedTextFieldBoxes, authenticatedEditText));
        globalPublicPath.setValue(System.currentTimeMillis());
        authenticatedPath.setValue(System.currentTimeMillis());
    }

    @Override
    protected void onPause() {
        super.onPause();
        authenticatedPath.removeEventListener(authenticatedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        final int itemId = item.getItemId();
        if (R.id.action_view_accounts == itemId) {
            startActivity(new LoginIntent(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        privatePath.removeEventListener(privateListener);
        globalPublicPath.removeEventListener(globalPublicListener);
    }
}
