package com.anotherdev.sample.firebase.auth;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

public class ChangePasswordDialog extends MultipleTextInputDialog {

    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText verifyPasswordEditText;


    public ChangePasswordDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.view_change_password_input;
    }

    @Override
    protected void onConfigureView(View view) {
        super.onConfigureView(view);
        currentPasswordEditText = view.findViewById(R.id.old_password_edittext);
        newPasswordEditText = view.findViewById(R.id.new_password_edittext);
        verifyPasswordEditText = view.findViewById(R.id.verify_password_edittext);
    }

    @Override
    public ChangePasswordDialog setListener(int viewId, boolean dismissOnClick, View.OnClickListener listener) {
        super.setListener(viewId, dismissOnClick, listener);
        return this;
    }

    @Override
    public ChangePasswordDialog setConfirmButtonText(int confirmTextRes) {
        super.setConfirmButtonText(confirmTextRes);
        return this;
    }

    @Override
    protected void dispatchOnConfirmClick() {
        // TODO implement this
    }
}
