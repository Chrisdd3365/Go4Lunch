package com.christophedurand.go4lunch.ui.listView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.detailsView.RestaurantDetailsActivity;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ListFragment extends Fragment implements ListInterface {

    final static int AUTOCOMPLETE_REQUEST_CODE = 1;


    private RecyclerView listRecyclerView;

    private ListViewModel listViewModel;


    public static ListFragment newInstance() {
        return new ListFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurants_list, container, false);

        listRecyclerView = view.findViewById(R.id.restaurants_list_recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        listRecyclerView.setLayoutManager(layoutManager);
        listRecyclerView.setItemAnimator(new DefaultItemAnimator());
        listRecyclerView.setClickable(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        listRecyclerView.addItemDecoration(dividerItemDecoration);

        ListRecyclerViewAdapter listRecyclerViewAdapter = new ListRecyclerViewAdapter(this);
        listRecyclerView.setAdapter(listRecyclerViewAdapter);

        configureViewModel();

        listViewModel.getListViewStateMediatorLiveData().observe(getViewLifecycleOwner(), listViewState -> {
            listRecyclerViewAdapter.setNewData(listViewState.getRestaurantViewStatesList());
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                listViewModel.getRestaurantQuery(place.getName());
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

    @Override
    public void onClickItemList(String restaurantId) {
        Intent intent = new Intent(requireActivity(), RestaurantDetailsActivity.class);
        intent.putExtra("restaurantId", restaurantId);
        startActivity(intent);
    }


    private void configureViewModel() {
        ListViewModelFactory listViewModelFactory = ListViewModelFactory.getInstance();
        listViewModel = new ViewModelProvider(this, listViewModelFactory).get(ListViewModel.class);
    }

}