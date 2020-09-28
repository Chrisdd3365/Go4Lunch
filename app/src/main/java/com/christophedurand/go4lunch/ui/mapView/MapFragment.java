package com.christophedurand.go4lunch.ui.mapView;


import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.christophedurand.go4lunch.R;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Collections;
import java.util.List;

import utils.GooglePlacesCalls;
import utils.NetworkAsyncTask;


public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback, NetworkAsyncTask.Listeners {

    //-- PROPERTIES
    String apiKey = "AIzaSyB8G6oFNA-nM5rPVKk6VqPiqCOu1gFEdZY";

    PlacesClient placesClient;
    // Use fields to define the data types to return.
    List<Place.Field> placeFields = Collections.singletonList(Place.Field.TYPES);
    // Use the builder to create a FindCurrentPlaceRequest.
    FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

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
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        // Initialize the SDK
        Places.initialize(context.getApplicationContext(), apiKey);

        // Create a new PlacesClient instance
        placesClient = Places.createClient(context);

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

                        mMap.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .title("YOU"));

                        // Move the camera instantly to current location with a zoom of 15.
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                        // Zoom in, animating the camera.
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());

                        // Construct a CameraPosition focusing on Mountain View and animate the camera to that position.
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(currentLocation)    // Sets the center of the map to current Location
                                .zoom(17)                   // Sets the zoom
                                .bearing(90)                // Sets the orientation of the camera to east
                                .tilt(0)                    // Sets the tilt of the camera to 0 degree
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        executeHttpRequest(location);

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

    // ------------------
    //  HTTP REQUEST
    // ------------------
    private void executeHttpRequest(Location location) {
        GooglePlacesCalls.fetchNearbyRestaurants(context,
                location.getLatitude() + "," + location.getLongitude());
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void doInBackground() {

    }

    @Override
    public void onPostExecute(String json) {

    }


}
