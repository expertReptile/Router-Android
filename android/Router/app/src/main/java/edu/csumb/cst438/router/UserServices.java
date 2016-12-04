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
        Log.d("UserServices", "UserServices constructor completed");
    }

    public static void updateUserBio(String newBio) {
        ContentValues values = new ContentValues();
        values.put("Bio", newBio);
        update(values);
        Log.d("UserServices", "updateUserBio completed");
    }

    public static void updateUserName(String newName) {
        ContentValues values = new ContentValues();
        values.put("Username", newName);
        update(values);
        Log.d("UserServices", "updateUserName completed");
    }

    public static void updateUserPrivacy(String newPrivacy) {
        ContentValues values = new ContentValues();
        values.put("Privacy", newPrivacy);
        update(values);
        Log.d("UserServices", "updateUserPrivacy completed");
    }

    public static void updateUserEmail(String newEmail) {
        ContentValues values = new ContentValues();
        values.put("Email", newEmail);
        update(values);
        Log.d("UserServices", "updateUserEmail completed");
    }

    public static void updateUserId(String newId) {
        ContentValues values = new ContentValues();
        values.put("UserId", newId);
        update(values);
        Log.d("UserServices", "updateUserId completed");
    }

    public static void updateUserUnits(String newUnit) {
        ContentValues values = new ContentValues();
        values.put("Units", newUnit);
        update(values);
        Log.d("UserServices", "updateUserUnits completed");
    }

    public static String getUserPrivacy() {
        String query = String.format("SELECT * FROM UserSettings WHERE 1");
        Cursor c= db.rawQuery(query, null);

        if(c.getCount() == 0) {
            Log.d("DeBra", "something went wrong!");
            return null;
        }

        c.moveToFirst();
        Log.d("UserServices", "getUserPrivacy completed");
        return c.getString(3);
    }

    public static String getUserEmail() {
        String query = String.format("SELECT * FROM UserSettings WHERE 1");
        Cursor c= db.rawQuery(query, null);

        if(c.getCount() == 0) {
            Log.d("DeBra", "something went wrong!");
            return null;
        }

        c.moveToFirst();
        Log.d("UserServices", "getUserEmail completed");
        return c.getString(4);
    }

    public static String getUserName() {
        String query = String.format("SELECT * FROM UserSettings WHERE 1");
        Cursor c= db.rawQuery(query, null);

        if(c.getCount() == 0) {
            Log.d("DeBra", "something went wrong!");
            return null;
        }

        c.moveToFirst();
        Log.d("UserServices", "getUserName completed");
        return c.getString(1);
    }

    public static String getUserId() {
        String query = String.format("SELECT * FROM UserSettings WHERE 1");
        Cursor c= db.rawQuery(query, null);

        if(c.getCount() == 0) {
            Log.d("DeBra", "something went wrong!");
            return null;
        }

        c.moveToFirst();
        Log.d("UserServices", "getUserId completed");
        return c.getString(5);
    }

    public static String getUserUnits() {
        String query = String.format("SELECT * FROM UserSettings WHERE 1");
        Cursor c= db.rawQuery(query, null);

        if(c.getCount() == 0) {
            Log.d("DeBra", "something went wrong!");
            return null;
        }

        c.moveToFirst();
        Log.d("UserServices", "getUserUnits completed");
        return c.getString(6);
    }

    private static void insert(final String tableName, final ContentValues values) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.insert(tableName, "null", values);
            }
        }).start();
        Log.d("UserServices", "insert completed");
    }

    public static void update(final ContentValues values) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.update(SQLiteHelper.UserSettings.TABLE_NAME, values, null, null);
            }
        }).start();
        Log.d("UserServices", "update completed");
    }

    public static void CreateLocalUser(String username, String bio, String privacy, String email, String userId, String units) {
        ContentValues values = new ContentValues();
        values.put("Username", username);
        values.put("Bio", bio);
        values.put("Privacy", privacy);
        values.put("Email", email);
        values.put("userId", userId);
        values.put("Units", units);

        deleteLocalUser();
        insert("UserSettings", values);
        Log.d("UserServices", "CreateLocalUser completed");
    }

    public static String getUserBio() {
        String query = String.format("SELECT * FROM UserSettings WHERE 1");
        Cursor c= db.rawQuery(query, null);

        if(c.getCount() == 0) {
            Log.d("DeBra", "something went wrong!");
            return null;
        }

        c.moveToFirst();
        Log.d("UserServices", "getUserBio completed");
        return c.getString(2);
    }

    public static void deleteLocalUser() {
        String table = "UserSettings";
        String where = "1";
        db.delete(table, where, null);
        Log.d("UserServices", "deleteLocalUser completed");
    }
}