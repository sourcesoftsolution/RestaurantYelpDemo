package com.example.arunkumar.restaurantapp._utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.arunkumar.restaurantapp.R;
import com.example.arunkumar.restaurantapp._model.RestModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

/**
 * Created by Arun.Kumar on 3/28/2018.
 */

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private Activity context;
    Context mContext;

    public CustomInfoWindowGoogleMap(Activity ctx, Context mContext) {
        context = ctx;
        this.mContext = mContext;
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater()
                .inflate(R.layout.info_window, null);

        TextView mTextViewAddres = view.findViewById(R.id.name);
        RatingBar mRatingBar = view.findViewById(R.id.ratingBar);
        ImageView imageView = view.findViewById(R.id.pic);
        final RestModel restModel = (RestModel) marker.getTag();
        Picasso.get().load(restModel.getmRestImage()).into(imageView,new InfoWindowRefresher(marker,restModel.getmRestImage(),imageView,mContext));
        mTextViewAddres.setText(restModel.getmRestAddress());
        mRatingBar.setRating(restModel.getRating());
        return view;
    }
}
