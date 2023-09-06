package com.example.messenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInActivity extends AppCompatActivity {

    private static final String TAG = "LogInActivity";
    private FirebaseAuth mAuth;

    private EditText edit_text_mail;
    private EditText edit_text_password;

    private Button button_log_in;

    private TextView text_view_forgot_password;
    private TextView text_view_register;

    private LogInViewModel viewModel;

    private String mail;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        initViews();

        viewModel = new ViewModelProvider(this).get(LogInViewModel.class);
        observeViewModel();

        setupClickListeners();

    }

    private void observeViewModel() {
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser user) {
                if (user != null) {
                    Log.e("!!!!","1!!!!!");
                    Intent intent = UsersActivity.newIntent(LogInActivity.this, user.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });
        viewModel.getException().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage != null) {
                    Toast.makeText(LogInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupClickListeners() {
        button_log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextsData();
                viewModel.logIn(mail, password);
            }
        });

        text_view_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextsData();
                Intent intent = ForgotPasswordActivity.newIntent(LogInActivity.this, mail);
                    Log.e("!!!!","2!!!!!");
                startActivity(intent);
            }
        });

        text_view_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEditTextsData();
                Intent intent = RegistrationActivity.newIntent(LogInActivity.this, mail);
                    Log.e("!!!!","3!!!!!");
                startActivity(intent);
            }
        });
    }

    private void initViews() {
        edit_text_mail = findViewById(R.id.edit_text_mail);
        edit_text_password = findViewById(R.id.edit_text_password);

        button_log_in = findViewById(R.id.button_log_in);

        text_view_forgot_password = findViewById(R.id.text_view_forgot_password);
        text_view_register = findViewById(R.id.text_view_register);
    }

    private void getEditTextsData() {
        mail = edit_text_mail.getText().toString().trim();
        password = edit_text_password.getText().toString().trim();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LogInActivity.class);
        return intent;
    }

}