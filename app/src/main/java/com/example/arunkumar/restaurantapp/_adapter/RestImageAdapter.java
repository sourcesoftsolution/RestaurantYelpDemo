package com.example.arunkumar.restaurantapp._adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.arunkumar.restaurantapp.R;

import java.util.ArrayList;

/**
 * Created by Arun.Kumar on 3/27/2018.
 */

public class RestImageAdapter extends RecyclerView.Adapter<RestImageAdapter.MyHolder> {
    ArrayList<String> mList;
    Context mContext;

    public RestImageAdapter(ArrayList<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RestImageAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rest_image_view, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestImageAdapter.MyHolder holder, int position) {
        Glide.with(mContext).load(mList.get(position)).into(holder.mImageViewRest);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView mImageViewRest;

        public MyHolder(View itemView) {
            super(itemView);
            mImageViewRest = itemView.findViewById(R.id.ivRestaurant);
        }
    }
}
