package model.repository;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.christophedurand.go4lunch.ui.mapView.RestaurantInfoAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import model.pojo.NearbyRestaurantsResponse;
import model.pojo.Restaurant;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;

//TODO: rename class when refactoring
public class NearbyRestaurantsViewModel extends AndroidViewModel {

    private final NearbyRestaurantsRepository mNearbyRestaurantsRepository;
    private final CurrentLocationRepository mCurrentLocationRepository;
    @SuppressWarnings({"FieldCanBeLocal"})
    //TODO: refactoring
    private LiveData<NearbyRestaurantsResponse> mNearbyRestaurantsList = new MutableLiveData<>();
    private LiveData<List<Marker>> mMarkersListLiveData = new MutableLiveData<>();


    public NearbyRestaurantsViewModel(@NonNull Application application, CurrentLocationRepository currentLocationRepository) {
        super(application);
        this.mNearbyRestaurantsRepository = NearbyRestaurantsRepository.getInstance();
        this.mCurrentLocationRepository = currentLocationRepository;
    }


    public LiveData<NearbyRestaurantsResponse> getNearbyRestaurantsRepository(String type, String radius, String apiKey) {
        Location location = mCurrentLocationRepository.getCurrentLocationLiveData().getValue();
        mNearbyRestaurantsList = loadNearbyRestaurantsData(type,
                location.getLatitude() + "," + location.getLongitude(), radius, apiKey);
        return mNearbyRestaurantsList;
    }

    private LiveData<NearbyRestaurantsResponse> loadNearbyRestaurantsData(String type, String location, String radius, String apiKey) {
        return mNearbyRestaurantsRepository.getListOfNearbyRestaurants(type, location, radius, apiKey);
    }

    public void setOnMapReady(GoogleMap mMap, String apiKey, Context context, Activity activity, Fragment fragment) {

        String radius = "1000";
        String type = "restaurant";

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mCurrentLocationRepository.initCurrentLocationUpdate();
            //TODO: use MediatorLiveData
            Location location = mCurrentLocationRepository.getCurrentLocationLiveData().getValue();
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            // Display a blue dot to represent the current user location
            mMap.setMyLocationEnabled(true);

            // Move the camera instantly to current location with a zoom of 15.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomIn());

            // Construct a CameraPosition focusing on the view and animate the camera to that position.
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(currentLocation)    // Sets the center of the map to current Location
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(0)                    // Sets the tilt of the camera to 0 degree
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            // Display all restaurants near current user location
            getNearbyRestaurantsRepository(type, radius, apiKey)
                    .observe(fragment.getViewLifecycleOwner(), nearbyRestaurantsResponse -> {
                    markNearbyRestaurant(nearbyRestaurantsResponse,mMap,apiKey,activity, fragment);
                    });
        }
    }

    private void markNearbyRestaurant(NearbyRestaurantsResponse nearbyRestaurantResponse, GoogleMap mMap, String apiKey,
                                      Activity activity, Fragment fragment) {
        List<Restaurant> mNearbyRestaurantsList = nearbyRestaurantResponse.getResults();
        for (Restaurant nearbyRestaurant : mNearbyRestaurantsList) {
            LatLng restaurantLatLng = new LatLng(nearbyRestaurant.getGeometry().getLocation().lat,
                    nearbyRestaurant.getGeometry().getLocation().lng);
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(restaurantLatLng)
                    .icon(bitmapDescriptorFromVector(activity, R.drawable.ic_restaurant_red_marker))
            );
            marker.setTag(nearbyRestaurant.getPlaceId());

            setNearbyRestaurantInfoWindowAdapter(mMap, apiKey, activity, fragment);
        }
    }

    private void setNearbyRestaurantInfoWindowAdapter(GoogleMap mMap, String apiKey, Activity activity, Fragment fragment) {
        mMap.setInfoWindowAdapter(new RestaurantInfoAdapter(
                activity.getLayoutInflater().inflate(R.layout.info_window_restaurant,
                        null), apiKey, fragment.getViewLifecycleOwner(), fragment));
    }


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public String getDistanceFromLastKnownUserLocation(int position) {
        Restaurant restaurant = getNearbyRestaurantsRepository("restaurant", "1000", apiKey)
                .getValue().results.get(position);

        Location restaurantLocation = new Location("restaurant location");
        restaurantLocation.setLatitude(restaurant.geometry.location.lat);
        restaurantLocation.setLongitude(restaurant.geometry.location.lng);

        Location currentLocation = mCurrentLocationRepository.getCurrentLocationLiveData().getValue();
        float distance = currentLocation.distanceTo(restaurantLocation);
        Log.d("DISTANCE", "distance is : " + distance);

        return (int)distance + "m";
    }

}
