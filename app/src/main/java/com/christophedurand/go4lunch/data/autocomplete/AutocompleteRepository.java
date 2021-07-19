package com.christophedurand.go4lunch.data.autocomplete;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.model.GooglePlacesAPIService;
import com.christophedurand.go4lunch.model.RetrofitService;
import com.christophedurand.go4lunch.model.pojo.AutocompleteResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AutocompleteRepository {

    private static GooglePlacesAPIService mGooglePlacesAPIService = null;
    private static AutocompleteRepository sAutocompleteRepository;

    public static AutocompleteRepository getInstance() {
        if (sAutocompleteRepository == null) {
            sAutocompleteRepository = new AutocompleteRepository();
        }
        return sAutocompleteRepository;
    }

    private AutocompleteRepository() {
        mGooglePlacesAPIService = RetrofitService.getInstance().getGooglePlacesAPIService();
    }

public LiveData<AutocompleteResponse> getAutocompleteResponseLiveData(String input, String apiKey) {
        final MutableLiveData<AutocompleteResponse> autocompleteResponseMutableLiveData = new MutableLiveData<>();
        Call<AutocompleteResponse> nearbyRestaurantsList = mGooglePlacesAPIService.getAutocompleteResult(input, apiKey);
        nearbyRestaurantsList.enqueue(new Callback<AutocompleteResponse>() {
            @Override
            public void onResponse(@NonNull Call<AutocompleteResponse> call, @NonNull Response<AutocompleteResponse> response) {
                autocompleteResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<AutocompleteResponse> call, @NonNull Throwable t) { }
        });

        return autocompleteResponseMutableLiveData;
    }

}
