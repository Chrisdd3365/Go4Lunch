package com.christophedurand.go4lunch.data.location;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;


public class CurrentLocationRepository {

    private static final int LOCATION_REQUEST_INTERVAL_MS = 30_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 100;


    private final MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();
    private LocationCallback callback;
    private final FusedLocationProviderClient fusedLocationClient;

    public CurrentLocationRepository(FusedLocationProviderClient fusedLocationClient) {
        this.fusedLocationClient = fusedLocationClient;
    }

    public LiveData<Location> getCurrentLocationLiveData() {
        return currentLocationLiveData;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void initCurrentLocationUpdate() {
        if (callback == null) {
            callback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();
                    currentLocationLiveData.setValue(location);
                }
            };
        }

        fusedLocationClient.removeLocationUpdates(callback);

        fusedLocationClient.requestLocationUpdates(
                LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                        .setInterval(LOCATION_REQUEST_INTERVAL_MS),
                callback,
                Looper.getMainLooper()
        );
    }

    public void stopLocationRequest() {
        if (callback != null) {
            fusedLocationClient.removeLocationUpdates(callback);
        }
    }

}
