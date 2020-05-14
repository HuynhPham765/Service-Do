package com.example.servicedo.Pages.ListFeedPage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.servicedo.Pages.CreateFeed.Model.Feed;
import com.example.servicedo.Pages.HomePage.Model.ListViewFeedAdapter;
import com.example.servicedo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListFeedActivity extends AppCompatActivity {
    private ListView lvFeed;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ArrayList<Feed> feeds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_feed);

        lvFeed = findViewById(R.id.lv_feed);
        toolbar = findViewById(R.id.toolbar);

        feeds = new ArrayList<>();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Drawable drawable = getResources().getDrawable(R.drawable.ic_back_foreground);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(drawable);

        final ListViewFeedAdapter adapter = new ListViewFeedAdapter(this, R.layout.lv_feed_adapter, feeds);
        lvFeed.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        boolean isMyFeed = intent.getBooleanExtra("isMyFeed", true);

        Query myFeedQuery = mDatabase.child("feeds")
                .orderByChild("createBy").equalTo(mAuth.getCurrentUser().getUid());

        Query receiveFeedQuery = mDatabase.child("feeds")
                .orderByChild("workBy").equalTo(mAuth.getCurrentUser().getUid());

        if(isMyFeed){
            myFeedQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(feeds != null) {
                        feeds.clear();
                        for (DataSnapshot feedSnapshot : dataSnapshot.getChildren()) {
                            Feed feed = feedSnapshot.getValue(Feed.class);
                            feeds.add(feed);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            actionBar.setTitle(getString(R.string.toolbar_title_my_feed));
        } else {
            receiveFeedQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(feeds != null) {
                        feeds.clear();
                        for (DataSnapshot feedSnapshot : dataSnapshot.getChildren()) {
                            Feed feed = feedSnapshot.getValue(Feed.class);
                            feeds.add(feed);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            actionBar.setTitle(getString(R.string.toolbar_title_receive_feed));
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
}
