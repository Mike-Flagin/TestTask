package com.mike.testtask;

import java.util.Calendar;

public class User {
    public static final String NAME_PARAM = "name";
    public static final String AGE_PARAM = "age";
    public static final String BIRTHDAY_PARAM = "birthday";
    public static final String IS_STUDENT_PARAM = "isStudent";
    public static final String DESCRIPTION_PARAM = "description";
    private final String name;
    private final Calendar birthday;
    private final String description;
    private boolean isStudent;

    public User(String name, String birthday, String description) {
        this(name, birthday, false, description);
    }

    public User(String name, String birthday, boolean isStudent, String description) {
        Calendar calendar = Calendar.getInstance();
        String[] date = birthday.split("/");
        calendar.set(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
        this.name = name;
        this.birthday = calendar;
        this.isStudent = isStudent;
        this.description = description;
    }

    public static String dateToString(Calendar date) {
        return date.get(Calendar.DAY_OF_MONTH) + "/" +
                date.get(Calendar.MONTH) + "/" +
                date.get(Calendar.YEAR);
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return dateToString(birthday);
    }

    public int getAge() {
        Calendar currentDate = Calendar.getInstance();
        if (currentDate.get(Calendar.DAY_OF_MONTH) < birthday.get(Calendar.DAY_OF_MONTH) && currentDate.get(Calendar.MONTH) < birthday.get(Calendar.MONTH)) {
            return currentDate.get(Calendar.YEAR) - birthday.get(Calendar.YEAR) - 1;
        } else {
            return currentDate.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);
        }
    }

    public boolean isStudent() {
        return isStudent;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setIsStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }
}

