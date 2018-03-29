package com.example.arunkumar.restaurantapp._utils;

import android.content.Context;
import android.widget.ImageView;

import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Arun.Kumar on 3/28/2018.
 */

public class InfoWindowRefresher implements Callback {
    private final Marker marker;
    private final String URL;
    private final ImageView userPhoto;
    private Marker markerToRefresh;
Context mContext;

    public InfoWindowRefresher(Marker marker, String URL, ImageView userPhoto, Context mContext) {
        this.marker = marker;
        this.URL = URL;
        this.userPhoto = userPhoto;
        this.mContext = mContext;
    }

    @Override
    public void onSuccess() {
        if (marker != null && marker.isInfoWindowShown()) {
            marker.hideInfoWindow();

            Picasso.get()
                    .load(URL)
                    .into(userPhoto);
            marker.showInfoWindow();
        }
    }

    @Override
    public void onError(Exception e) {

    }


}
