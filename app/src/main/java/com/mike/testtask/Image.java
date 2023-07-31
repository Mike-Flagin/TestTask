package com.mike.testtask;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class Image {
    private final String url;
    private boolean isFavourite;

    public Image(String url) {
        this.url = url;
        isFavourite = false;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    @Override
    public boolean equals(@Nullable Object img) {
        if (img == this) return true;
        if (!(img instanceof Image)) {
            return false;
        }

        Image image = (Image) img;
        return url.equals(image.url);
    }

    public void invertFavourite() {
        isFavourite = !isFavourite;
    }

    public void setImage(Context context, ImageView imageView) {
        Glide.with(context).load(url).fitCenter().placeholder(R.drawable.loading).into(imageView);
    }
}
