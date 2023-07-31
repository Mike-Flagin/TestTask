package com.mike.testtask;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

interface OnFavouritesChangedListener {
    void onFavouritesAdd(Image image);

    void onFavouritesRemoved(Image image, int position);
}

public class MainActivity extends AppCompatActivity {
    private static List<Image> favouriteImages;
    private final List<OnFavouritesChangedListener> listeners = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        favouriteImages = new ArrayList<>();
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new ViewPagerAdapter(this));
        viewPager.setCurrentItem(0);

        //check internet and try to reconnect every 5 sec
        if (!hasInternet()) {
            Snackbar.make(viewPager, getString(R.string.not_connected), Snackbar.LENGTH_SHORT).show();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if (hasInternet()) {
                        Snackbar.make(viewPager, getString(R.string.connected), Snackbar.LENGTH_SHORT).show();
                        ((ViewPagerAdapter) Objects.requireNonNull(viewPager.getAdapter())).mainFragment.loadMoreImages(20);
                        cancel();
                    }
                }
            }, 0, 5000);
        }
    }

    public void addToFavourites(Image image) {
        boolean res = favouriteImages.add(image);
        if (res && listeners != null && listeners.size() != 0) {
            for (OnFavouritesChangedListener listener : listeners) {
                listener.onFavouritesAdd(image);
            }
        }
    }

    public void removeFromFavourites(Image image) {
        int position = favouriteImages.indexOf(image);
        boolean res = favouriteImages.remove(image);
        if (res && listeners != null && listeners.size() != 0) {
            for (OnFavouritesChangedListener listener : listeners) {
                listener.onFavouritesRemoved(image, position);
            }
        }
    }

    public List<Image> getFavouriteImages() {
        return favouriteImages;
    }

    public void addOnFavouritesChangedListener(OnFavouritesChangedListener listener) {
        this.listeners.add(listener);
    }

    boolean hasInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Network activeNetwork = connectivityManager.getActiveNetwork();
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(activeNetwork);

        if (capabilities != null) {
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return false;
    }

    public static class ViewPagerAdapter extends FragmentStateAdapter {

        private MainFragment mainFragment;

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 1) {
                return new FavouritesFragment();
            }
            mainFragment = new MainFragment();
            return mainFragment;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}