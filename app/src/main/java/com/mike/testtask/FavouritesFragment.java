package com.mike.testtask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class FavouritesFragment extends Fragment {
    public FavouritesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity mainActivity = (MainActivity) requireActivity();

        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView_favourites);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        ImageAdapter imageAdapter = new ImageAdapter(requireContext(), mainActivity.getFavouriteImages());
        recyclerView.setAdapter(imageAdapter);

        ((MainActivity) requireActivity()).addOnFavouritesChangedListener(new OnFavouritesChangedListener() {
            @Override
            public void onFavouritesAdd(Image image) {
                imageAdapter.notifyItemInserted(mainActivity.getFavouriteImages().indexOf(image));
            }

            @Override
            public void onFavouritesRemoved(Image image, int position) {
                imageAdapter.notifyItemRemoved(position);
            }
        });
        //in this fragment it can be only deleted
        imageAdapter.setOnFavouriteClickedListener(mainActivity::removeFromFavourites);
    }
}