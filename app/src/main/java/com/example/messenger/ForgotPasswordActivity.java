package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String EMAIL_TAG = "email";

    private TextView edit_text_mail;
    private Button button_reset_password;

    ForgotPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initViews();

        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
        observeViewModel();

        String email = getIntent().getStringExtra(EMAIL_TAG);
        if (email != null) {
            edit_text_mail.setText(email);
        }

        button_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edit_text_mail.getText().toString().trim();
                viewModel.resetPassword(email);
            }
        });
    }

    private void observeViewModel() {
        viewModel.getSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isResetPasswordSucces) {
                if (isResetPasswordSucces) {
                    finish();
                }
            }
        });
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(ForgotPasswordActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, ForgotPasswordActivity.class);
        intent.putExtra(EMAIL_TAG, email);
        return intent;
    }

    private void initViews() {
        edit_text_mail = findViewById(R.id.edit_text_mail);
        button_reset_password = findViewById(R.id.button_reset_password);
    }
}