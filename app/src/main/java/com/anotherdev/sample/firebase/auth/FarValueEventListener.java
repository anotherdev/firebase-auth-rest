package com.anotherdev.sample.firebase.auth;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class FarValueEventListener implements ValueEventListener {

    private final TextFieldBoxes textBox;
    private final ExtendedEditText editText;


    FarValueEventListener(TextFieldBoxes textBox, ExtendedEditText editText) {
        this.textBox = textBox;
        this.editText = editText;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot data) {
        editText.setText(String.valueOf(data.getValue()));
        textBox.setError("", false);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError dbe) {
        String error = String.format("code: %s msg: %s", dbe.getCode(), dbe.getMessage());
        String detailedError = String.format("%s detail: %s", error, dbe.getDetails());
        Log.e(getClass().getName(), detailedError);
        editText.setText(error);
        textBox.setError(error, false);
    }
}
