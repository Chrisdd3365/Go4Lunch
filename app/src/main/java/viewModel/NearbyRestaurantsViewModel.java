package viewModel;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.christophedurand.go4lunch.data.location.CurrentLocationRepository;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.Objects;

import model.pojo.NearbyRestaurantsResponse;
import model.pojo.Restaurant;
import model.repository.NearbyRestaurantsRepository;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;

//TODO: rename class when refactoring
public class NearbyRestaurantsViewModel extends AndroidViewModel {

    private final NearbyRestaurantsRepository mNearbyRestaurantsRepository;
    private final CurrentLocationRepository mCurrentLocationRepository;
    @SuppressWarnings({"FieldCanBeLocal"})
    private LiveData<NearbyRestaurantsResponse> mNearbyRestaurantsList = new MutableLiveData<>();
    public final MediatorLiveData<Location> locationMediatorLiveData = new MediatorLiveData<>();


    public NearbyRestaurantsViewModel(@NonNull Application application, CurrentLocationRepository currentLocationRepository) {
        super(application);
        this.mNearbyRestaurantsRepository = NearbyRestaurantsRepository.getInstance();
        this.mCurrentLocationRepository = currentLocationRepository;

        mCurrentLocationRepository.initCurrentLocationUpdate();
        LiveData<Location> locationLiveData = mCurrentLocationRepository.getCurrentLocationLiveData();
        locationMediatorLiveData.addSource(locationLiveData, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                locationMediatorLiveData.setValue(location);
            }
        });
    }


    public LiveData<NearbyRestaurantsResponse> getNearbyRestaurantsRepository(String type, Location location,
                                                                              String radius, String apiKey) {
        mNearbyRestaurantsList = loadNearbyRestaurantsData(type,
                    location.getLatitude() + "," + location.getLongitude(),
                radius, apiKey);

        return mNearbyRestaurantsList;
    }

    private LiveData<NearbyRestaurantsResponse> loadNearbyRestaurantsData(String type, String location, String radius, String apiKey) {
        return mNearbyRestaurantsRepository.getListOfNearbyRestaurants(type, location, radius, apiKey);
    }

    public BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public String getDistanceFromLastKnownUserLocation(int position, Location location) {
        Restaurant restaurant = Objects.requireNonNull(
                getNearbyRestaurantsRepository("restaurant", location, "1000", apiKey)
                .getValue()
        ).results.get(position);

        Location restaurantLocation = new Location("restaurant location");
        restaurantLocation.setLatitude(restaurant.geometry.location.lat);
        restaurantLocation.setLongitude(restaurant.geometry.location.lng);

        Location currentLocation = locationMediatorLiveData.getValue();
        assert currentLocation != null;
        float distance = currentLocation.distanceTo(restaurantLocation);
        Log.d("DISTANCE", "distance is : " + distance);

        return (int)distance + "m";
    }

}
