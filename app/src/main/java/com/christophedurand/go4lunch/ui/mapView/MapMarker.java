package com.christophedurand.go4lunch.ui.mapView;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;


public class MapMarker {
    @NonNull
    private final String placeId;
    private final String name;
    private final String vicinity;
    private final LatLng latLng;
    private final String photoReference;


    public MapMarker(@NonNull String placeId, String name, String vicinity, LatLng latLng, @NonNull String photoReference) {
        this.placeId = placeId;
        this.name = name;
        this.vicinity = vicinity;
        this.latLng = latLng;
        this.photoReference = photoReference;
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

    public String getPhotoReference() {
        return photoReference;
    }

}
