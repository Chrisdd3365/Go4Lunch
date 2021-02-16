package com.christophedurand.go4lunch.ui.mapView;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.R;
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

import java.util.List;

import model.pojo.NearbyRestaurantsResponse;
import model.pojo.Restaurant;
import viewModel.NearbyRestaurantsViewModel;
import viewModel.ViewModelFactory;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    //-- PROPERTIES
    private final String apiKey = "AIzaSyD6FndQ_yMQLDPOYVzaeLt1rIuJ72Ntg_M";
    private GoogleMap mMap;
    private Context context;
    private NearbyRestaurantsViewModel mNearbyRestaurantsViewModel;

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

        configureViewModel();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        Places.initialize(context.getApplicationContext(), apiKey);

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

        mNearbyRestaurantsViewModel.setOnMapReady(mMap, apiKey, getContext(), getActivity(), this);

//        String radius = "1000";
//        String type = "restaurant";
//
//        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context,
//                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationClient.getLastLocation()
//                    .addOnSuccessListener((Activity) context, location -> {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//
//                            // Display a blue dot to represent the current user location
//                            mMap.setMyLocationEnabled(true);
//
//                            // Move the camera instantly to current location with a zoom of 15.
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
//
//                            // Zoom in, animating the camera.
//                            mMap.animateCamera(CameraUpdateFactory.zoomIn());
//
//                            // Construct a CameraPosition focusing on the view and animate the camera to that position.
//                            CameraPosition cameraPosition = new CameraPosition.Builder()
//                                    .target(currentLocation)    // Sets the center of the map to current Location
//                                    .zoom(17)                   // Sets the zoom
//                                    .bearing(90)                // Sets the orientation of the camera to east
//                                    .tilt(0)                    // Sets the tilt of the camera to 0 degree
//                                    .build();                   // Creates a CameraPosition from the builder
//                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//                            // Display all restaurants near current user location
//                            mNearbyRestaurantsViewModel.getNearbyRestaurantsRepository(type,
//                                    currentLocation.latitude + "," + currentLocation.longitude, radius, apiKey).
//                                    observe(getViewLifecycleOwner(), this::markNearbyRestaurant);
//                        }
//                    });
//        }

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

    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = ViewModelFactory.getInstance();
        mNearbyRestaurantsViewModel = new ViewModelProvider(this, mViewModelFactory).get(NearbyRestaurantsViewModel.class);
    }

//    private void markNearbyRestaurant(NearbyRestaurantsResponse nearbyRestaurantResponse) {
//        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            List<Restaurant> mNearbyRestaurantsList = nearbyRestaurantResponse.getResults();
//            for (Restaurant nearbyRestaurant : mNearbyRestaurantsList) {
//                LatLng restaurantLatLng = new LatLng(nearbyRestaurant.getGeometry().getLocation().lat,
//                        nearbyRestaurant.getGeometry().getLocation().lng);
//                Marker marker = mMap.addMarker(new MarkerOptions()
//                        .position(restaurantLatLng)
//                        .icon(bitmapDescriptorFromVector(requireActivity(),
//                                R.drawable.ic_restaurant_red_marker))
//                );
//                marker.setTag(nearbyRestaurant.getPlaceId());
//
//                setNearbyRestaurantInfoWindowAdapter();
//            }
//        }
//    }
//
//    private void setNearbyRestaurantInfoWindowAdapter() {
//        mMap.setInfoWindowAdapter(new RestaurantInfoAdapter(
//                requireActivity().getLayoutInflater().inflate(R.layout.info_window_restaurant,
//                        null), apiKey, getViewLifecycleOwner()));
//    }
//
//
//    //-- HELPER'S METHODS
//    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
//        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
//
//        assert vectorDrawable != null;
//        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
//
//        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//
//        vectorDrawable.draw(canvas);
//
//        return BitmapDescriptorFactory.fromBitmap(bitmap);
//    }

}
