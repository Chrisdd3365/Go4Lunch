package com.christophedurand.go4lunch.data.autocomplete;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import kotlin.Pair;


public class AutocompleteRepository {

    private final MediatorLiveData<Pair<String, Location>> autocompleteMediatorLiveData = new MediatorLiveData<>();
    public MediatorLiveData<Pair<String, Location>> getAutocompleteMediatorLiveData() {
        return autocompleteMediatorLiveData;
    }

    public AutocompleteRepository(LiveData<String> placeNameLiveData, LiveData<Location> currentLocationLiveData) {
        autocompleteMediatorLiveData.addSource(currentLocationLiveData, currentLocation -> autocompleteMediatorLiveData.setValue(new Pair(placeNameLiveData.getValue(), currentLocation)));
        autocompleteMediatorLiveData.addSource(placeNameLiveData, keyword -> autocompleteMediatorLiveData.setValue(new Pair(keyword, currentLocationLiveData.getValue())));
    }

}
