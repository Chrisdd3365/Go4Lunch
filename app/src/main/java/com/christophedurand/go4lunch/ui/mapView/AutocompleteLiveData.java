package com.christophedurand.go4lunch.ui.mapView;


import android.location.Location;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;


public class AutocompleteLiveData extends MediatorLiveData<Pair<String, Location>> {

    public AutocompleteLiveData(LiveData<String> keywordLiveData, LiveData<Location> currentLocationLiveData) {
        addSource(keywordLiveData, keyword -> setValue(Pair.create(keyword, currentLocationLiveData.getValue())));
        addSource(currentLocationLiveData, currentLocation -> setValue(Pair.create(keywordLiveData.getValue(), currentLocation)));
    }

}
