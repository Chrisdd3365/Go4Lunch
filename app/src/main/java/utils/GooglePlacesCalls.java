package utils;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

import model.Restaurant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GooglePlacesCalls {
    // 1 - Creating a callback
    public interface Callbacks {
        void onResponse(@Nullable List<Restaurant> restaurants);
        void onFailure();
    }

    // 2 - Public method to start fetching places nearby current user location
    public static void fetchNearbyRestaurants(Callbacks callbacks, String location) {

        // 2.1 - Create a weak reference to callback (avoid memory leaks)
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        // 2.2 - Get a Retrofit instance and the related endpoints
        GooglePlacesService googlePlacesService = GooglePlacesService.retrofit.create(GooglePlacesService.class);

        // 2.3 - Create the call on Google Places API
        Call<List<Restaurant>> call = googlePlacesService.getRestaurants(location);
        // 2.4 - Start the call
        call.enqueue(new Callback<List<Restaurant>>() {

            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                // 2.5 - Call the proper callback used in controller (MainFragment)
                if (callbacksWeakReference.get() != null) callbacksWeakReference.get().onFailure();
            }
        });
    }
}
