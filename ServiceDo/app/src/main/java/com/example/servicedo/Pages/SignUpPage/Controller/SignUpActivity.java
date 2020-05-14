package com.example.servicedo.Pages.SignUpPage.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.servicedo.Config.DialogConfig;
import com.example.servicedo.Config.ReferencesConfig;
import com.example.servicedo.Pages.HomePage.Controller.HomeActivity;
import com.example.servicedo.Pages.SignInPage.Controller.SignInActivity;
import com.example.servicedo.Pages.SignInPage.Model.User;
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

public class SignUpActivity extends AppCompatActivity {

    private ExtendedEditText edtUserName;
    private TextFieldBoxes edtLayoutUserName;
    private ExtendedEditText edtEmail;
    private TextFieldBoxes edtLayoutEmail;
    private ExtendedEditText edtPhone;
    private TextFieldBoxes edtLayoutPhone;
    private ExtendedEditText edtAddress;
    private TextFieldBoxes edtLayoutAddress;
    private ExtendedEditText edtPassword;
    private TextFieldBoxes edtLayoutPassword;
    private ExtendedEditText edtRePassword;
    private TextFieldBoxes edtLayoutRePassword;

    private RadioButton rdbMale;
    private RadioButton rdbFeMale;
    private Button btnSignUp;
    private TextView tvSignIn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        edtUserName = findViewById(R.id.edt_user_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        edtAddress = findViewById(R.id.edt_address);
        rdbMale = findViewById(R.id.rdb_male);
        rdbFeMale = findViewById(R.id.rdb_female);
        edtPassword = findViewById(R.id.edt_password);
        edtRePassword = findViewById(R.id.edt_re_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        tvSignIn = findViewById(R.id.tv_sign_in);

        edtLayoutUserName = findViewById(R.id.text_field_boxes_user_name);
        edtLayoutEmail = findViewById(R.id.text_field_boxes_email);
        edtLayoutPhone = findViewById(R.id.text_field_boxes_phone);
        edtLayoutAddress = findViewById(R.id.text_field_boxes_address);
        edtLayoutPassword = findViewById(R.id.text_field_boxes_password);
        edtLayoutRePassword = findViewById(R.id.text_field_boxes_re_password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
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

        edtLayoutEmail.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                if(!TextUtils.isEmpty(theNewText)){
                    edtLayoutEmail.setError("", false);
                }
            }
        });

        edtLayoutPhone.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                if(!TextUtils.isEmpty(theNewText)){
                    edtLayoutPhone.setError("", false);
                }
            }
        });

        edtLayoutAddress.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                if(!TextUtils.isEmpty(theNewText)){
                    edtLayoutAddress.setError("", false);
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

        edtLayoutRePassword.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                if(!TextUtils.isEmpty(theNewText)){
                    edtLayoutRePassword.setError("", false);
                }
            }
        });

    }

    public void resetView(){
        edtUserName.setText("");
        edtEmail.setText("");
        edtPhone.setText("");
        edtAddress.setText("");
        edtPassword.setText("");
        edtRePassword.setText("");
    }

    public void handleSignUp(){
        final String userName = edtUserName.getText().toString();
        final String password = edtPassword.getText().toString();
        final String rePassword = edtRePassword.getText().toString();
        final String email = edtEmail.getText().toString();
        final String phone = edtPhone.getText().toString();
        final String address = edtAddress.getText().toString();
        final DialogConfig dialogConfig = new DialogConfig(SignUpActivity.this, SweetAlertDialog.ERROR_TYPE);

        boolean isError = false;
        if (TextUtils.isEmpty(userName)) {
            edtLayoutUserName.setError(getString(R.string.dialog_empty_user_name), true);
            isError = true;
        }
        if (userName.length() > 30) {
            edtLayoutUserName.setError(getString(R.string.dialog_max_length_user_name), true);
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
        if (TextUtils.isEmpty(rePassword)) {
            edtLayoutRePassword.setError(getString(R.string.dialog_empty_password), true);
            isError = true;
        }
        if (TextUtils.isEmpty(email)) {
            edtLayoutEmail.setError(getString(R.string.dialog_empty_email), true);
            isError = true;
        }
        if (TextUtils.isEmpty(phone)) {
            edtLayoutPhone.setError(getString(R.string.dialog_empty_phone), true);
            isError = true;
        }
        if (TextUtils.isEmpty(address)) {
            edtLayoutAddress.setError(getString(R.string.dialog_empty_address), true);
            isError = true;
        }

        if(isError) return;

        if(!edtPassword.getText().toString().equals(edtRePassword.getText().toString())){
            dialogConfig.showDialog(getString(R.string.dialog_password_not_match_re_password));
            return;
        }

        final String sampleEmail = email+"@gmail.com";

        //create user
        mAuth.createUserWithEmailAndPassword(sampleEmail, password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            dialogConfig.showDialog(getString(R.string.dialog_something_when_wrong));
                        } else {
                            mAuth.getCurrentUser().sendEmailVerification();
                            ReferencesConfig referencesConfig = new ReferencesConfig(SignUpActivity.this);
                            referencesConfig.putString(ReferencesConfig.USER_NAME, userName);
                            String userId = mAuth.getCurrentUser().getUid();
                            User.EnumSex sex = (rdbMale.isChecked()) ? User.EnumSex.Male : User.EnumSex.Female;
                            User user = new User(userId, userName, sampleEmail, phone, address, sex.toString(), "");
                            mDatabase.child("user").child(userId).setValue(user);
                            DialogConfig dialogConfig1 = new DialogConfig(SignUpActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            dialogConfig1.showDialog(getString(R.string.dialog_create_user_success));
                            resetView();
                        }
                    }
                });
    }
}
