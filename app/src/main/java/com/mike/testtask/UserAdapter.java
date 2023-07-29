package com.mike.testtask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

interface OnStudentSwitchChangedListener {
    void onStudentChanged(SwitchCompat view, boolean isChecked, int position);
}

interface OnClickListener {
    void onClick(User user);

    void onLongClick(User user, int position);
}

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {
    private final Context context;
    private List<User> users;
    private OnStudentSwitchChangedListener studentChangedlistener = null;
    private OnClickListener clickListener = null;

    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    public void setOnStudentChangedListener(OnStudentSwitchChangedListener listener) {
        this.studentChangedlistener = listener;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user, context, studentChangedlistener, clickListener);
    }

    public List<User> getData() {
        return users;
    }

    public void setData(List<User> users) {
        this.users = users;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
