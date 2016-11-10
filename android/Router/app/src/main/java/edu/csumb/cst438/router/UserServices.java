package edu.csumb.cst438.router;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * Created by pico on 11/4/16.
 */

public class UserServices extends Services{

    public UserServices(SQLiteDatabase db) {
        super(db);
    }

    public void updateUserBio(String newBio) {
        String query = String.format("UPDATE UserSettings SET Bio = '%s' WHERE 1", newBio);
        update(query);
    }

    public void updateUserName(String newName) {
        String query = String.format("UPDATE UserSettings SET Username = '%s' WHERE 1", newName);
        update(query);
    }

    public void updateUserPrivacy(String newPrivacy) {
        String query = String.format("UPDATE UserSettings SET Privacy = '%s' WHERE 1", newPrivacy);
        update(query);
    }

    public void updateUserEmail(String newEmail) {
        String query = String.format("UPDATE UserSettings SET Email = '%s' WHERE 1", newEmail);
        update(query);
    }

    public void updateUserId(String newId) {
        String query = String.format("UPDATE UserSettings SET UserId = '%s' WHERE 1", newId);
        update(query);
    }

    public String getUserPrivacy() {
        String query = String.format("SELECT * FROM UserSettings WHERE 1");
        Cursor c= db.rawQuery(query, null);

        if(c.getCount() == 0) {
            Log.d("DeBra", "something went wrong!");
            return null;
        }

        c.moveToFirst();
        return c.getString(3);
    }

    public String getUserEmail() {
        String query = String.format("SELECT * FROM UserSettings WHERE 1");
        Cursor c= db.rawQuery(query, null);

        if(c.getCount() == 0) {
            Log.d("DeBra", "something went wrong!");
            return null;
        }

        c.moveToFirst();
        return c.getString(4);
    }

    public String getUserName() {
        String query = String.format("SELECT * FROM UserSettings WHERE 1");
        Cursor c= db.rawQuery(query, null);

        if(c.getCount() == 0) {
            Log.d("DeBra", "something went wrong!");
            return null;
        }

        c.moveToFirst();
        return c.getString(1);
    }

    public String getUserId() {
        String query = String.format("SELECT * FROM UserSettings WHERE 1");
        Cursor c= db.rawQuery(query, null);

        if(c.getCount() == 0) {
            Log.d("DeBra", "something went wrong!");
            return null;
        }

        c.moveToFirst();
        return c.getString(5);
    }

    private void insert(final String tableName, final ContentValues values) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.insert(tableName, "null", values);
            }
        }).start();
    }

    public void update(final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.execSQL(query);
                Log.d("test", query);
            }
        }).start();
    }

    public void CreateLocalUser(String username, String bio, String privacy, String email, String userId) {
        ContentValues values = new ContentValues();
        values.put("Username", username);
        values.put("Bio", bio);
        values.put("Privacy", privacy);
        values.put("Email", email);
        values.put("userId", userId);

        deleteLocalUser();
        insert("UserSettings", values);
    }
    public String getUserBio() {
        String query = String.format("SELECT * FROM UserSettings WHERE 1");
        Cursor c= db.rawQuery(query, null);

        if(c.getCount() == 0) {
            Log.d("DeBra", "something went wrong!");
            return null;
        }

        c.moveToFirst();
        return c.getString(2);
    }

    public void deleteLocalUser() {
        String query = "DELETE FROM UserSettings WHERE 1";
        db.rawQuery(query, null);
    }

}
