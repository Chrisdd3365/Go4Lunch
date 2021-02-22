package model;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {

    private static RetrofitService sInstance;
    private static final String BASE_URL = "https://maps.googleapis.com/";
    private final GooglePlacesAPIService googlePlacesAPIService;

    private RetrofitService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        googlePlacesAPIService = retrofit.create(GooglePlacesAPIService.class);
    }

    public static RetrofitService getInstance() {
        if (sInstance == null) {
            sInstance = new RetrofitService();
        }
        return sInstance;
    }

    public GooglePlacesAPIService getGooglePlacesAPIService() {
        return googlePlacesAPIService;
    }

}
