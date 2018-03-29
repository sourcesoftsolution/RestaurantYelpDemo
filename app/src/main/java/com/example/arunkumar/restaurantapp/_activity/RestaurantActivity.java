package com.example.arunkumar.restaurantapp._activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.example.arunkumar.restaurantapp._utils.DialogClass;
import com.example.arunkumar.restaurantapp.R;
import com.example.arunkumar.restaurantapp._adapter.RestImageAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantActivity extends AppCompatActivity {
    TextView mTextViewName, mTextViewAddress, mTextViewPhoneNo,mTextViewCategory;
    RatingBar mRatingBar;
    private RequestQueue requestQueue;
    private ArrayList<String> mPhotoList;
    RecyclerView mRestaurantImageRecycler;
    StringBuilder builderAlias;
    ImageView mImageViewBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        DialogClass.showDailog(this,"Getting Restaurants Details");
        mTextViewCategory=findViewById(R.id.restCategory);
        mImageViewBack=findViewById(R.id.ivBack);
        mRatingBar=findViewById(R.id.ratingBar);
        getRestImage(Const.BASE_URL + getIntent().getStringExtra(Const.ID));
        mRestaurantImageRecycler = findViewById(R.id.restImageRecycler);
        mTextViewName = findViewById(R.id.restName);
        mTextViewAddress = findViewById(R.id.restAddress);
        mTextViewPhoneNo = findViewById(R.id.restPhoneNo);
        mTextViewName.setText(getIntent().getStringExtra(Const.name));
        mTextViewAddress.setText(getIntent().getStringExtra(Const.display_address));
        mTextViewPhoneNo.setText(getIntent().getStringExtra(Const.display_phone));
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getRestImage(String url) {
        mPhotoList = new ArrayList<>();
         builderAlias=new StringBuilder();
        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectBusines = new JSONObject(response);
                    JSONArray jsonArrayPhoto = jsonObjectBusines.getJSONArray(Const.photos);
                    JSONArray jsonArrayCategory = jsonObjectBusines.getJSONArray(Const.category);
                    for (int i = 0; i < jsonArrayPhoto.length(); i++) {
                        mPhotoList.add(jsonArrayPhoto.getString(i));
                    }
                    for (int j=0;j<jsonArrayCategory.length();j++){
                        JSONObject jsonObjectAlias=jsonArrayCategory.getJSONObject(j);
                        builderAlias.append(jsonObjectAlias.getString("title"));
                    }
                    mTextViewCategory.setText(builderAlias);
                    mRatingBar.setRating(jsonObjectBusines.getInt(Const.rating));
                    mRatingBar.setClickable(false);
                    setRestaurantImage(mPhotoList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), " Volley error :-" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer CKa8wWNL9tSQbd9HQXN747uG5nRa4OAsTS" +
                        "nsozn2gdY1be0IMMuL1PmN219p1MB9rHOqIV6EyU-RIa2m7gNqfR1j" +
                        "qFyjiXRfaV5GnznqyUMBk2tMEnIDaEWItb24WnYx");
                return header;
            }
        };

        RetryPolicy policy = new DefaultRetryPolicy(6000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);

    }

    private void setRestaurantImage(ArrayList<String> mArrayList) {
        RestImageAdapter adapter = new RestImageAdapter(mArrayList, this);
        mRestaurantImageRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRestaurantImageRecycler.setAdapter(adapter);
        DialogClass.dismissDailog();

    }
}


