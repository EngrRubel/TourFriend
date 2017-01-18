package com.jkkniugmail.rubel.tourfriend.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jkkniugmail.rubel.tourfriend.models.Event;
import com.jkkniugmail.rubel.tourfriend.models.Expense;
import com.jkkniugmail.rubel.tourfriend.models.Moment;
import com.jkkniugmail.rubel.tourfriend.models.User;

import java.util.ArrayList;

/**
 * Created by islan on 12/26/2016.
 */

public class DatabaseManager {

    private DatabaseHelper helper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        helper = new DatabaseHelper(context);
    }

    private void openDatabase() {
        database = helper.getWritableDatabase();

    }

    private void closeDatabase() {
        database.close();
        helper.close();

    }

    //user sign up
    public boolean addNewUser(User user) {
        this.openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_USER_FIRST_NAME, user.getFirst_name());
        cv.put(DatabaseHelper.COL_USER_LAST_NAME, user.getLast_name());
        cv.put(DatabaseHelper.COL_USER_EMAIL, user.getEmail());
        cv.put(DatabaseHelper.COL_USER_PHONE_NO, user.getPhone_no());
        cv.put(DatabaseHelper.COL_USER_PASSWORD, user.getPassword());
        long inserted = database.insert(DatabaseHelper.USER_TABLE, null, cv);
        this.closeDatabase();
        return (inserted > 0);
    }

    //user update
    public boolean updateUser(User user, int user_id) {
        this.openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_USER_FIRST_NAME, user.getFirst_name());
        cv.put(DatabaseHelper.COL_USER_LAST_NAME, user.getLast_name());
        cv.put(DatabaseHelper.COL_USER_EMAIL, user.getEmail());
        cv.put(DatabaseHelper.COL_USER_PHONE_NO, user.getPhone_no());
        cv.put(DatabaseHelper.COL_USER_PASSWORD, user.getPassword());
        int result = database.update(DatabaseHelper.USER_TABLE, cv, DatabaseHelper.COL_USER_ID + "=" + user_id, null);
        this.closeDatabase();
        return (result > 0);
    }

    //user sign in
    public int userLogin(String email, String password) {
        Cursor cursor;
        int id;
        this.openDatabase();
        String sql = "SELECT " + DatabaseHelper.COL_USER_ID + " FROM " + DatabaseHelper.USER_TABLE + " WHERE " +
                DatabaseHelper.COL_USER_EMAIL + " = '" + email + "' AND " + DatabaseHelper.COL_USER_PASSWORD + " = '" + password + "'";

        cursor = database.rawQuery(sql, null, null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_USER_ID));
        } else {
            id = 0;
        }
        this.closeDatabase();
        return id;
    }

    public User getUser(int id) {
        Cursor cursor;
        User user;

        this.openDatabase();
        cursor = database.query(DatabaseHelper.USER_TABLE, new String[]{
                DatabaseHelper.COL_USER_FIRST_NAME, DatabaseHelper.COL_USER_LAST_NAME,
                DatabaseHelper.COL_USER_EMAIL, DatabaseHelper.COL_USER_PHONE_NO}, DatabaseHelper.COL_USER_ID + "=" + id, null, null, null, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String first_name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_USER_FIRST_NAME));
            String last_name = cursor.getString((cursor.getColumnIndex(DatabaseHelper.COL_USER_LAST_NAME)));
            String email = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_USER_EMAIL));
            String phone_no = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_USER_PHONE_NO));

            user = new User(id, first_name, last_name, phone_no, email, " ");
        } else {
            user = null;
        }
        return user;
    }

    public boolean addNewEvent(Event event, int user_id) {
        this.openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_EVENT_TITLE, event.getTitle());
        cv.put(DatabaseHelper.COL_EVENT_DESCRIPTION, event.getDescription());
        cv.put(DatabaseHelper.COL_EVENT_LOCATION, event.getLocation());
        cv.put(DatabaseHelper.COL_EVENT_START_DATE, event.getStarting_date());
        cv.put(DatabaseHelper.COL_EVENT_BUDGET, event.getBudget());
        cv.put(DatabaseHelper.COL_EVENT_USER_ID, user_id);
        long inserted = database.insert(DatabaseHelper.EVENT_TABLE, null, cv);
        this.closeDatabase();
        return (inserted > 0);
    }

    public boolean updateEvent(Event event, int event_id) {
        this.openDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_EVENT_TITLE, event.getTitle());
        cv.put(DatabaseHelper.COL_EVENT_DESCRIPTION, event.getDescription());
        cv.put(DatabaseHelper.COL_EVENT_LOCATION, event.getLocation());
        cv.put(DatabaseHelper.COL_EVENT_START_DATE, event.getStarting_date());
        cv.put(DatabaseHelper.COL_EVENT_BUDGET, event.getBudget());
        int result = database.update(DatabaseHelper.EVENT_TABLE, cv, DatabaseHelper.COL_EVENT_ID + " = " + event_id, null);
        this.closeDatabase();
        return (result > 0);
    }

    public ArrayList<Event> getAllEvent(int user_id) {
        Cursor cursor;
        ArrayList<Event> eventArrayList = new ArrayList<>();
        this.openDatabase();
        cursor = database.query(DatabaseHelper.EVENT_TABLE, new String[]{DatabaseHelper.COL_EVENT_ID, DatabaseHelper.COL_EVENT_TITLE,
                DatabaseHelper.COL_EVENT_DESCRIPTION, DatabaseHelper.COL_EVENT_LOCATION, DatabaseHelper.COL_EVENT_START_DATE,
                DatabaseHelper.COL_EVENT_BUDGET}, DatabaseHelper.COL_EVENT_USER_ID + " = " + user_id, null, null, null, DatabaseHelper.COL_EVENT_START_DATE + " DESC");

        cursor.moveToFirst();
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                Event event;
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_ID));
                String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_DESCRIPTION));
                String location = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_LOCATION));
                String starting_date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_START_DATE));
                float budget = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_BUDGET));
                event = new Event(id, title, description, location, starting_date, budget);
                eventArrayList.add(event);
                cursor.moveToNext();
            }
        } else {
            eventArrayList = null;
        }
        this.closeDatabase();
        return eventArrayList;
    }

    public Event getEvent(int event_id) {
        Event event;
        Cursor cursor;
        this.openDatabase();
        cursor = database.query(DatabaseHelper.EVENT_TABLE, new String[]{DatabaseHelper.COL_EVENT_ID, DatabaseHelper.COL_EVENT_USER_ID, DatabaseHelper.COL_EVENT_TITLE,
                DatabaseHelper.COL_EVENT_DESCRIPTION, DatabaseHelper.COL_EVENT_LOCATION, DatabaseHelper.COL_EVENT_START_DATE,
                DatabaseHelper.COL_EVENT_BUDGET}, DatabaseHelper.COL_EVENT_ID + " = " + event_id, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_ID));
            String title = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_TITLE));
            String description = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_DESCRIPTION));
            String location = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_LOCATION));
            String starting_date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_START_DATE));
            float budget = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_BUDGET));
            event = new Event(id, title, description, location, starting_date, budget);
        } else {
            event = null;
        }
        this.closeDatabase();
        return event;
    }

    public boolean deleteEvent(int event_id) {
        this.openDatabase();
        int result;
        result = database.delete(DatabaseHelper.EVENT_TABLE, DatabaseHelper.COL_EVENT_ID + " = " + event_id, null);
        this.closeDatabase();
        return (result > 0);
    }

    public boolean addExpense(Expense expense, int event_id) {
        this.openDatabase();
        long result;
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_EXPENSE_DETAIL, expense.getDetail());
        cv.put(DatabaseHelper.COL_EXPENSE_DATE_TIME, expense.getDate_time());
        cv.put(DatabaseHelper.COL_EXPENSE_COST, expense.getCost());
        cv.put(DatabaseHelper.COL_EXPENSE_EVENT_ID, event_id);
        result = database.insert(DatabaseHelper.EXPENSE_TABLE, null, cv);
        this.closeDatabase();
        return (result > 0);
    }

    public boolean updateExpense(Expense expense, int expense_id) {
        this.openDatabase();
        int result;
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_EXPENSE_DETAIL, expense.getDetail());
        cv.put(DatabaseHelper.COL_EXPENSE_DATE_TIME, expense.getDate_time());
        cv.put(DatabaseHelper.COL_EXPENSE_COST, expense.getCost());
        result = database.update(DatabaseHelper.EXPENSE_TABLE, cv, DatabaseHelper.COL_EXPENSE_ID + " = " + expense_id, null);
        this.closeDatabase();
        return (result > 0);
    }


    public ArrayList<Expense> getAllExpense(int event_id) {
        ArrayList<Expense> expenseArrayList = new ArrayList<Expense>();
        Cursor cursor;
        this.openDatabase();
        cursor = database.query(DatabaseHelper.EXPENSE_TABLE, new String[]{DatabaseHelper.COL_EXPENSE_ID, DatabaseHelper.COL_EXPENSE_DETAIL,
                        DatabaseHelper.COL_EXPENSE_COST, DatabaseHelper.COL_EXPENSE_DATE_TIME},
                DatabaseHelper.COL_EXPENSE_EVENT_ID + " = " + event_id, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                Expense expense;
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_EXPENSE_ID));
                String detail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EXPENSE_DETAIL));
                float cost = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COL_EXPENSE_COST));
                String date_time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EXPENSE_DATE_TIME));
                expense = new Expense(id, detail, cost, date_time);
                expenseArrayList.add(expense);
                cursor.moveToNext();
            }
        } else {
            expenseArrayList = null;
        }
        this.closeDatabase();
        return expenseArrayList;
    }

    public Expense getExpense(int expense_id) {
        Expense expense;
        Cursor cursor;
        this.openDatabase();
        cursor = database.query(DatabaseHelper.EXPENSE_TABLE, new String[]{DatabaseHelper.COL_EXPENSE_ID, DatabaseHelper.COL_EXPENSE_DETAIL,
                        DatabaseHelper.COL_EXPENSE_COST, DatabaseHelper.COL_EXPENSE_DATE_TIME},
                DatabaseHelper.COL_EXPENSE_ID + " = " + expense_id, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_EXPENSE_ID));
            String detail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EXPENSE_DETAIL));
            float cost = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COL_EXPENSE_COST));
            String date_time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EXPENSE_DATE_TIME));
            expense = new Expense(id, detail, cost, date_time);
        } else {
            expense = null;
        }
        this.closeDatabase();
        return expense;
    }

    public boolean deleteExpense(int expense_id) {
        this.openDatabase();
        int result;
        result = database.delete(DatabaseHelper.EXPENSE_TABLE, DatabaseHelper.COL_EXPENSE_ID + " = " + expense_id, null);
        this.closeDatabase();
        return (result > 0);
    }

    public boolean addMoment(Moment moment, int event_id) {
        this.openDatabase();
        long result;
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_MOMENT_IMAGE_LINK, moment.getImage_link());
        cv.put(DatabaseHelper.COL_MOMENT_CAPTION, moment.getCaption());
        cv.put(DatabaseHelper.COL_MOMENT_DATE_TIME, moment.getDate_time());
        cv.put(DatabaseHelper.COL_MOMENT_EVENT_ID, event_id);
        result = database.insert(DatabaseHelper.MOMENT_TABLE, null, cv);
        this.closeDatabase();
        return (result > 0);
    }

    public boolean updateMoment(Moment moment, int moment_id) {
        this.openDatabase();
        int result;
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_MOMENT_IMAGE_LINK, moment.getImage_link());
        cv.put(DatabaseHelper.COL_MOMENT_CAPTION, moment.getCaption());
        cv.put(DatabaseHelper.COL_MOMENT_DATE_TIME, moment.getDate_time());
        result = database.update(DatabaseHelper.MOMENT_TABLE, cv, DatabaseHelper.COL_MOMENT_ID + " = " + moment_id, null);
        this.closeDatabase();
        return (result > 0);
    }

    public ArrayList<Moment> getAllMoment(int event_id) {
        ArrayList<Moment> momentArrayList = new ArrayList<Moment>();
        Cursor cursor;
        this.openDatabase();
        cursor = database.query(DatabaseHelper.MOMENT_TABLE, new String[]{DatabaseHelper.COL_MOMENT_ID, DatabaseHelper.COL_MOMENT_IMAGE_LINK,
                        DatabaseHelper.COL_MOMENT_CAPTION, DatabaseHelper.COL_MOMENT_DATE_TIME},
                DatabaseHelper.COL_MOMENT_EVENT_ID + " = " + event_id, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null) {
            for (int i = 0; i < cursor.getCount(); i++) {
                Moment moment;
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_MOMENT_ID));
                String caption = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MOMENT_CAPTION));
                String date_time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MOMENT_DATE_TIME));
                String image_link = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MOMENT_IMAGE_LINK));
                moment = new Moment(id, caption, date_time, image_link);
                momentArrayList.add(moment);
                cursor.moveToNext();
            }
        } else {
            momentArrayList = null;
        }
        this.closeDatabase();
        return momentArrayList;
    }

    public Moment getMoment(int moment_id) {
        Cursor cursor;
        Moment moment;
        this.openDatabase();
        cursor = database.query(DatabaseHelper.MOMENT_TABLE, new String[]{DatabaseHelper.COL_MOMENT_ID, DatabaseHelper.COL_MOMENT_IMAGE_LINK,
                        DatabaseHelper.COL_MOMENT_CAPTION, DatabaseHelper.COL_MOMENT_DATE_TIME},
                DatabaseHelper.COL_MOMENT_ID + " = " + moment_id, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_MOMENT_ID));
            String caption = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MOMENT_CAPTION));
            String date_time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MOMENT_DATE_TIME));
            String image_link = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MOMENT_IMAGE_LINK));
            moment = new Moment(id, caption, date_time, image_link);
        } else {
            moment = null;
        }
        return moment;
    }

    public boolean deleteMoment(int moment_id) {
        this.openDatabase();
        int result;
        result = database.delete(DatabaseHelper.MOMENT_TABLE, DatabaseHelper.COL_MOMENT_ID + " = " + moment_id, null);
        this.closeDatabase();
        return (result > 0);
    }

    public float getBudget(int event_id) {
        Cursor cursor;
        float budget;
        this.openDatabase();
        cursor = database.query(DatabaseHelper.EVENT_TABLE, new String[]{DatabaseHelper.COL_EVENT_BUDGET}, DatabaseHelper.COL_EVENT_ID + "=" + event_id, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null) {
            budget = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_BUDGET));
        } else {
            budget = 0;
        }
        this.closeDatabase();
        return budget;
    }

    public boolean addMoreBudget(int event_id, float extra_budget) {
        this.openDatabase();
        float budget;
        Cursor cursor;
        cursor = database.query(DatabaseHelper.EVENT_TABLE, new String[]{DatabaseHelper.COL_EVENT_BUDGET}, DatabaseHelper.COL_EVENT_ID + "=" + event_id, null, null, null, null);
        cursor.moveToFirst();
        if (cursor != null) {
            budget = cursor.getFloat(cursor.getColumnIndex(DatabaseHelper.COL_EVENT_BUDGET));
        } else {
            budget = 0;
        }
        float total_budget;
        total_budget = budget+extra_budget;
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_EVENT_BUDGET, total_budget);
        int result = database.update(DatabaseHelper.EVENT_TABLE, cv, DatabaseHelper.COL_EVENT_ID + " = " + event_id, null);
        this.closeDatabase();
        return (result > 0);
    }

    public float getTotalExpense(int event_id) {
        Cursor cursor;
        float totalExpense;
        this.openDatabase();
        String sql = "SELECT SUM (" + DatabaseHelper.COL_EXPENSE_COST + ") FROM " + DatabaseHelper.EXPENSE_TABLE + " WHERE " + DatabaseHelper.COL_EXPENSE_EVENT_ID + "=" + event_id;
        cursor = database.rawQuery(sql, null);
        cursor.moveToFirst();
        if (cursor != null) {
            totalExpense = cursor.getLong(0);
        } else {
            totalExpense = 0;
        }
        this.closeDatabase();
        return totalExpense;
    }
}




