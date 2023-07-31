package com.mike.testtask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

interface OnFavouriteClickedListener {
    void onFavouriteClicked(Image image);
}

public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    private final Context context;
    private final List<Image> images;
    private OnFavouriteClickedListener listener = null;

    ImageAdapter(Context context, List<Image> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Image image = images.get(position);
        holder.bind(image, context, listener);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setOnFavouriteClickedListener(OnFavouriteClickedListener listener) {
        this.listener = listener;
    }
}
