package com.christophedurand.go4lunch.ui.mapView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.HomeActivity;
import com.christophedurand.go4lunch.ui.detailsView.RestaurantDetailsActivity;
import com.christophedurand.go4lunch.ui.workmatesView.User;
import com.christophedurand.go4lunch.ui.workmatesView.UserManager;
import com.christophedurand.go4lunch.utils.Utils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    final static int AUTOCOMPLETE_REQUEST_CODE = 1;


    private ConstraintLayout _mapItemParentConstraintLayout;
    private ImageView _mapItemImageView;
    private TextView _mapItemNameTextView;
    private TextView _mapItemAddressTextView;


    private MapViewModel mMapViewModel;

    private Map<Object, MapMarker> _markerHashMap = new HashMap<>();


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

        initMarkerDetailsView(view);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        configureViewModel();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        ActivityCompat.requestPermissions(
                requireActivity(),
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0
        );

        HomeActivity homeActivity = (HomeActivity) requireActivity();
        if (homeActivity.toolbar != null) {
            homeActivity.toolbar.setTitle(R.string.hungry_title);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapViewModel.refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapViewModel.getMapViewStateLiveData().removeObservers(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                _mapItemParentConstraintLayout.setVisibility(View.GONE);

                Place place = Autocomplete.getPlaceFromIntent(data);
                mMapViewModel.getQueriedRestaurant(place.getName());
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_item:
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(
                        Place.Field.NAME,
                        Place.Field.ID,
                        Place.Field.ADDRESS,
                        Place.Field.LAT_LNG,
                        Place.Field.PHOTO_METADATAS);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .setCountries(Arrays.asList("FR"))
                        .build(requireContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
                return true;
            default:
                return false;
        }
    }

    private void onMapMarkerClicked(GoogleMap googleMap) {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                _mapItemParentConstraintLayout.setVisibility(View.VISIBLE);
                setMarkerDetailsView(marker.getTag());
                return false;
            }
        });
    }

    private void onMapMarkerDetailsClicked(String restaurantId) {
        _mapItemParentConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), RestaurantDetailsActivity.class);
                intent.putExtra("restaurantId", restaurantId);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        setOnMapReady(googleMap);
        onMapMarkerClicked(googleMap);
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
        MapViewModelFactory mapViewModelFactory = MapViewModelFactory.getInstance();
        mMapViewModel = new ViewModelProvider(this, mapViewModelFactory).get(MapViewModel.class);
    }

    private void setOnMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            List<MapMarker> updatedMarkersList = new ArrayList<>();

            mMapViewModel.getMapViewStateLiveData().observe(this.getViewLifecycleOwner(), mapViewState -> {

                googleMap.clear();
                updatedMarkersList.clear();
                updatedMarkersList.addAll(mapViewState.getMapMarkersList());

                LatLng currentLocation = new LatLng(mapViewState.getLocation().getLatitude(),
                        mapViewState.getLocation().getLongitude());

                // Display a blue dot to represent the current user location
                googleMap.setMyLocationEnabled(true);

                // Move the camera instantly to current location with a zoom of 15.
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                // Zoom in, animating the camera.
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());

                // Construct a CameraPosition focusing on the view and animate the camera to that position.
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentLocation)    // Sets the center of the map to current RestaurantLocation
                        .zoom(15)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(0)                    // Sets the tilt of the camera to 0 degree
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                // Display all restaurants near current user location
                markNearbyRestaurants(googleMap, updatedMarkersList);
            });
        }
    }

    private void markNearbyRestaurants(GoogleMap googleMap, List<MapMarker> mapMarkersList) {
        if (mapMarkersList != null) {
            for (MapMarker mapMarker : mapMarkersList) {
                String restaurantId = mapMarker.getPlaceId();
                LatLng restaurantLatLng = mapMarker.getLatLng();

                UserManager.getInstance().getAllUsers().get().addOnSuccessListener(queryDocumentSnapshots -> {
                    List<DocumentSnapshot> workmatesList = queryDocumentSnapshots.getDocuments();
                    int numberOfWorkmates = 0;
                    for (DocumentSnapshot workmate : workmatesList) {
                        User user = workmate.toObject(User.class);
                        if (user.getRestaurant() != null
                                && user.getRestaurant().getId() != null
                                && user.getRestaurant().getId().equals(restaurantId)) {
                            numberOfWorkmates += 1;
                        }
                    }
                    Marker marker = googleMap.addMarker(
                            new MarkerOptions()
                                    .position(restaurantLatLng)
                                    .icon(Utils.bitmapDescriptorFromVector(getActivity(), Utils.getResId(setMapMarkerIcon(numberOfWorkmates), R.drawable.class)))
                    );
                    marker.setTag(restaurantId);
                    _markerHashMap.put(marker.getTag(), mapMarker);
                });
            }
        }
    }

    private void initMarkerDetailsView(View rootView) {
        _mapItemParentConstraintLayout = rootView.findViewById(R.id.map_item_parent_constraint_layout);
        _mapItemImageView = _mapItemParentConstraintLayout.findViewById(R.id.map_item_image_view);
        _mapItemNameTextView = _mapItemParentConstraintLayout.findViewById(R.id.map_item_name_text_view);
        _mapItemAddressTextView = _mapItemParentConstraintLayout.findViewById(R.id.map_item_address_text_view);
    }

    private void setMarkerDetailsView(Object markerTag) {
        String photoReference = _markerHashMap.get(markerTag).getPhotoReference();
        String photoURL = Utils.buildGooglePhotoURL(photoReference);
        Glide.with(requireContext())
                .load(photoURL)
                .into(_mapItemImageView);

        String name = _markerHashMap.get(markerTag).getName();
        _mapItemNameTextView.setText(name);

        String vicinity = _markerHashMap.get(markerTag).getVicinity();
        _mapItemAddressTextView.setText(vicinity);

        onMapMarkerDetailsClicked(_markerHashMap.get(markerTag).getPlaceId());
    }

    private String setMapMarkerIcon(int numberOfWorkmates) {
        if (numberOfWorkmates == 0) {
            return "ic_restaurant_red_marker";
        } else {
            return "ic_restaurant_green_marker";
        }
    }

}
