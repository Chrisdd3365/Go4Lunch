package com.christophedurand.go4lunch.ui.mapView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.model.User;
import com.christophedurand.go4lunch.model.UserManager;
import com.christophedurand.go4lunch.ui.detailsView.RestaurantDetailsActivity;
import com.christophedurand.go4lunch.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    private MapViewModel mMapViewModel;


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

//        AutocompleteSupportFragment _autoCompleteFragment = (AutocompleteSupportFragment) this.getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//        if (_autoCompleteFragment != null) {
//            _autoCompleteFragment.setPlaceFields(Collections.singletonList(Place.Field.NAME));
//            _autoCompleteFragment.setOnPlaceSelectedListener(this);
//        }

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
        setOnMapReady(googleMap);
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
        MapViewModelFactory mMapViewModelFactory = MapViewModelFactory.getInstance("this");
        mMapViewModel = new ViewModelProvider(this, mMapViewModelFactory).get(MapViewModel.class);
    }

    private void setOnMapReady(GoogleMap mMap) {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMapViewModel.getMapViewStateLiveData().observe(this.getViewLifecycleOwner(), mapViewState -> {

                LatLng currentLocation = new LatLng(mapViewState.getLocation().getLatitude(),
                        mapViewState.getLocation().getLongitude());

                // Display a blue dot to represent the current user location
                mMap.setMyLocationEnabled(true);

                // Move the camera instantly to current location with a zoom of 15.
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                // Zoom in, animating the camera.
                mMap.animateCamera(CameraUpdateFactory.zoomIn());

                // Construct a CameraPosition focusing on the view and animate the camera to that position.
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentLocation)    // Sets the center of the map to current RestaurantLocation
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(0)                    // Sets the tilt of the camera to 0 degree
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // Display all restaurants near current user location
                markNearbyRestaurant(mapViewState, mMap);
            });
        }
    }

    private void markNearbyRestaurant(MapViewState mapViewState, GoogleMap mMap) {
        if (mapViewState.getMapMarkersList() != null) {
            Map<Object, MapMarker> markerHashMap = new HashMap<>();
            for (MapMarker mapMarker : mapViewState.getMapMarkersList()) {
                String restaurantId = mapMarker.getPlaceId();
                LatLng restaurantLatLng = mapMarker.getLatLng();

                UserManager.getInstance().getAllUsers().get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> workmatesList = queryDocumentSnapshots.getDocuments();
                    int numberOfWorkmates = 0;
                    for (DocumentSnapshot workmate : workmatesList) {
                        User user = workmate.toObject(User.class);
                        if (user.getRestaurant().getId() != null && user.getRestaurant().getId().equals(restaurantId)) {
                            numberOfWorkmates += 1;
                        }
                    }
                    Marker marker = mMap.addMarker(
                            new MarkerOptions()
                                    .position(restaurantLatLng)
                                    .icon(Utils.bitmapDescriptorFromVector(getActivity(), Utils.getResId(setMapMarkerIcon(numberOfWorkmates), R.drawable.class)))
                    );
                    marker.setTag(mapMarker.getPlaceId());
                    markerHashMap.put(marker.getTag(), mapMarker);
                });
            }
            setNearbyRestaurantInfoWindowAdapter(mMap, markerHashMap);
        }
    }

    private void setNearbyRestaurantInfoWindowAdapter(GoogleMap mMap, Map<Object, MapMarker> markerMap) {
        mMap.setInfoWindowAdapter(
                new RestaurantInfoAdapter(
                        requireActivity().getLayoutInflater().inflate(R.layout.info_window_restaurant, null),
                        markerMap
                )
        );

        mMap.setOnInfoWindowClickListener(marker -> {
            Intent intent = new Intent(requireActivity(), RestaurantDetailsActivity.class);
            intent.putExtra("restaurantId", markerMap.get(marker.getTag()).getPlaceId());
            startActivity(intent);
        });
    }

    private String setMapMarkerIcon(int numberOfWorkmates) {
        if (numberOfWorkmates == 0) {
            return "ic_restaurant_red_marker";
        } else {
            return "ic_restaurant_green_marker";
        }
    }

}
