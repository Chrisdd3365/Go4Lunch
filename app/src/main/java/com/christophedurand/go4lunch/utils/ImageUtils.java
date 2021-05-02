package com.christophedurand.go4lunch.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.christophedurand.go4lunch.BuildConfig;

import static com.christophedurand.go4lunch.ui.HomeActivity.apiKey;


public class ImageUtils {

    public static final String PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?photoreference=%s&key=%s&maxheight=10000";

    public static void loadGooglePhoto(Context context, ImageView imageView, String photoReference) {
        String url = String.format(PHOTO_URL, photoReference, apiKey);
        loadImage(context, url, imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) return;
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

}
