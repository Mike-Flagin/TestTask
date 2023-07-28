package com.mike.testtask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class CounterFragment extends Fragment {
    public static final String COUNTER_KEY = "counter";
    private int counter = 0;
    private TextView textView;

    public CounterFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_counter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = requireView().findViewById(R.id.fragment_toolbar);
        toolbar.setNavigationOnClickListener(v -> requireActivity().onBackPressed());
        textView = requireView().findViewById(R.id.fragment_textview);
        textView.setText(String.valueOf(counter));
        requireView().findViewById(R.id.add_button).setOnClickListener(button -> textView.setText(String.valueOf(++counter)));
    }

    public int getCounterValue() {
        return counter;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COUNTER_KEY, counter);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) return;
        counter = savedInstanceState.getInt(COUNTER_KEY, 0);
        textView.setText(String.valueOf(counter));
    }
}
