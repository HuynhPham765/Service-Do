package com.example.servicedo.Pages.DetailFeedPage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.servicedo.Pages.CreateFeed.Model.Feed;
import com.example.servicedo.R;
import com.google.gson.Gson;

public class DetailFeedActivity extends AppCompatActivity {

    private RecyclerView rcvImage;
    private TextView tvTitle;
    private TextView tvReward;
    private TextView tvUserName;
    private ImageView imgUser;
    private Button btnChangeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_feed);

        rcvImage = findViewById(R.id.rcv_image);
        tvTitle = findViewById(R.id.tv_title);
        tvReward = findViewById(R.id.tv_reward);
        tvUserName = findViewById(R.id.tv_user_name);
        imgUser = findViewById(R.id.img_user);
        btnChangeStatus = findViewById(R.id.btn_change_status);

        Intent intent = getIntent();
        String jsonFeed = intent.getStringExtra(Feed.FEED_STRING);
        Gson gson = new Gson();
        Feed feed = (!TextUtils.isEmpty(jsonFeed)) ? gson.fromJson(jsonFeed, Feed.class) : null;
        if(feed != null){
            tvTitle.setText(feed.getTitle());
            tvReward.setText(feed.getReward());

        }
    }
}
