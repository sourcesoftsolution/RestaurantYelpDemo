package com.example.arunkumar.restaurantapp._utils;

/**
 * Created by Arun.Kumar on 3/27/2018.
 */

public interface Const {
    String BASE_URL = "https://api.yelp.com/v3/businesses/";
    String SEARCH_URL = BASE_URL+"search?location=";
    String URL = "https://api.yelp.com/v3/businesses/search?location=San Francisco";
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    String ID="id";
    String businesses="businesses";
    String image_url="image_url";
    String name="name";
    String display_phone="display_phone";
    String location="location";
    String coordinates="coordinates";
    String display_address="display_address";
    String latitude="latitude";
    String longitude="longitude";
    String photos="photos";
    String category="categories";
    String rating="rating";
    String header="Bearer CKa8wWNL9tSQbd9HQXN747uG5nRa4OAsTS" +
            "nsozn2gdY1be0IMMuL1PmN219p1MB9rHOqIV6EyU-RIa2m7gNqfR1j" +
            "qFyjiXRfaV5GnznqyUMBk2tMEnIDaEWItb24WnYx";
}
