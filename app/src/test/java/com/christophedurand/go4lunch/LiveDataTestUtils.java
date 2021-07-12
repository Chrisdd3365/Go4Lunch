package com.christophedurand.go4lunch;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class LiveDataTestUtils {

    // TEST D'INTEGRATION
    public static <T> T getOrAwaitValue(final LiveData<T> liveData) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        Observer<T> observer = o -> latch.countDown();
        liveData.observeForever(observer);
        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new RuntimeException("LiveData value was never set.");
        }
        // No inspection unchecked
        return liveData.getValue();
    }

    // TEST UNITAIRE
    public static <T> T observeForTesting(LiveData<T> liveData) {
        liveData.observeForever(new Observer<T>() {
            @Override
            public void onChanged(T t) {

            }
        });
        return liveData.getValue();
    }

}
