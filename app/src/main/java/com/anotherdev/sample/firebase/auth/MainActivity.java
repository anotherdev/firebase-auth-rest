package com.anotherdev.sample.firebase.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.sample.firebase.auth.intent.LoginIntent;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends BaseActivity {

    @Override
    protected int getActivityLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestAuthIfNeeded();

        FirebaseApp app = FirebaseApp.getInstance();
        Log.e(getClass().getSimpleName(), String.valueOf(app));

        com.anotherdev.firebase.auth.common.FirebaseAuth auth = FirebaseAuthRest.getInstance(app);
        com.anotherdev.firebase.auth.FirebaseUser user = auth.getCurrentUser();

        Log.e("FAR", "auth.getUid(): " + auth.getUid());
        Log.e("FAR", "user: " + user);

        /*if (user == null) {
            auth.signInAnonymously()
                    .doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable e) throws Throwable {
                            Log.e("FAR", "error", e);
                        }
                    })
                    .onErrorComplete()
                    .doOnSuccess(new Consumer<SignInAnonymouslyResponse>() {
                        @Override
                        public void accept(SignInAnonymouslyResponse response) throws Throwable {
                            Log.e("FAR", "doOnSuccess: " + auth.getCurrentUser());
                        }
                    })
                    .subscribe();
        } else {
            Log.e("FAR", "Already signed in: " + user);
        }*/

        FirebaseDatabase.getInstance()
                .getReference("test")
                .setValue(System.currentTimeMillis());
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
}
