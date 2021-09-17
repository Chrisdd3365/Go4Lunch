package com.christophedurand.go4lunch.ui.mapView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;


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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapMarker mapMarker = (MapMarker) o;
        return placeId.equals(mapMarker.placeId) && Objects.equals(name, mapMarker.name) && Objects.equals(vicinity, mapMarker.vicinity) && Objects.equals(latLng, mapMarker.latLng) && Objects.equals(photoReference, mapMarker.photoReference) && Objects.equals(iconResId, mapMarker.iconResId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(placeId, name, vicinity, latLng, photoReference, iconResId);
    }

    @Override
    public String toString() {
        return "MapMarker{" +
                "placeId='" + placeId + '\'' +
                ", name='" + name + '\'' +
                ", vicinity='" + vicinity + '\'' +
                ", latLng=" + latLng +
                ", photoReference='" + photoReference + '\'' +
                ", iconResId='" + iconResId + '\'' +
                '}';
    }

}
