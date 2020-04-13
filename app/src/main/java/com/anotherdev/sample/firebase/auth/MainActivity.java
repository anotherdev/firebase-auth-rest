package com.anotherdev.sample.firebase.auth;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.anotherdev.firebase.auth.FirebaseAuthRest;
import com.anotherdev.sample.firebase.auth.intent.LoginIntent;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class MainActivity extends BaseActivity {

    @BindView(R.id.private_tfb) TextFieldBoxes privateTextFieldBoxes;
    @BindView(R.id.private_eet) ExtendedEditText privateEditText;

    @BindView(R.id.global_public_tfb) TextFieldBoxes globalPublicTextFieldBoxes;
    @BindView(R.id.global_public_eet) ExtendedEditText globalPublicEditText;

    @BindView(R.id.authenticated_tfb) TextFieldBoxes authenticatedTextFieldBoxes;
    @BindView(R.id.authenticated_eet) ExtendedEditText authenticatedEditText;

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

        privateListener = rtdb.getReference("private")
                .addValueEventListener(new FarValueEventListener(privateTextFieldBoxes, privateEditText));

        DatabaseReference globalPublicPath = rtdb.getReference("global_public");
        globalPublicListener = globalPublicPath
                .addValueEventListener(new FarValueEventListener(globalPublicTextFieldBoxes, globalPublicEditText));
        globalPublicPath.setValue(System.currentTimeMillis());

        DatabaseReference authenticatedPath = rtdb.getReference("authenticated");
        authenticatedListener = authenticatedPath
                .addValueEventListener(new FarValueEventListener(authenticatedTextFieldBoxes, authenticatedEditText));
        authenticatedPath.setValue(System.currentTimeMillis());


        com.anotherdev.firebase.auth.common.FirebaseAuth auth = FirebaseAuthRest.getInstance(app);
        com.anotherdev.firebase.auth.FirebaseUser user = auth.getCurrentUser();

        Log.e("FAR", "auth.getUid(): " + auth.getUid());
        Log.e("FAR", "user: " + user);
        if (user != null) {
            Log.e("FAR", "uid: " + user.getUid());
            Log.e("FAR", "expire: " + user.getExpirationTime());
            Log.e("FAR", "id_token: " + user.getIdToken());
            Log.e("FAR", "refresh_token: " + user.getRefreshToken());
        }
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
