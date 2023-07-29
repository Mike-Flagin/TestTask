package com.mike.testtask;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class UserDiffUtilCallback extends DiffUtil.Callback {
    private final List<User> oldList;
    private final List<User> newList;

    public UserDiffUtilCallback(List<User> oldList, List<User> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        User oldUser = oldList.get(oldItemPosition);
        User newUser = newList.get(newItemPosition);
        return oldUser == newUser;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        User oldUser = oldList.get(oldItemPosition);
        User newUser = newList.get(newItemPosition);
        return oldUser.getName().equals(newUser.getName())
                && oldUser.getBirthday().equals(newUser.getBirthday())
                && oldUser.isStudent() == newUser.isStudent()
                && oldUser.getDescription().equals(newUser.getDescription());
    }
}
