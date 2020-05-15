package com.anotherdev.sample.firebase.auth;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;

@SuppressWarnings("WeakerAccess")
public class ChangePasswordDialog extends MultipleTextInputDialog {

    public interface OnConfirmClickListener {
        void onConfirmClick(String oldPassword, String newPassword);
    }

    @BindView(R.id.old_password_edittext) EditText currentPasswordEditText;
    @BindView(R.id.new_password_edittext) EditText newPasswordEditText;
    @BindView(R.id.verify_password_edittext) EditText verifyPasswordEditText;

    private OnConfirmClickListener confirmClickListener;


    public ChangePasswordDialog(Context context) {
        super(context);
        setTopTitle(R.string.change_password);
        setTopTitleColor(context.getResources().getColor(android.R.color.white));
        setIcon(R.drawable.ic_vpn_key_white_24dp);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.view_change_password_input;
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

    public ChangePasswordDialog onConfirm(OnConfirmClickListener listener) {
        confirmClickListener = listener;
        return this;
    }

    @Override
    protected void dispatchOnConfirmClick() {
        String oldPassword = currentPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        if (confirmClickListener != null) {
            confirmClickListener.onConfirmClick(oldPassword, newPassword);
        }
    }
}
