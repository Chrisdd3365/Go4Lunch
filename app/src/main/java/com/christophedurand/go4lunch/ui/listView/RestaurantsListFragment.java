package com.christophedurand.go4lunch.ui.listView;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.RestaurantDetailsActivity;
import viewModel.NearbyRestaurantsViewModel;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import model.pojo.Restaurant;
import viewModel.ViewModelFactory;

import static com.christophedurand.go4lunch.ui.HomeActivity.LAUNCH_SECOND_ACTIVITY;
import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;
import static com.christophedurand.go4lunch.ui.RestaurantDetailsActivity.BUNDLE_EXTRA_RESTAURANT;


/**
 * A fragment representing a list of Restaurants.
 */
public class RestaurantsListFragment extends Fragment implements RestaurantsListInterface {

    private List<Restaurant> mRestaurants;
    private RecyclerView mRecyclerView;
    private RestaurantsListRecyclerViewAdapter restaurantsListRecyclerViewAdapter;
    private Context context;
    private NearbyRestaurantsViewModel mNearbyRestaurantsViewModel;


    public static RestaurantsListFragment newInstance(Location location) {
        RestaurantsListFragment fragment = new RestaurantsListFragment();

        Bundle args = new Bundle();
        args.putSerializable("location", (Serializable) location);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        context = view.getContext();

        ViewModelFactory mViewModelFactory = ViewModelFactory.getInstance();
        mNearbyRestaurantsViewModel = new ViewModelProvider(this, mViewModelFactory).get(NearbyRestaurantsViewModel.class);

        androidx.lifecycle.Observer<Location> locationObserver = location ->
                mNearbyRestaurantsViewModel.getNearbyRestaurantsRepository("restaurant", location, "1000", apiKey)
                        .observe(this.getViewLifecycleOwner(), nearbyRestaurantsResponse -> {

                            mRecyclerView = (RecyclerView) view;
                            mRestaurants = nearbyRestaurantsResponse.getResults();
                            restaurantsListRecyclerViewAdapter = new RestaurantsListRecyclerViewAdapter(mRestaurants,
                                    RestaurantsListFragment.this, mNearbyRestaurantsViewModel, location);
                            mRecyclerView.setAdapter(restaurantsListRecyclerViewAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            mRecyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
                        });
        mNearbyRestaurantsViewModel.locationMediatorLiveData.observe(this.getViewLifecycleOwner(), locationObserver);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    //-- METHODS
    public void updateList(List<Restaurant> restaurants) {
        mRestaurants = restaurants;
        restaurantsListRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickRestaurant(Restaurant restaurant) {
        Intent intent = new Intent(getActivity(), RestaurantDetailsActivity.class);
        intent.putExtra(BUNDLE_EXTRA_RESTAURANT, (Parcelable) restaurant);

        if (getActivity() != null) {
            getActivity().startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY);
        }
    }

}