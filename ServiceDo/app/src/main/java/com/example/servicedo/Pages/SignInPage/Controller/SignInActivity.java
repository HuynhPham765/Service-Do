package com.example.servicedo.Pages.SignInPage.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.servicedo.Config.DialogConfig;
import com.example.servicedo.Config.ReferencesConfig;
import com.example.servicedo.Pages.HomePage.Controller.HomeActivity;
import com.example.servicedo.Pages.SignInPage.Model.User;
import com.example.servicedo.Pages.SignUpPage.Controller.SignUpActivity;
import com.example.servicedo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private EditText edtUserName;
    private EditText edtPassword;
    private Button btnSignIn;
    private TextView tvSignUp;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ReferencesConfig referencesConfig;

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        edtUserName = findViewById(R.id.edt_user_name);
        edtPassword = findViewById(R.id.edt_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        tvSignUp = findViewById(R.id.tv_sign_up);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = edtUserName.getText().toString();
                final String password = edtPassword.getText().toString();
                handleSignIn(userName, password);
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void handleSignIn(final String userName, final String password){
        final DialogConfig dialogConfig = new DialogConfig(this);
        if (TextUtils.isEmpty(userName)) {
            dialogConfig.showAlertDialog(getString(R.string.dialog_empty_user_name));
            return;
        }

        if (TextUtils.isEmpty(password)) {
            dialogConfig.showAlertDialog(getString(R.string.dialog_empty_password));
            return;
        }

        if (password.length() < 6) {
            dialogConfig.showAlertDialog(getString(R.string.dialog_password_least_6));
            return;
        }

        mAuth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            dialogConfig.showAlertDialog(getString(R.string.dialog_something_when_wrong));
                        } else {
                            String userId = mAuth.getCurrentUser().getUid();
                            User user = new User(userId, userName);
                            mDatabase.child("user").child(userId).setValue(user);
                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
