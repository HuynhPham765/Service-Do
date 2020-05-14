package com.example.servicedo.Pages.HomePage.Model;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.servicedo.Pages.CreateFeed.Model.Feed;
import com.example.servicedo.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListViewFeedAdapter extends ArrayAdapter<Feed> {

    private Context context;
    private int resource;
    private ArrayList<Feed> feeds;

    public ListViewFeedAdapter(Context context, int resource, ArrayList<Feed> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.feeds = objects;
    }

    @Override
    public int getCount() {
        return feeds.size();
    }

    @Override
    public Feed getItem(int position) {
        return (feeds.size() != 0) ? feeds.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imgView = (ImageView) convertView.findViewById(R.id.img_feed);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvReward = (TextView) convertView.findViewById(R.id.tv_reward);
            viewHolder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            viewHolder.tvCreateBy = (TextView) convertView.findViewById(R.id.tv_create_by);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        System.out.println("render view item");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        if(feeds != null && feeds.size() != 0) {
            Feed feed = feeds.get(position);
            Feed.EnumStatus enumStatus = Feed.EnumStatus.valueOf(feed.getStatus());
            String status = "";
            switch (enumStatus){
                case New:
                    status = context.getString(R.string.tv_status_new);
                    break;
                case Done:
                    status = context.getString(R.string.tv_status_done);
                    break;
                case Close:
                    status = context.getString(R.string.tv_status_close);
                    break;
                case Progress:
                    status = context.getString(R.string.tv_status_progress);
                    break;
                default:
                    status = context.getString(R.string.tv_status);
                    break;
            }

            if(feed.getImage() != null && feed.getImage().size() != 0) {
                Picasso.get().load(feed.getImage().get(0)).into(viewHolder.imgView);
            }
            viewHolder.tvTitle.setText(feed.getTitle());
            viewHolder.tvReward.setText(context.getString(R.string.tv_reward)+" "+feed.getReward());
            viewHolder.tvAddress.setText(feed.getAddress());
            viewHolder.tvStatus.setText(context.getString(R.string.tv_status)+" "+status);

            DatabaseReference userNameReference = databaseReference.child("user").child(feed.getCreateBy()).child("userName");
            userNameReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String userName = dataSnapshot.getValue().toString();
                    viewHolder.tvCreateBy.setText(userName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        return convertView;
    }

    public class ViewHolder {
        ImageView imgView;
        TextView tvTitle;
        TextView tvReward;
        TextView tvAddress;
        TextView tvStatus;
        TextView tvCreateBy;
    }
}
