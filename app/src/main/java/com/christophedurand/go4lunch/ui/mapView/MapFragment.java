package com.christophedurand.go4lunch.ui.mapView;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.christophedurand.go4lunch.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    //-- PROPERTIES
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private Context context;


    //-- INIT
    /**
     * Create and return a new instance
     * @return @{@link MapFragment}
     */
    public static MapFragment newInstance() {
        return new MapFragment();
    }


    //-- VIEW LIFE CYCLE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

        context = view.getContext();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Initialize the SDK
        String apiKey = "AIzaSyD6FndQ_yMQLDPOYVzaeLt1rIuJ72Ntg_M";
        Places.initialize(context.getApplicationContext(), apiKey);

        // Create a new PlacesClient instance
        placesClient = Places.createClient(requireActivity());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    //-- METHODS
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener((Activity) context, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        LatLng currentLocation = new LatLng( location.getLatitude(), location.getLongitude() );

                        // Display a blue dot to represent the current user location
                        mMap.setMyLocationEnabled(true);

                        // Move the camera instantly to current location with a zoom of 15.
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                        // Zoom in, animating the camera.
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());

                        // Construct a CameraPosition focusing on the view and animate the camera to that position.
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(currentLocation)    // Sets the center of the map to current Location
                                .zoom(17)                   // Sets the zoom
                                .bearing(90)                // Sets the orientation of the camera to east
                                .tilt(0)                    // Sets the tilt of the camera to 0 degree
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        // Display all restaurants near current user location
                        getRestaurants(mMap);

                    }
                });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void getRestaurants(GoogleMap googleMap) {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.TYPES);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            placesClient.findCurrentPlace(request).addOnSuccessListener(((response) -> {
                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {

                    Place place = placeLikelihood.getPlace();

                    if (Objects.requireNonNull(place.getTypes()).contains(Place.Type.RESTAURANT)) {
                        LatLng restaurantLatLng = place.getLatLng();

                        assert restaurantLatLng != null;
                        Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position(restaurantLatLng)
                                .icon(bitmapDescriptorFromVector(requireActivity(), R.drawable.ic_restaurant_red_marker))
                        );

                        marker.setTag(place.getId());

                        mMap.setInfoWindowAdapter(new RestaurantInfoAdapter());
                    }
                }
            })).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e("TAG", "Place not found: " + apiException.getStatusCode());
                }
            });
        }

    }

    private class RestaurantInfoAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mRestaurantInfoView;


        public RestaurantInfoAdapter() {
            mRestaurantInfoView = requireActivity().getLayoutInflater().inflate(R.layout.info_window_restaurant, null);
        }


        @Override
        public View getInfoWindow(Marker marker) {
            // Define a Place ID.
            String placeId = (String) marker.getTag();

            // Specify the fields to return.
            List<Place.Field> placeFieldsById = Arrays.asList(
                    Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI, Place.Field.BUSINESS_STATUS,
                    Place.Field.RATING, Place.Field.PHOTO_METADATAS
            );

            // Construct a request object, passing the place ID and fields array.
            assert placeId != null;
            FetchPlaceRequest requestId = FetchPlaceRequest.builder(Objects.requireNonNull(placeId), placeFieldsById).build();

            // Init Card View UI
            TextView nameTextView = mRestaurantInfoView.findViewById(R.id.name_text_view);
            TextView descriptionTextView = mRestaurantInfoView.findViewById(R.id.address_text_view);
            ImageView imageView = mRestaurantInfoView.findViewById(R.id.restaurant_image_view);
            ProgressBar progressBar = mRestaurantInfoView.findViewById(R.id.progress_bar);

            // FETCH RESTAURANT DETAILS BY ID
            // Add a listener to handle the response.
            placesClient.fetchPlace(requestId).addOnSuccessListener((response) -> {

                Place place = response.getPlace();

                Log.i("TAG", "Place found: " + place.getId());
                Log.i("TAG", "Place found: " + place.getName());
                Log.i("TAG", "Place found: " + place.getAddress());

                nameTextView.setText(place.getName());
                descriptionTextView.setText(place.getAddress());

                // Get the photo metadata.
                PhotoMetadata photoMetadata = Objects.requireNonNull(place.getPhotoMetadatas()).get(0);

                // Create a FetchPhotoRequest.
                FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500) // Optional.
                        .setMaxHeight(300) // Optional.
                        .build();

                // FETCH RESTAURANT PHOTO
                placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {

                    Bitmap bitmap = fetchPhotoResponse.getBitmap();
                    imageView.setImageBitmap(bitmap);

                    progressBar.setVisibility(View.GONE);

                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        Log.e("TAG", "Place not found: " + exception.getMessage());
                        progressBar.setVisibility(View.VISIBLE);
                    }
                });

            }).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    Log.e("TAG", "Place not found: " + exception.getMessage());
                    progressBar.setVisibility(View.VISIBLE);
                }
            });


            return mRestaurantInfoView;
        }


        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

    }


    //-- HELPER'S METHODS
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}