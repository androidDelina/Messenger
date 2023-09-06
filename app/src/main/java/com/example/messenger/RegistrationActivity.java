package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private static final String EMAIL_TAG = "email";

    private EditText edit_text_mail;
    private EditText edit_text_password;
    private EditText edit_text_first_name;
    private EditText edit_text_last_name;
    private EditText edit_text_age;

    private Button button_sign_up;

    private RegistrationViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViwes();

        String email = getIntent().getStringExtra(EMAIL_TAG);
        if (email != null) {
            edit_text_mail.setText(email);
        }

        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        observeViewModel();

        setOnClickListener();
    }

    private void setOnClickListener() {
        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = edit_text_mail.getText().toString().trim();
                String password = edit_text_password.getText().toString().trim();
                String first_nam = edit_text_first_name.getText().toString().trim();
                String last_name = edit_text_last_name.getText().toString().trim();
                String age_string = edit_text_age.getText().toString().trim();
                int age = Integer.parseInt(age_string);

                viewModel.createNewUser(mail, password, first_nam, last_name, age);
            }
        });
    }
    private void observeViewModel() {
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = UsersActivity.newIntent(RegistrationActivity.this, firebaseUser.getUid());
                        Log.e("!!!!","4!!!!!");
                    startActivity(intent);
                    finish();
                }
            }
        });

        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessagge) {
                if (errorMessagge != null) {
                    Toast.makeText(RegistrationActivity.this, errorMessagge, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, RegistrationActivity.class);
        intent.putExtra(EMAIL_TAG, email);
        return intent;
    }

    private void initViwes() {
        edit_text_mail = findViewById(R.id.edit_text_mail);
        edit_text_password = findViewById(R.id.edit_text_password);
        edit_text_first_name = findViewById(R.id.edit_text_first_name);
        edit_text_last_name = findViewById(R.id.edit_text_last_name);
        edit_text_age = findViewById(R.id.edit_text_age);

        button_sign_up = findViewById(R.id.button_sign_up);
    }
}