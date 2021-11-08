package com.christophedurand.go4lunch.ui.workmatesView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.model.User;
import com.christophedurand.go4lunch.ui.listView.ListInterface;

import java.util.ArrayList;
import java.util.List;


public class WorkmatesRecyclerViewAdapter extends RecyclerView.Adapter<WorkmateViewHolder> {

    private final List<User> usersList = new ArrayList<>();

    private final ListInterface listener;


    public WorkmatesRecyclerViewAdapter(ListInterface listener) {
        this.listener = listener;
    }


    @NonNull
    @Override
    public WorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workmate, parent, false);
        return new WorkmateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.bind(user, listener);
    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public void setNewData(List<User> newList) {
        usersList.clear();
        usersList.addAll(newList);
        notifyDataSetChanged();
    }

}
