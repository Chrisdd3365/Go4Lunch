package com.christophedurand.go4lunch.ui.mapView;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.utils.Utils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.Map;


public class RestaurantInfoAdapter implements GoogleMap.InfoWindowAdapter {

    private final View restaurantInfoView;
    private final Map<Object, MapMarker> markerMap;


    public RestaurantInfoAdapter(View restaurantInfoView,
                                 Map<Object, MapMarker> markerMap) {
        this.restaurantInfoView = restaurantInfoView;
        this.markerMap = markerMap;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        getRestaurantDetailsData(marker);

        return restaurantInfoView;
    }

    private void getRestaurantDetailsData(Marker marker) {

        ImageView photoImageView = restaurantInfoView.findViewById(R.id.marker_info_window_restaurant_image_view);
        String photoReference = markerMap.get(marker.getTag()).getPhotoReference();
        String photoURL = Utils.buildGooglePhotoURL(photoReference);
        Glide.with(restaurantInfoView)
                .load(photoURL)
                .into(photoImageView);

        TextView nameTextView = restaurantInfoView.findViewById(R.id.marker_info_window_name_text_view);
        String name = markerMap.get(marker.getTag()).getName();
        nameTextView.setText(name);

        TextView descriptionTextView = restaurantInfoView.findViewById(R.id.marker_info_window_address_text_view);
        String vicinity = markerMap.get(marker.getTag()).getVicinity();
        descriptionTextView.setText(vicinity);

    }
}
