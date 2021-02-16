package com.christophedurand.go4lunch.data.location;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

public class CurrentLocationRepository {

    private final MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();
    private LocationCallback callback;
    private final FusedLocationProviderClient fusedLocationClient;

    public CurrentLocationRepository(FusedLocationProviderClient fusedLocationClient) {
        this.fusedLocationClient = fusedLocationClient;
    }

    public LiveData<Location> getCurrentLocationLiveData() {
        return currentLocationLiveData;
    }

    @SuppressLint("MissingPermission")
    public void initCurrentLocationUpdate() {
        if (callback == null) {
            callback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    if (location != null) {
                        currentLocationLiveData.setValue(location);
                    }
                }
            };
        }
        fusedLocationClient.requestLocationUpdates(new LocationRequest().setInterval(30_000), callback, Looper.getMainLooper());
    }

    public void stopLocationRequest() {
        fusedLocationClient.removeLocationUpdates(callback);
    }

}
