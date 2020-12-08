package model;


import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;


public class RestaurantDataAsyncTask extends AsyncTask<Void, Void, Void> {

    //-- PROPERTIES
    private final Marker marker;
    private final String placeId;
    private final PlacesClient placesClient;


    //-- CONSTRUCTOR
    public RestaurantDataAsyncTask(Marker marker, String placeId, PlacesClient placesClient) {
        this.marker = marker;
        this.placeId = placeId;
        this.placesClient = placesClient;
    }


    //-- METHODS
    @Override
    protected void onPreExecute() { }

    @Override
    protected Void doInBackground(Void... voids) {
        // Specify the fields to return.
        List<Place.Field> placeFieldsById = Arrays.asList(
                Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI, Place.Field.BUSINESS_STATUS,
                Place.Field.RATING, Place.Field.PHOTO_METADATAS
        );

        // Construct a request object, passing the place ID and fields array.
        assert placeId != null;
        FetchPlaceRequest requestId = FetchPlaceRequest.builder(Objects.requireNonNull(placeId), placeFieldsById).build();

        // FETCH RESTAURANT DETAILS BY ID
        // Add a listener to handle the response.
        placesClient.fetchPlace(requestId).addOnSuccessListener((response) -> {

            Place place = response.getPlace();

            Log.i("TAG", "Place found: " + place.getId());
            Log.i("TAG", "Place found: " + place.getName());
            Log.i("TAG", "Place found: " + place.getAddress());

            marker.setTitle(place.getName());
            marker.setSnippet(place.getAddress());

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                Log.e("TAG", "Place not found: " + exception.getMessage());
            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (marker != null && marker.isInfoWindowShown()) {
            marker.showInfoWindow();
        }
    }
}
