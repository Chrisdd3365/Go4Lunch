package com.christophedurand.go4lunch.ui.listView;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.ui.listView.dummy.RestaurantContent.Restaurant;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Restaurant}.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Restaurant> mRestaurantsList;

    public MyItemRecyclerViewAdapter(List<Restaurant> items) {
        mRestaurantsList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mRestaurantItem = mRestaurantsList.get(position);
        holder.mRestaurantNameTextView.setText(mRestaurantsList.get(position).name);
        holder.mRestaurantAddressTextView.setText(mRestaurantsList.get(position).address);
        holder.mRestaurantBusinessStatus.setText(mRestaurantsList.get(position).businessStatus);
    }

    @Override
    public int getItemCount() {
        return mRestaurantsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mRestaurantNameTextView;
        public final TextView mRestaurantAddressTextView;
        public final TextView mRestaurantBusinessStatus;
        public Restaurant mRestaurantItem;

        public ViewHolder(View view) {
            super(view);
            mRestaurantNameTextView = view.findViewById(R.id.restaurant_name);
            mRestaurantAddressTextView = view.findViewById(R.id.restaurant_address);
            mRestaurantBusinessStatus = view.findViewById(R.id.restaurant_business_status);
        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}