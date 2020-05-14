package com.example.servicedo.Pages.DetailFeedPage.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.servicedo.Config.DialogConfig;
import com.example.servicedo.Pages.CreateFeed.Model.Feed;
import com.example.servicedo.Pages.CreateFeed.Model.Feed.EnumStatus;
import com.example.servicedo.Pages.DetailFeedPage.Model.RecyclerViewImageFeedAdapter;
import com.example.servicedo.Pages.ProfileActivity.Controller.ProfileActivity;
import com.example.servicedo.Pages.SignInPage.Model.User;
import com.example.servicedo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.servicedo.Pages.CreateFeed.Model.Feed.EnumStatus.*;

public class DetailFeedActivity extends AppCompatActivity {

    private RecyclerView rcvImage;
    private TextView tvTitle;
    private TextView tvReward;
    private TextView tvUserName;
    private TextView tvDecscription;
    private TextView tvStatus;
    private CircleImageView imgUser;
    private Button btnReceiveWork;
    private Button btnCancelWork;
    private Button btnCloseWork;
    private Button btnReopenWork;
    private Button btnDoneWork;
    private Toolbar toolbar;
    private LinearLayout layoutUser;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);

        rcvImage = findViewById(R.id.rcv_image);
        tvTitle = findViewById(R.id.tv_title);
        tvReward = findViewById(R.id.tv_reward);
        tvUserName = findViewById(R.id.tv_user_name);
        tvDecscription = findViewById(R.id.tv_description);
        tvStatus = findViewById(R.id.tv_status);
        imgUser = findViewById(R.id.img_user);
        btnReceiveWork = findViewById(R.id.btn_receive_work);
        btnCancelWork = findViewById(R.id.btn_cancel_work);
        btnCloseWork = findViewById(R.id.btn_close_work);
        btnReopenWork = findViewById(R.id.btn_reopen_work);
        btnDoneWork = findViewById(R.id.btn_done_work);
        toolbar = findViewById(R.id.toolbar);
        layoutUser = findViewById(R.id.layout_user);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back_foreground);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(drawable);
        actionBar.setTitle(getString(R.string.toolbar_title_detail_feed));

        databaseReference = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        String jsonFeed = intent.getStringExtra(Feed.FEED_STRING);
        Gson gson = new Gson();
        final Feed feed = (!TextUtils.isEmpty(jsonFeed)) ? gson.fromJson(jsonFeed, Feed.class) : null;
        if(feed != null){

            String status = feed.getStatus();
            Feed.EnumStatus enumStatus = Feed.EnumStatus.valueOf(status);
            String userId = mAuth.getCurrentUser().getUid();
            final String createBy = feed.getCreateBy();

            if(userId.equals(createBy)) {
                if (enumStatus.equals(New)) {
                    btnCloseWork.setVisibility(View.VISIBLE);
                    btnDoneWork.setVisibility(View.GONE);
                    btnReopenWork.setVisibility(View.GONE);
                    btnCancelWork.setVisibility(View.GONE);
                    btnReceiveWork.setVisibility(View.GONE);
                    tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_new));
                } else if(enumStatus.equals(Progress)){
                    btnCloseWork.setVisibility(View.GONE);
                    btnDoneWork.setVisibility(View.VISIBLE);
                    btnReopenWork.setVisibility(View.GONE);
                    btnCancelWork.setVisibility(View.GONE);
                    btnReceiveWork.setVisibility(View.GONE);
                    tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_progress));
                } else if(enumStatus.equals(Close)){
                    btnCloseWork.setVisibility(View.GONE);
                    btnDoneWork.setVisibility(View.GONE);
                    btnReopenWork.setVisibility(View.VISIBLE);
                    btnCancelWork.setVisibility(View.GONE);
                    btnReceiveWork.setVisibility(View.GONE);
                    tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_close));
                } else if(status.equals(Done)){
                    btnCloseWork.setVisibility(View.GONE);
                    btnDoneWork.setVisibility(View.GONE);
                    btnReopenWork.setVisibility(View.GONE);
                    btnCancelWork.setVisibility(View.GONE);
                    btnReceiveWork.setVisibility(View.GONE);
                    tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_done));
                }
            } else {
                layoutUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intentProfile = new Intent(DetailFeedActivity.this, ProfileActivity.class);
                        intentProfile.putExtra("userId", createBy);
                        startActivity(intentProfile);
                    }
                });
                if (enumStatus.equals(New)) {
                    btnCloseWork.setVisibility(View.GONE);
                    btnDoneWork.setVisibility(View.GONE);
                    btnReopenWork.setVisibility(View.GONE);
                    btnCancelWork.setVisibility(View.GONE);
                    btnReceiveWork.setVisibility(View.VISIBLE);
                    tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_new));
                } else if(enumStatus.equals(Progress)){
                    btnCloseWork.setVisibility(View.GONE);
                    btnDoneWork.setVisibility(View.GONE);
                    btnReopenWork.setVisibility(View.GONE);
                    btnCancelWork.setVisibility(View.VISIBLE);
                    btnReceiveWork.setVisibility(View.GONE);
                    tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_progress));
                } else if(enumStatus.equals(Close)){
                    btnCloseWork.setVisibility(View.GONE);
                    btnDoneWork.setVisibility(View.GONE);
                    btnReopenWork.setVisibility(View.GONE);
                    btnCancelWork.setVisibility(View.GONE);
                    btnReceiveWork.setVisibility(View.GONE);
                    tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_close));
                } else if(enumStatus.equals(Done)){
                    btnCloseWork.setVisibility(View.GONE);
                    btnDoneWork.setVisibility(View.GONE);
                    btnReopenWork.setVisibility(View.GONE);
                    btnCancelWork.setVisibility(View.GONE);
                    btnReceiveWork.setVisibility(View.GONE);
                    tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_done));
                }
            }

            RecyclerViewImageFeedAdapter adapter = new RecyclerViewImageFeedAdapter(this, feed.getImage());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DetailFeedActivity.this, LinearLayoutManager.HORIZONTAL, false);
            rcvImage.setLayoutManager(mLayoutManager);
            rcvImage.setAdapter(adapter);

            tvTitle.setText(feed.getTitle());
            tvReward.setText(feed.getReward());
            tvDecscription.setText(feed.getDescription());

            DatabaseReference userReference = databaseReference.child("user").child(feed.getCreateBy());
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    tvUserName.setText(user.getUserName());
                    if(user.getAvatar() != null && !user.getAvatar().equals("")){
                        Picasso.get().load(user.getAvatar()).into(imgUser);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            final DatabaseReference statusRef = databaseReference.child("feeds").child(feed.getFeedId()).child("status");
            final DatabaseReference workByRef = databaseReference.child("feeds").child(feed.getFeedId()).child("workBy");

            btnReceiveWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleReceiveWork(statusRef, workByRef);
                }
            });

            btnCancelWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleCancelWork(statusRef);
                }
            });

            btnCloseWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleCloseWork(statusRef);
                }
            });

            btnReopenWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleReOpenWork(statusRef);
                }
            });

            btnDoneWork.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleDoneWork(statusRef);
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleCloseWork(DatabaseReference dbRef){
        final DialogConfig dialogConfig = new DialogConfig(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogConfig.showProgress(getString(R.string.dialog_close_work));
        dbRef.setValue(Close).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialogConfig.dismissProgress();
                DialogConfig dialogConfig1 = new DialogConfig(DetailFeedActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                dialogConfig1.showDialog(getString(R.string.dialog_success));
                btnCloseWork.setVisibility(View.GONE);
                btnDoneWork.setVisibility(View.GONE);
                btnReopenWork.setVisibility(View.VISIBLE);
                btnCancelWork.setVisibility(View.GONE);
                btnReceiveWork.setVisibility(View.GONE);
                tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_close));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialogConfig.dismissProgress();
                DialogConfig dialogConfig1 = new DialogConfig(DetailFeedActivity.this, SweetAlertDialog.ERROR_TYPE);
                dialogConfig1.showDialog(getString(R.string.dialog_fail));
            }
        });
    }

    private void handleDoneWork(DatabaseReference dbRef){
        final DialogConfig dialogConfig = new DialogConfig(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogConfig.showProgress(getString(R.string.dialog_done_work));
        dbRef.setValue(Done).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialogConfig.dismissProgress();
                DialogConfig dialogConfig1 = new DialogConfig(DetailFeedActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                dialogConfig1.showDialog(getString(R.string.dialog_success));
                btnCloseWork.setVisibility(View.GONE);
                btnDoneWork.setVisibility(View.GONE);
                btnReopenWork.setVisibility(View.GONE);
                btnCancelWork.setVisibility(View.GONE);
                btnReceiveWork.setVisibility(View.GONE);
                tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_done));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialogConfig.dismissProgress();
                DialogConfig dialogConfig1 = new DialogConfig(DetailFeedActivity.this, SweetAlertDialog.ERROR_TYPE);
                dialogConfig1.showDialog(getString(R.string.dialog_fail));
            }
        });
    }

    private void handleReceiveWork(DatabaseReference dbRef, DatabaseReference workRef){

        final DialogConfig dialogConfig = new DialogConfig(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogConfig.showProgress(getString(R.string.dialog_receive_work));
        workRef.setValue(mAuth.getCurrentUser().getUid());
        dbRef.setValue(Progress).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialogConfig.dismissProgress();
                DialogConfig dialogConfig1 = new DialogConfig(DetailFeedActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                dialogConfig1.showDialog(getString(R.string.dialog_success));
                btnCloseWork.setVisibility(View.GONE);
                btnDoneWork.setVisibility(View.GONE);
                btnReopenWork.setVisibility(View.GONE);
                btnCancelWork.setVisibility(View.VISIBLE);
                btnReceiveWork.setVisibility(View.GONE);
                tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_progress));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialogConfig.dismissProgress();
                DialogConfig dialogConfig1 = new DialogConfig(DetailFeedActivity.this, SweetAlertDialog.ERROR_TYPE);
                dialogConfig1.showDialog(getString(R.string.dialog_fail));
            }
        });
    }
    private void handleCancelWork(DatabaseReference dbRef){
        final DialogConfig dialogConfig = new DialogConfig(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogConfig.showProgress(getString(R.string.dialog_cancel_work));
        dbRef.setValue(New).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialogConfig.dismissProgress();
                DialogConfig dialogConfig1 = new DialogConfig(DetailFeedActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                dialogConfig1.showDialog(getString(R.string.dialog_success));
                btnCloseWork.setVisibility(View.GONE);
                btnDoneWork.setVisibility(View.GONE);
                btnReopenWork.setVisibility(View.GONE);
                btnCancelWork.setVisibility(View.GONE);
                btnReceiveWork.setVisibility(View.VISIBLE);
                tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_new));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialogConfig.dismissProgress();
                DialogConfig dialogConfig1 = new DialogConfig(DetailFeedActivity.this, SweetAlertDialog.ERROR_TYPE);
                dialogConfig1.showDialog(getString(R.string.dialog_fail));
            }
        });
    }

    private void handleReOpenWork(DatabaseReference dbRef){
        final DialogConfig dialogConfig = new DialogConfig(this, SweetAlertDialog.PROGRESS_TYPE);
        dialogConfig.showProgress(getString(R.string.dialog_reopen_work));
        dbRef.setValue(New).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialogConfig.dismissProgress();
                DialogConfig dialogConfig1 = new DialogConfig(DetailFeedActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                dialogConfig1.showDialog(getString(R.string.dialog_success));
                btnCloseWork.setVisibility(View.VISIBLE);
                btnDoneWork.setVisibility(View.GONE);
                btnReopenWork.setVisibility(View.GONE);
                btnCancelWork.setVisibility(View.GONE);
                btnReceiveWork.setVisibility(View.GONE);
                tvStatus.setText(getString(R.string.tv_status)+" "+getString(R.string.tv_status_new));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialogConfig.dismissProgress();
                DialogConfig dialogConfig1 = new DialogConfig(DetailFeedActivity.this, SweetAlertDialog.ERROR_TYPE);
                dialogConfig1.showDialog(getString(R.string.dialog_fail));
            }
        });
    }
}
