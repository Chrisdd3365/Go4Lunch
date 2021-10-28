package com.christophedurand.go4lunch.data.autocomplete;

import android.location.Location;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;


public class AutocompleteRepository {

    private final MediatorLiveData<Pair<String, Location>> autocompleteMediatorLiveData = new MediatorLiveData<>();
    public MediatorLiveData<Pair<String, Location>> getAutocompleteMediatorLiveData() {
        return autocompleteMediatorLiveData;
    }


    public AutocompleteRepository(LiveData<String> placeNameLiveData, LiveData<Location> currentLocationLiveData) {
        autocompleteMediatorLiveData.addSource(placeNameLiveData, keyword -> autocompleteMediatorLiveData.setValue(Pair.create(keyword, currentLocationLiveData.getValue())));
        autocompleteMediatorLiveData.addSource(currentLocationLiveData, currentLocation -> autocompleteMediatorLiveData.setValue(Pair.create(placeNameLiveData.getValue(), currentLocation)));
    }

}
