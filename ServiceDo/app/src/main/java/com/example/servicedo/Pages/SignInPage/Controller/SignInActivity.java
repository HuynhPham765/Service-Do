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

import cn.pedant.SweetAlert.SweetAlertDialog;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;


public class SignInActivity extends AppCompatActivity {

    private ExtendedEditText edtUserName;
    private ExtendedEditText edtPassword;
    private TextFieldBoxes edtLayoutUserName;
    private TextFieldBoxes edtLayoutPassword;
    private Button btnSignIn;
    private TextView tvSignUp;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ReferencesConfig referencesConfig;

    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            if(mAuth.getCurrentUser().isEmailVerified()) {
                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        edtUserName = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        tvSignUp = findViewById(R.id.tv_sign_up);

        edtLayoutUserName = findViewById(R.id.text_field_boxes_email);
        edtLayoutPassword = findViewById(R.id.text_field_boxes_password);

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

        edtLayoutUserName.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                if(!TextUtils.isEmpty(theNewText)){
                    edtLayoutUserName.setError("", false);
                }
            }
        });

        edtLayoutPassword.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                if(!TextUtils.isEmpty(theNewText)){
                    edtLayoutPassword.setError("", false);
                }
            }
        });
    }

    public void handleSignIn(final String userName, final String password){
        final DialogConfig dialogConfig = new DialogConfig(this, SweetAlertDialog.ERROR_TYPE);
        boolean isError = false;
        if (TextUtils.isEmpty(userName)) {
            edtLayoutUserName.setError(getString(R.string.dialog_empty_email), true);
            isError = true;
        }
        if (TextUtils.isEmpty(password)) {
            edtLayoutPassword.setError(getString(R.string.dialog_empty_password), true);
            isError = true;
        }
        if (password.length() < 6) {
            edtLayoutPassword.setError(getString(R.string.dialog_password_least_6), true);
            isError = true;
        }

        if(isError) return;

        String simpleEmail = userName+"@gmail.com";

        mAuth.signInWithEmailAndPassword(simpleEmail, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            dialogConfig.showDialog(getString(R.string.dialog_something_when_wrong));
                        } else {
                            if(!mAuth.getCurrentUser().isEmailVerified()){
                                dialogConfig.showDialog(getString(R.string.dialog_sign_in_with_no_verify));
                                mAuth.getCurrentUser().sendEmailVerification();
                            } else {
//                                String userId = mAuth.getCurrentUser().getUid();
//                                User user = new User(userId, userName);
//                                mDatabase.child("user").child(userId).setValue(user);
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
    }
}
