package com.christophedurand.go4lunch.data.placeName;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


public class PlaceNameRepository {

    private final MutableLiveData<String> placeNameMutableLiveData = new MutableLiveData<>();
    public LiveData<String> getPlaceNameMutableLiveData() {
        return placeNameMutableLiveData;
    }


    public void getQueriedRestaurantByName(String placeName) {
        placeNameMutableLiveData.setValue(placeName);
    }

}
