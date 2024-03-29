package com.christophedurand.go4lunch;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class LiveDataTestUtils {

    public static <T> T getOrAwaitValue(final LiveData<T> liveData, int changeCount) throws InterruptedException {
        final Object[] data = new Object[1];
        final CountDownLatch latch = new CountDownLatch(changeCount);
        Observer<T> observer = new Observer<T>() {
            @Override
            public void onChanged(@Nullable T o) {
                data[0] = o;
                latch.countDown();
                liveData.removeObserver(this);
            }
        };

        liveData.observeForever(observer);

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(2, TimeUnit.SECONDS)) {
            throw new RuntimeException("LiveData value was never set.");
        }
        //noinspection unchecked
        return (T) data[0];
    }

    public static <T> T observeForTesting(LiveData<T> liveData) {
        liveData.observeForever(new Observer<T>() {
            @Override
            public void onChanged(T t) {

            }
        });
        return liveData.getValue();
    }

}
