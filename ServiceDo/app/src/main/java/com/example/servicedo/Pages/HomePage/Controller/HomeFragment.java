package com.example.servicedo.Pages.HomePage.Controller;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.servicedo.Pages.CreateFeed.Model.Feed;
import com.example.servicedo.Pages.DetailFeedPage.Controller.DetailFeedActivity;
import com.example.servicedo.Pages.HomePage.Model.ListViewFeedAdapter;
import com.example.servicedo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private DatabaseReference databaseReference;
    private DatabaseReference feedReference;

    private ArrayList<Feed> feeds;

    private ListView lvFeed;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        lvFeed = view.findViewById(R.id.lv_feed);
        feeds = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        feedReference = databaseReference.child("feeds");
        Query newFeedQuery = feedReference
                .orderByChild("status").equalTo("New");
        ValueEventListener mMessagesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("data change");
                feeds.clear();
                ArrayList<Feed> revArrayList = new ArrayList<Feed>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Feed feed = child.getValue(Feed.class);
                    revArrayList.add(feed);
                }

                for (int i = revArrayList.size() - 1; i >= 0; i--) {
                    feeds.add(revArrayList.get(i));
                }

                final ListViewFeedAdapter adapter = new ListViewFeedAdapter(getContext(), R.layout.lv_feed_adapter, feeds);
                lvFeed.setAdapter(adapter);
                lvFeed.setDividerHeight(5);

                lvFeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), DetailFeedActivity.class);
                        Gson gson = new Gson();
                        String jsonFeed = (feeds != null && feeds.size() >= position) ? gson.toJson(feeds.get(position)) : "";
                        intent.putExtra(Feed.FEED_STRING, jsonFeed);
                        startActivity(intent);
                    }
                });
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Could not successfully listen for data, log the error
            }
        };
        feedReference.addListenerForSingleValueEvent(mMessagesListener);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
