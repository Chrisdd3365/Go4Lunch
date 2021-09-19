package com.christophedurand.go4lunch.ui.workmatesView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.listView.ListInterface;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class WorkmatesRecyclerViewAdapter extends FirestoreRecyclerAdapter<User, WorkmateViewHolder> {

    private final ListInterface listener;


    public WorkmatesRecyclerViewAdapter(ListInterface listener, @NonNull FirestoreRecyclerOptions<User> options) {
        super(options);

        this.listener = listener;
    }


    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate, parent, false);
        return new WorkmateViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull WorkmateViewHolder workmateViewHolder, int i, @NonNull User user) {
        workmateViewHolder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

}
