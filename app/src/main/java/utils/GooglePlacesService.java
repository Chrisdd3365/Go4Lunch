package utils;

import java.util.List;

import model.Restaurant;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;


interface GooglePlacesService {
    @GET("location={location}&radius=1000&type=restaurant&key=AIzaSyB8G6oFNA-nM5rPVKk6VqPiqCOu1gFEdZY")
    Call<List<Restaurant>> getRestaurants(@Path("location") String location);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/search/json?")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
