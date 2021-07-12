package com.christophedurand.go4lunch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class Utils {

    public static final String PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?photoreference=%s&key=%s&maxheight=10000";

    public static void loadGooglePhoto(Context context, ImageView imageView, String photoReference) {
        //TODO: MOVE TO VM
        String url = String.format(PHOTO_URL, photoReference, apiKey);
        loadImage(context, url, imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) return;
//        Glide.with(context)
//                .asBitmap()
//                .load(url)
//                .listener(new RequestListener<Bitmap>() {
//                    @Override
//                    public boolean onResourceReady(Bitmap resource,
//                                                   Object model,
//                                                   Target<Bitmap> target,
//                                                   DataSource dataSource,
//                                                   boolean isFirstResource) {
//                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//                            if (marker.isInfoWindowShown()) {
//                                marker.showInfoWindow();
//                            }
//                        }, 3000);
//
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                })
//                .into(imageView);
        Glide.with(context).load(url).into(imageView);
    }

//    public static void loadMarkerIcon(Context context, Marker marker, String iconUrl) {
//        if (TextUtils.isEmpty(iconUrl)) return;
//        Glide.with(context).load(iconUrl).asBitmap().centerCrop().into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
//                marker.setIcon(icon);
//            }
//        });
//    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);

        assert vectorDrawable != null;
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

}
