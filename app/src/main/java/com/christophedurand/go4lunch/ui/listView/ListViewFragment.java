package com.christophedurand.go4lunch.ui.listView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.RestaurantDetailsActivity;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.christophedurand.go4lunch.ui.HomeActivity.LAUNCH_SECOND_ACTIVITY;
import static com.christophedurand.go4lunch.ui.RestaurantDetailsActivity.BUNDLE_EXTRA_RESTAURANT;

/**
 * A fragment representing a list of Restaurants.
 */
public class ListViewFragment extends Fragment implements ListRestaurantsInterface {

    //-- PROPERTIES
    //private List<Restaurant> mRestaurants;
    private List<PlaceLikelihood> mRestaurants;
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationClient;
    private RecyclerView mRecyclerView;
    private MyRestaurantRecyclerViewAdapter myRestaurantRecyclerViewAdapter;
    private Context context;


    //-- INIT
    public static ListViewFragment newInstance() {
        return new ListViewFragment();
    }


    //-- VIEW LIFE CYCLE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        context = view.getContext();

        // Initialize the SDK
        String apiKey = "AIzaSyD6FndQ_yMQLDPOYVzaeLt1rIuJ72Ntg_M";
        Places.initialize(context.getApplicationContext(), apiKey);

        // Create a new PlacesClient instance
        placesClient = Places.createClient(requireActivity());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        mRestaurants = new ArrayList<>();

        mRecyclerView = (RecyclerView) view;

        initList();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
    }


    //-- METHODS
    /**
     * Init the List of restaurants
     */
    private void initList() {
        getRestaurants();

    }

    public void updateList(List<PlaceLikelihood> restaurants) {
        mRestaurants = restaurants;
        myRestaurantRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void getRestaurants() {
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.BUSINESS_STATUS, Place.Field.TYPES);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            placesClient.findCurrentPlace(request).addOnSuccessListener(((response) -> {

                for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {

                    Place place = placeLikelihood.getPlace();

                    if (Objects.requireNonNull(place.getTypes()).contains(Place.Type.RESTAURANT)) {
//                        String restaurantId = place.getId();
//                        String restaurantName = place.getName();
//                        String restaurantAddress = place.getAddress();
//                        String restaurantBusinessStatus = String.valueOf(place.getBusinessStatus());

                        mRestaurants.add(placeLikelihood);
                    }
                }
                myRestaurantRecyclerViewAdapter = new MyRestaurantRecyclerViewAdapter(mRestaurants, this);
                mRecyclerView.setAdapter(myRestaurantRecyclerViewAdapter);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

            })).addOnFailureListener((exception) -> {
                if (exception instanceof ApiException) {
                    ApiException apiException = (ApiException) exception;
                    Log.e("TAG", "Place not found: " + apiException.getStatusCode());
                }
            });
        }
    }

    @Override
    public void onClickRestaurant(PlaceLikelihood restaurant) {
//        Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
//
//        intent.putExtra(BUNDLE_EXTRA_RESTAURANT, restaurant);
//
//        if (getActivity() != null)
//            getActivity().startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);

    }

}