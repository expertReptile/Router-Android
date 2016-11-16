package edu.csumb.cst438.router;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;


/**
 * Created by pico on 11/4/16.
 */

public class UserServices extends Services{

    public UserServices() {
        super(Application.db);
    }

    public void updateUserBio(String newBio) {
        ContentValues values = new ContentValues();
        values.put("Bio", newBio);
        update(values);
    }

    public void updateUserName(String newName) {
        ContentValues values = new ContentValues();
        values.put("Username", newName);
        update(values);
    }

    public void updateUserPrivacy(String newPrivacy) {
        ContentValues values = new ContentValues();
        values.put("Privacy", newPrivacy);
        update(values);
    }

    public void updateUserEmail(String newEmail) {
        ContentValues values = new ContentValues();
        values.put("Email", newEmail);
        update(values);
    }

    public void updateUserId(String newId) {
        ContentValues values = new ContentValues();
        values.put("UserId", newId);
        update(values);
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

    public void update(final ContentValues values) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.update(SQLiteHelper.UserSettings.TABLE_NAME, values, null, null);
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
