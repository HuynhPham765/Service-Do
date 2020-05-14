package com.example.servicedo.Pages.DetailFeedPage.Model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.servicedo.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewImageFeedAdapter extends RecyclerView.Adapter<RecyclerViewImageFeedAdapter.ImageViewHolder> {
    private ArrayList<String> listImage;
    private Activity activity;

    public RecyclerViewImageFeedAdapter(Activity activity, ArrayList<String> bookList) {
        this.activity = activity;
        this.listImage = bookList;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgFeed;

        public ImageViewHolder(View itemView) {
            super(itemView);
            imgFeed = itemView.findViewById(R.id.img_feed);
        }
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /** Get layout */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rcv_image_item_adapter, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        String url = listImage.get(position);
        Picasso.get().load(url).into(holder.imgFeed);
        holder.imgFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //handle onclick itemView
            }
        });
    }

    @Override
    public int getItemCount() {
        return listImage.size();
    }
}
