package com.mike.testtask;

import static com.mike.testtask.User.AGE_PARAM;
import static com.mike.testtask.User.BIRTHDAY_PARAM;
import static com.mike.testtask.User.DESCRIPTION_PARAM;
import static com.mike.testtask.User.IS_STUDENT_PARAM;
import static com.mike.testtask.User.NAME_PARAM;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailedInfoFragment extends Fragment {

    private String name;
    private int age;
    private String birthday;
    private boolean isStudent;
    private String description;

    public DetailedInfoFragment() {
    }

    public static DetailedInfoFragment newInstance(String name, int age, String birthday, boolean isStudent, String description) {
        DetailedInfoFragment fragment = new DetailedInfoFragment();
        Bundle args = new Bundle();
        args.putString(NAME_PARAM, name);
        args.putInt(AGE_PARAM, age);
        args.putString(BIRTHDAY_PARAM, birthday);
        args.putBoolean(IS_STUDENT_PARAM, isStudent);
        args.putString(DESCRIPTION_PARAM, description);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(NAME_PARAM);
            age = getArguments().getInt(AGE_PARAM);
            birthday = getArguments().getString(BIRTHDAY_PARAM);
            isStudent = getArguments().getBoolean(IS_STUDENT_PARAM);
            description = getArguments().getString(DESCRIPTION_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detailed_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) requireView().findViewById(R.id.name_fragment_textview)).setText(getString(R.string.name, name));
        ((TextView) requireView().findViewById(R.id.age_fragment_textview)).setText(getString(R.string.age, age, birthday));
        ((TextView) requireView().findViewById(R.id.isStudent_fragment_textview)).setText(getString(R.string.isStudent, isStudent ? "Yes" : "No"));
        ((TextView) requireView().findViewById(R.id.description_fragment_textview)).setText(getString(R.string.description, description));
    }
}