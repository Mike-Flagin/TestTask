package com.mike.testtask;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String FRAGMENT_TAG = "fragment";
    private static final String COUNTER_KEY = "counter";
    private CounterFragment fragment;
    private int counter = 0;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.activity_textview);
        if (savedInstanceState != null) {
            counter = savedInstanceState.getInt(COUNTER_KEY, 0);
        }
        textView.setText(String.valueOf(counter));
        findViewById(R.id.start_fragment_button).setOnClickListener(view -> {
            fragment = new CounterFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_layout, fragment, FRAGMENT_TAG)
                    .addToBackStack(null)
                    .commit();
        });
        if (fragment == null)
            fragment = (CounterFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (fragment != null) {
            counter = fragment.getCounterValue();
            textView.setText(String.valueOf(counter));
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COUNTER_KEY, counter);
    }
}