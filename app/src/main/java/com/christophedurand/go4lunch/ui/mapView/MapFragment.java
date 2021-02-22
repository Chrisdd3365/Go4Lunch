package com.christophedurand.go4lunch.ui.mapView;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.christophedurand.go4lunch.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import model.repository.NearbyRestaurantsViewModel;
import viewModel.ViewModelFactory;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    private GoogleMap mMap;
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
        configureViewModel();

        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = ViewModelFactory.getInstance();
        mNearbyRestaurantsViewModel = new ViewModelProvider(this, mViewModelFactory).get(NearbyRestaurantsViewModel.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mNearbyRestaurantsViewModel.setOnMapReady(mMap, apiKey, getContext(), getActivity(), this);
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

}
