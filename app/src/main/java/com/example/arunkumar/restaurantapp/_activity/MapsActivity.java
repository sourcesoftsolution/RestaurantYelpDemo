package com.example.arunkumar.restaurantapp._activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.arunkumar.restaurantapp._utils.Const;
import com.example.arunkumar.restaurantapp._utils.CustomInfoWindowGoogleMap;
import com.example.arunkumar.restaurantapp._utils.DialogClass;
import com.example.arunkumar.restaurantapp._location.GPSTracker;
import com.example.arunkumar.restaurantapp.R;
import com.example.arunkumar.restaurantapp._model.RestModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, View.OnClickListener {

    private static final int PERMISSION_LOCATION_REQUEST_CODE = 1111;
    private GoogleMap mMap;
    RequestQueue requestQueue;
    ImageView mImageSearch;
    ArrayList<RestModel> mRestaurantList;
    ImageView mImageViewLMyLoc;
    private GPSTracker gps;
    RestModel restModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mImageViewLMyLoc = findViewById(R.id.ivMyLocation);
        mImageSearch = findViewById(R.id.ivSearch);
        if (!checkPermission()) {
            requestPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getDataFromUrl(Const.URL);
        mImageViewLMyLoc.setOnClickListener(this);
        mImageSearch.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
    }

    private void addMarker(double latitude, double longitude, String name) {
        LatLng latLng = new LatLng(latitude, longitude);
       Marker marker= mMap.addMarker(new MarkerOptions().position(latLng).title(name));
       marker.setTag(restModel);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));


    }

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (!checkPermission()) {
            ActivityCompat.requestPermissions(
                    MapsActivity.this,
                    new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION_REQUEST_CODE);
        } else {
            Toast.makeText(MapsActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (locationAccepted1) {
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to all the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    ActivityCompat.requestPermissions(
                                                            MapsActivity.this,
                                                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
                                                            PERMISSION_LOCATION_REQUEST_CODE);
                                                }
                                            }
                                        }, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finishAffinity();
                                            }
                                        });

                                return;
                            }
                        }

                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancleListener) {
        new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", cancleListener)
                .create()
                .show();
    }

    private void getDataFromUrl(String url) {
        DialogClass.showDailog(this,"Getting Restaurants");
        mRestaurantList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectBusines = new JSONObject(response);
                    JSONArray jsonArrayBusines = jsonObjectBusines.getJSONArray(Const.businesses);
                    for (int i = 0; i < jsonArrayBusines.length(); i++) {
                        String fullAddress = "";
                        JSONObject jsonObjectMain = jsonArrayBusines.getJSONObject(i);
                        int rating=jsonObjectMain.getInt(Const.rating);
                        String id = jsonObjectMain.getString(Const.ID);
                        String image = jsonObjectMain.getString(Const.image_url);
                        String name = jsonObjectMain.getString(Const.name);
                        String displayPhone = jsonObjectMain.getString(Const.display_phone);
                        JSONObject jsonObjectLocation = jsonObjectMain.getJSONObject(Const.location);
                        JSONObject jsonObjectCoordinate = jsonObjectMain.getJSONObject(Const.coordinates);
                        JSONArray jsonArrayAddress = jsonObjectLocation.getJSONArray(Const.display_address);
                        for (int j = 0; j < jsonArrayAddress.length(); j++) {
                            fullAddress = fullAddress + jsonArrayAddress.getString(j)+" ";
                        }
                            double userLat = Double.parseDouble(jsonObjectCoordinate.getString(Const.latitude));
                        double userLong = Double.parseDouble(jsonObjectCoordinate.getString(Const.longitude));
                        restModel=new RestModel(fullAddress, image, name, displayPhone,id, userLat, userLong,rating);
                        mMap.setInfoWindowAdapter(new CustomInfoWindowGoogleMap(MapsActivity.this,getApplicationContext()));
                        addMarker(userLat, userLong, name);
                    }
                    DialogClass.dismissDailog();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DialogClass.dismissDailog();
                Toast.makeText(MapsActivity.this, " LOCATION NOT FOUND", Toast.LENGTH_SHORT).show();
                Toast.makeText(MapsActivity.this, "Could not execute search, try specifying a more exact location.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", Const.header);
                return header;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(6000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getApplicationContext(), RestaurantActivity.class);
        RestModel restModel = (RestModel) marker.getTag();
        intent.putExtra(Const.name, restModel.getmRestName());
        intent.putExtra(Const.display_address, restModel.getmRestAddress());
        intent.putExtra(Const.display_phone, restModel.getmRestPhoneNo());
        intent.putExtra(Const.ID, restModel.getmRestId());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivMyLocation:
                gps = new GPSTracker(MapsActivity.this);
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Geocoder geocoder;
                    List<Address> addresses;
                    geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                    try {
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        String city = addresses.get(0).getLocality();
                        mMap.clear();
                        getDataFromUrl(Const.SEARCH_URL+city);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    gps.showSettingsAlert();
                }
                break;
            case R.id.ivSearch:
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .build(this);
                    startActivityForResult(intent, Const.PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Const.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mMap.clear();
                getDataFromUrl(Const.SEARCH_URL + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
