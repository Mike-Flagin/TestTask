package com.mike.testtask;

import static com.mike.testtask.User.BIRTHDAY_PARAM;
import static com.mike.testtask.User.DESCRIPTION_PARAM;
import static com.mike.testtask.User.IS_STUDENT_PARAM;
import static com.mike.testtask.User.NAME_PARAM;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private static final ArrayList<User> users = new ArrayList<>();
    private final String USERS_SP_NAME = "Users";
    private final String USERS_SIZE_PARAM = "size";
    private RecyclerView recyclerView;
    private UserAdapter recyclerViewAdapter;

    ActivityResultLauncher<Intent> startAddUserActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent == null) return;
                    String name = intent.getStringExtra(NAME_PARAM);
                    String birthday = intent.getStringExtra(BIRTHDAY_PARAM);
                    String description = intent.getStringExtra(DESCRIPTION_PARAM);
                    AddUser(name, birthday, description);
                    Snackbar.make(findViewById(R.id.activity_layout), R.string.user_added_successfully, Snackbar.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.users_recyclerview);
        if (savedInstanceState == null) {
            setInitialData();
        }
        setupRecyclerView();
        recyclerViewAdapter = ((UserAdapter) Objects.requireNonNull(recyclerView.getAdapter()));
        recyclerViewAdapter.setOnStudentChangedListener((view, isChecked, position) -> users.get(position).setIsStudent(isChecked));
        recyclerViewAdapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(User user) {
                DetailedInfoFragment fragment = DetailedInfoFragment.newInstance(user.getName(), user.getAge(), user.getBirthday(), user.isStudent(), user.getDescription());
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.activity_layout, fragment)
                        .addToBackStack(null)
                        .commit();
            }

            @Override
            public void onLongClick(User user, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.delete_user_dialog)
                        .setNegativeButton(getString(R.string.no), (dialog, which) -> dialog.cancel())
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> DeleteUser(position))
                        .create();
                builder.show();
            }
        });
    }

    private void AddUser(String name, String birthday, String description) {
        User user = new User(name, birthday, description);
        users.add(user);
        recyclerViewAdapter.notifyItemInserted(users.size() - 1);
        SharedPreferences preferences = getSharedPreferences(USERS_SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(USERS_SIZE_PARAM, users.size());
        editor.putString(NAME_PARAM + (users.size() - 1), user.getName());
        editor.putString(BIRTHDAY_PARAM + (users.size() - 1), user.getBirthday());
        editor.putBoolean(IS_STUDENT_PARAM + (users.size() - 1), user.isStudent());
        editor.putString(DESCRIPTION_PARAM + (users.size() - 1), user.getDescription());
        editor.apply();
    }

    private void DeleteUser(int position) {
        users.remove(position);
        recyclerViewAdapter.notifyItemRemoved(position);
        saveUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_user) {
            startAddUserActivity.launch(new Intent(this, AddUserActivity.class));
            return true;
        }
        if (item.getItemId() == R.id.action_sort) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(R.string.pick_sort)
                    .setItems(R.array.sort_types, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                SortUsersName();
                                break;
                            case 1:
                                SortUsersAge();
                                break;
                            case 2:
                                SortUsersStudentStatus();
                                break;
                            case 3:
                                SortUsersDescriptionLength();
                                break;
                        }
                    })
                    .setCancelable(true)
                    .create();
            builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SortUsersDescriptionLength() {
        List<User> temp = new ArrayList<>(users);
        temp.sort(Comparator.comparingInt(user -> user.getDescription().length()));
        UpdateRecyclerView(temp);
    }

    private void SortUsersStudentStatus() {
        List<User> temp = new ArrayList<>(users);
        temp.sort((user1, user2) -> {
            if (user1.isStudent() && !user2.isStudent()) {
                return -1;
            }
            if (!user1.isStudent() && user2.isStudent()) {
                return 1;
            }
            return 0;
        });
        UpdateRecyclerView(temp);
    }

    private void SortUsersAge() {
        List<User> temp = new ArrayList<>(users);
        temp.sort(Comparator.comparing(User::getAge));
        UpdateRecyclerView(temp);
    }

    private void SortUsersName() {
        List<User> temp = new ArrayList<>(users);
        temp.sort(Comparator.comparing(User::getName));
        UpdateRecyclerView(temp);
    }

    private void UpdateRecyclerView(List<User> users) {
        UserDiffUtilCallback userDiffUtilCallback = new UserDiffUtilCallback(recyclerViewAdapter.getData(), users);
        DiffUtil.DiffResult productDiffResult = DiffUtil.calculateDiff(userDiffUtilCallback, true);

        recyclerViewAdapter.setData(users);
        productDiffResult.dispatchUpdatesTo(recyclerViewAdapter);
    }

    private void setupRecyclerView() {
        UserAdapter adapter = new UserAdapter(this, users);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, RecyclerView.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void setInitialData() {
        SharedPreferences preferences = getSharedPreferences(USERS_SP_NAME, Context.MODE_PRIVATE);
        int size = preferences.getInt(USERS_SIZE_PARAM, 0);
        if (size == 0) return;
        for (int i = 0; i < size; i++) {
            String name = preferences.getString(NAME_PARAM + i, null);
            String birthday = preferences.getString(BIRTHDAY_PARAM + i, null);
            boolean isStudent = preferences.getBoolean(IS_STUDENT_PARAM + i, false);
            String description = preferences.getString(DESCRIPTION_PARAM + i, null);
            users.add(new User(name, birthday, isStudent, description));
        }
        /*
        Calendar calendar = Calendar.getInstance();
        calendar.set(1879, 3, 14);
        users.add(new User("Einstein Albert", calendar, "Альбе́рт Эйнште́йн — американский, немецкий и швейцарский физик-теоретик, один из основателей современной теоретической физики, философ науки и общественный деятель-гуманист, лауреат Нобелевской премии по физике 1921 года"));
        calendar = Calendar.getInstance();
        calendar.set(2002, 5, 7);
        users.add(new User("Mike", calendar));
         */
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        saveUsers();
    }

    private void saveUsers() {
        SharedPreferences preferences = getSharedPreferences(USERS_SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(USERS_SIZE_PARAM, users.size());
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            editor.putString(NAME_PARAM + i, user.getName());
            editor.putString(BIRTHDAY_PARAM + i, user.getBirthday());
            editor.putBoolean(IS_STUDENT_PARAM + i, user.isStudent());
            editor.putString(DESCRIPTION_PARAM + i, user.getDescription());
        }
        editor.apply();
    }
}