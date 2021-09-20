package com.christophedurand.go4lunch.ui.workmatesView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.detailsView.RestaurantDetailsActivity;
import com.christophedurand.go4lunch.ui.listView.ListInterface;


public class WorkmatesFragment extends Fragment implements ListInterface {

    private RecyclerView workmatesRecyclerView;

    private WorkmatesRecyclerViewAdapter workmatesRecyclerViewAdapter;

    private WorkmatesViewModel workmatesViewModel;


    public static WorkmatesFragment newInstance() {
        return new WorkmatesFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_workmates_list, container, false);

        workmatesRecyclerView = view.findViewById(R.id.workmates_list_recycler_view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        workmatesRecyclerView.setLayoutManager(layoutManager);
        workmatesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        workmatesRecyclerView.setClickable(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        workmatesRecyclerView.addItemDecoration(dividerItemDecoration);

        configureViewModel();
        subscribe();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (workmatesRecyclerViewAdapter != null) {
            workmatesRecyclerViewAdapter.startListening();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        workmatesRecyclerViewAdapter.stopListening();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        workmatesViewModel.onCleared();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
    }


    @Override
    public void onClickItemList(String restaurantId) {
        Intent intent = new Intent(requireActivity(), RestaurantDetailsActivity.class);
        intent.putExtra("restaurantId", restaurantId);
        startActivity(intent);
    }


    private void configureViewModel() {
        WorkmatesViewModelFactory workmatesViewModelFactory = WorkmatesViewModelFactory.getInstance(getViewLifecycleOwner());
        workmatesViewModel = new ViewModelProvider(this, workmatesViewModelFactory).get(WorkmatesViewModel.class);
    }

    private void subscribe() {
        workmatesViewModel.getWorkmatesViewStateMediatorLiveData().observe(getViewLifecycleOwner(), workmatesViewState -> {
            workmatesRecyclerViewAdapter = new WorkmatesRecyclerViewAdapter(this, workmatesViewState.getWorkmatesList());
            workmatesRecyclerViewAdapter.startListening();
            workmatesRecyclerView.setAdapter(workmatesRecyclerViewAdapter);
        });
    }

}