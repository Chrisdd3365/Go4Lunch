package com.christophedurand.go4lunch.ui.listView;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.detailsView.RestaurantDetailsActivity;


public class ListFragment extends Fragment implements ListInterface {

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SearchView searchView = (SearchView) menu.findItem(R.id.search_item).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                listViewModel.onSearchButtonClicked(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listViewModel.onSearchButtonClicked(newText);
                return false;
            }
        });
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