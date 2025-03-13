package com.ttl.coffeemanagement.staff;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ttl.coffeemanagement.data.DatabaseHelper;
import com.ttl.coffeemanagement.R;

public class AddEmployeeDialog extends DialogFragment {
    private EditText edtUsername, edtPassword, edtPhone, edtName;
    private Button btnSave;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_employee, null);
        edtUsername = view.findViewById(R.id.edtUsername);
        edtPassword = view.findViewById(R.id.edtPassword);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtName = view.findViewById(R.id.edtName);
        btnSave = view.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> {
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String name = edtName.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || phone.isEmpty() || name.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (DatabaseHelper.insertEmployee(username, password, phone, name)) {
                Toast.makeText(getContext(), "Employee added!", Toast.LENGTH_SHORT).show();
                dismiss();
                ((EmployeeActivity) getActivity()).loadEmployees();
            } else {
                Toast.makeText(getContext(), "Failed to add!", Toast.LENGTH_SHORT).show();
            }
        });

        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(view);
        return dialog;
    }
}