package com.mike.testtask;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

public class UserViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameTextView;
    private final TextView ageTextView;
    private final TextView descriptionTextView;
    private final SwitchCompat studentSwitch;
    private final View mainView;

    public UserViewHolder(@NonNull View view) {
        super(view);
        nameTextView = view.findViewById(R.id.name);
        ageTextView = view.findViewById(R.id.age);
        descriptionTextView = view.findViewById(R.id.description);
        studentSwitch = view.findViewById(R.id.student);
        mainView = view;
    }

    public void bind(User user, Context context, OnStudentSwitchChangedListener listener, OnClickListener clickListener) {
        nameTextView.setText(context.getString(R.string.name, user.getName()));
        ageTextView.setText(context.getString(R.string.age, user.getAge(), user.getBirthday()));
        descriptionTextView.setText(context.getString(R.string.description, user.getDescription()));
        studentSwitch.setChecked(user.isStudent());
        studentSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (listener != null) {
                listener.onStudentChanged((SwitchCompat) buttonView, isChecked, getAdapterPosition());
            }
        });
        mainView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(user);
            }
        });
        mainView.setOnLongClickListener(v -> {
            if (clickListener != null) {
                clickListener.onLongClick(user, getAdapterPosition());
                return true;
            }
            return false;
        });
    }
}
