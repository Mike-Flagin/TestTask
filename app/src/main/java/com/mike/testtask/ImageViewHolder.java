package com.mike.testtask;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageViewHolder extends RecyclerView.ViewHolder {
    private final ImageView imageView;
    private final CheckBox favouriteCheckbox;
    private Image image;

    public ImageViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        favouriteCheckbox = itemView.findViewById(R.id.favourite_checkbox);
    }

    public void bind(Image image, Context context, OnFavouriteClickedListener listener) {
        if (this.image == null) {
            this.image = image;
        }
        image.setImage(context, imageView);
        favouriteCheckbox.setChecked(image.isFavourite());
        //click listener because change listener calls when view is recycled by recycleView
        favouriteCheckbox.setOnClickListener(v -> {
            if (listener != null) {
                image.invertFavourite();
                listener.onFavouriteClicked(image);
            }
        });
    }
}
