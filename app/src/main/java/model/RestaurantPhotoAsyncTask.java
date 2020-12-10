package model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.christophedurand.go4lunch.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RestaurantPhotoAsyncTask extends AsyncTask<Void, Void, Void> {

    //-- PROPERTIES
    private final String placeId;
    private final PlacesClient placesClient;
    private Bitmap bitmap;
    private final View mRestaurantInfoView;



    //-- CONSTRUCTOR
    public RestaurantPhotoAsyncTask(String placeId, PlacesClient placesClient, View mRestaurantInfoView) {
        this.placeId = placeId;
        this.placesClient = placesClient;
        this.mRestaurantInfoView = mRestaurantInfoView;
    }


    //-- METHODS
    @Override
    protected void onPreExecute() { }

    @Override
    protected Void doInBackground(Void... voids) {
        // Specify the fields to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.PHOTO_METADATAS);

        // Construct a request object, passing the place ID and fields array.
        assert placeId != null;
        FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(Objects.requireNonNull(placeId), fields).build();

        // FETCH RESTAURANT DETAILS BY ID
        // Add a listener to handle the response.
        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {

            Place place = response.getPlace();
            // Get the photo metadata.
            PhotoMetadata photoMetadata = Objects.requireNonNull(place.getPhotoMetadatas()).get(0);

            // Create a FetchPhotoRequest.
            FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500) // Optional.
                    .setMaxHeight(300) // Optional.
                    .build();

            // FETCH RESTAURANT PHOTO
            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
                bitmap = fetchPhotoResponse.getBitmap();
                ImageView restaurantImageView = mRestaurantInfoView.findViewById(R.id.restaurant_image_view);
                restaurantImageView.setImageBitmap(bitmap);

            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    Log.e("TAG", "Photo not found: " + exception.getMessage());
                }
            });

        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                Log.e("TAG", "Place not found: " + exception.getMessage());
            }
        });

        return null;
    }

    @Override
    protected void onPostExecute(Void result) { }

}
