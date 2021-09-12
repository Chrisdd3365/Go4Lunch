package com.christophedurand.go4lunch.ui.workmatesView;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.christophedurand.go4lunch.R;
import com.christophedurand.go4lunch.model.User;
import com.christophedurand.go4lunch.ui.listView.ListInterface;
import com.christophedurand.go4lunch.utils.Utils;


class WorkmateViewHolder extends RecyclerView.ViewHolder {

    public final ImageView avatarImageView;
    public final TextView descriptionTextView;


    public WorkmateViewHolder(View view) {
        super(view);

        avatarImageView = view.findViewById(R.id.avatar_image_view);
        descriptionTextView = view.findViewById(R.id.description_text_view);

    }


    public void bind(User user, ListInterface listener) {

        Drawable avatarPlaceHolder = Utils.getAvatarPlaceHolder(itemView.getContext());
        String avatarURL = user.getAvatarURL();
        Glide.with(itemView.getContext())
                .load(avatarURL)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(avatarPlaceHolder)
                .into(avatarImageView);

        String description = user.getName() + " is eating at " + user.getRestaurantName();
        descriptionTextView.setText(description);

        itemView.setOnClickListener(v -> {
            listener.onClickItemList(user.getRestaurantId());
        });
    }

}