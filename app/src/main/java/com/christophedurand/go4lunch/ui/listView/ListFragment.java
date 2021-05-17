package com.christophedurand.go4lunch.ui.listView;

import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.model.pojo.Restaurant;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetails;
import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class ListFragment extends Fragment {

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

        configureViewModel();

        listViewModel.getListViewStateMediatorLiveData().observe(getViewLifecycleOwner(), listViewState -> {
            initRecyclerView(listViewState.getLocation(), listViewState.getRestaurantsList(), listViewState.getMap());
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    private void configureViewModel() {
        ListViewModelFactory listViewModelFactory = ListViewModelFactory.getInstance();
        listViewModel = new ViewModelProvider(this, listViewModelFactory).get(ListViewModel.class);
    }

    private void initRecyclerView(Location currentLocation,
                                  List<Restaurant> restaurantsList,
                                  Map<String, RestaurantDetailsResponse> map) {

        final LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        listRecyclerView.setLayoutManager(layoutManager);
        listRecyclerView.setItemAnimator(new DefaultItemAnimator());
        listRecyclerView.setClickable(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        listRecyclerView.addItemDecoration(dividerItemDecoration);

        ListRecyclerViewAdapter listRecyclerViewAdapter = new ListRecyclerViewAdapter(
                requireActivity(),
                currentLocation,
                restaurantsList,
                map
        );
        listRecyclerView.setAdapter(listRecyclerViewAdapter);
    }

}