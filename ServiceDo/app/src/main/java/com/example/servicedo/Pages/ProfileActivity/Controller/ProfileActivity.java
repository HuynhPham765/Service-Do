package com.example.servicedo.Pages.ProfileActivity.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.servicedo.Pages.CreateFeed.Model.Feed;
import com.example.servicedo.Pages.SignInPage.Model.User;
import com.example.servicedo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private CircleImageView imgAvatar;
    private TextView tvUserName;
    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvSex;
    private TextView tvAddress;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgAvatar = findViewById(R.id.img_avatar);
        tvUserName = findViewById(R.id.tv_user_name);
        tvEmail = findViewById(R.id.tv_email);
        tvPhone = findViewById(R.id.tv_phone);
        tvSex = findViewById(R.id.tv_sex);
        tvAddress = findViewById(R.id.tv_address);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");
        DatabaseReference userRef = mDatabase.child("user").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null){
                    tvUserName.setText(user.getUserName());
                    tvEmail.setText(user.getEmail());
                    tvPhone.setText(user.getPhone());
                    User.EnumSex enumSex = User.EnumSex.valueOf(user.getSex());
                    if(enumSex.equals(User.EnumSex.Male)){
                        tvSex.setText(getString(R.string.rdb_sex_male));
                    } else {
                        tvSex.setText(getString(R.string.rdb_sex_female));
                    }
                    tvAddress.setText(user.getAddress());
                    if(!TextUtils.isEmpty(user.getAvatar())){
                        Picasso.get().load(user.getAvatar()).into(imgAvatar);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
