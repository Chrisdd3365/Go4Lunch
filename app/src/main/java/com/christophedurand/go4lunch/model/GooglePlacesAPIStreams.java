//package com.christophedurand.go4lunch.model;
//
//
//import com.christophedurand.go4lunch.model.pojo.RestaurantDetailsResponse;
//
//import java.util.concurrent.TimeUnit;
//
//import io.reactivex.Observable;
//import io.reactivex.android.schedulers.AndroidSchedulers;
//import io.reactivex.schedulers.Schedulers;
//
//
//public class GooglePlacesAPIStreams {
//
//    public static Observable<RestaurantDetailsResponse> streamFetchRestaurantDetails(String placeId,
//                                                                                     String apiKey) {
//        RetrofitService retrofitService = RetrofitService.getInstance();
//        return retrofitService.getGooglePlacesAPIService().getPlaceDetails(placeId, apiKey)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .timeout(10, TimeUnit.SECONDS);
//    }
//
//}
