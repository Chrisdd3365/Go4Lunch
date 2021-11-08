package com.christophedurand.go4lunch.ui.mapView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;


public class MapMarker {
    @NonNull
    private final String placeId;
    private final String name;
    private final String vicinity;
    private final LatLng latLng;
    @Nullable
    private final String photoReference;
    private final String iconResId;


    public MapMarker(@NonNull String placeId, String name, String vicinity, LatLng latLng, @Nullable String photoReference, String iconResId) {
        this.placeId = placeId;
        this.name = name;
        this.vicinity = vicinity;
        this.latLng = latLng;
        this.photoReference = photoReference;
        this.iconResId = iconResId;
    }


    @NonNull
    public String getPlaceId() {
        return placeId;
    }

    public String getName() {
        return name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    @Nullable
    public String getPhotoReference() {
        return photoReference;
    }

    public String getIconResId() {
        return iconResId;
    }

}
