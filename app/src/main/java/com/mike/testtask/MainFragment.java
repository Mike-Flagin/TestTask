package com.mike.testtask;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    private List<Image> images;
    private RequestQueue requestQueue;
    private ImageAdapter imageAdapter;

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        images = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = requireView().findViewById(R.id.recyclerView_main);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        imageAdapter = new ImageAdapter(requireContext(), images);
        recyclerView.setAdapter(imageAdapter);
        loadMoreImages(20);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager == null) return;
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int[] firstVisibleItems = layoutManager.findFirstVisibleItemPositions(null);
                    if (firstVisibleItems == null || firstVisibleItems.length == 0) return;
                    int pastVisibleItems = firstVisibleItems[0];
                    if (pastVisibleItems + visibleItemCount >= totalItemCount - 20) {
                        loadMoreImages(25);
                    }
                }
            }
        });
        MainActivity mainActivity = (MainActivity) requireActivity();

        imageAdapter.setOnFavouriteClickedListener(image -> {
            if (image.isFavourite()) {
                mainActivity.addToFavourites(image);
            } else {
                mainActivity.removeFromFavourites(image);
            }
        });
        mainActivity.addOnFavouritesChangedListener(new OnFavouritesChangedListener() {
            @Override
            public void onFavouritesAdd(Image image) {
            }

            @Override
            public void onFavouritesRemoved(Image image, int position) {
                int imagePosition = images.indexOf(image);
                imageAdapter.notifyItemChanged(imagePosition);
            }
        });
        recyclerView.setItemAnimator(null);
    }

    public void loadMoreImages(int imagesToLoad) {
        String API_URL = "https://shibe.online/api/shibes?urls=true&httpsUrls=true&count=";
        JsonArrayRequest imagesRequest = new JsonArrayRequest(
                Request.Method.GET, API_URL + imagesToLoad, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    getImage(response.getString(i));
                } catch (JSONException ignored) {
                }
            }
        }, error -> Log.d("Network", "Image list loading error"));
        requestQueue.add(imagesRequest);
    }

    private void getImage(String url) {
        images.add(new Image(url));
        imageAdapter.notifyItemInserted(images.size() - 1);
    }
}