package com.christophedurand.go4lunch.ui.listView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
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
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onClickItemList(String placeId) {
        RestaurantDetailsActivity.startActivity(requireActivity(), placeId);
    }


    private void configureViewModel() {
        ListViewModelFactory listViewModelFactory = ListViewModelFactory.getInstance();
        listViewModel = new ViewModelProvider(this, listViewModelFactory).get(ListViewModel.class);
    }

}