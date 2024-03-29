package com.christophedurand.go4lunch.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.christophedurand.go4lunch.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import static com.christophedurand.go4lunch.ui.homeView.HomeActivity.API_KEY;

import java.lang.reflect.Field;


public class Utils {

    public static final String PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?photoreference=%s&key=%s&maxheight=10000";

    public static String buildGooglePhotoURL(String photoReference) {
        return String.format(PHOTO_URL, photoReference, API_KEY);
    }

    public static void loadGooglePhoto(Context context, ImageView imageView, String photoReference) {
        //TODO: MOVE TO VM
        String url = String.format(PHOTO_URL, photoReference, API_KEY);
        loadImage(context, url, imageView);
    }

    public static void loadImage(Context context, String url, ImageView imageView) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(context).asBitmap().load(url).into(imageView);
    }

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

    public static Drawable getAvatarPlaceHolder(Context context) {
        Drawable avatarPlaceHolder = ContextCompat.getDrawable(context, R.drawable.ic_account_circle).mutate();
        avatarPlaceHolder.setColorFilter(ContextCompat.getColor(context, R.color.colorAvatarPlaceholder), PorterDuff.Mode.SRC_IN);
        return avatarPlaceHolder;
    }

    public static int getResId(String resName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}
