package com.christophedurand.go4lunch.ui.mapView;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import model.pojo.NearbyRestaurantsResponse;
import model.pojo.Restaurant;
import viewModel.NearbyRestaurantsViewModel;
import viewModel.ViewModelFactory;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    private NearbyRestaurantsViewModel mNearbyRestaurantsViewModel;


    public static MapFragment newInstance() {
        return new MapFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        configureViewModel();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        setOnMapReady(googleMap, apiKey, getContext(), getActivity());
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

    public void setOnMapReady(GoogleMap mMap, String apiKey, Context context, Activity activity) {

        String radius = "1000";
        String type = "restaurant";

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            androidx.lifecycle.Observer<Location> locationObserver = location -> {
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

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
                mNearbyRestaurantsViewModel.getNearbyRestaurantsRepository(type, location, radius, apiKey)
                        .observe(this.getViewLifecycleOwner(), nearbyRestaurantsResponse ->
                                markNearbyRestaurant(nearbyRestaurantsResponse, mMap, apiKey, activity));
            };
            mNearbyRestaurantsViewModel.locationMediatorLiveData.observe(this, locationObserver);


        }
    }

    private void markNearbyRestaurant(NearbyRestaurantsResponse nearbyRestaurantResponse, GoogleMap mMap, String apiKey,
                                      Activity activity) {
        List<Restaurant> mNearbyRestaurantsList = nearbyRestaurantResponse.getResults();
        for (Restaurant nearbyRestaurant : mNearbyRestaurantsList) {
            LatLng restaurantLatLng = new LatLng(nearbyRestaurant.getGeometry().getLocation().lat,
                    nearbyRestaurant.getGeometry().getLocation().lng);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(restaurantLatLng)
                    .icon(mNearbyRestaurantsViewModel.bitmapDescriptorFromVector(activity, R.drawable.ic_restaurant_red_marker))
            );
            marker.setTag(nearbyRestaurant.getPlaceId());

            setNearbyRestaurantInfoWindowAdapter(mMap, apiKey, activity);
        }
    }

    private void setNearbyRestaurantInfoWindowAdapter(GoogleMap mMap, String apiKey, Activity activity) {
        mMap.setInfoWindowAdapter(new RestaurantInfoAdapter(
                activity.getLayoutInflater().inflate(R.layout.info_window_restaurant,
                        null), apiKey, getViewLifecycleOwner(), this));
    }

}
