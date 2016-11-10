package edu.csumb.cst438.router;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * Created by pico on 11/1/16.
 */

public class Application extends android.app.Application {

    public static SQLiteHelper.DeBra dbUtil;
    public static SQLiteDatabase db;
    public static Context context;


    public void onCreate() {
        super.onCreate();
        Application.context = getApplicationContext();
        instantiate();
    }

    public void instantiate() {
        Log.d("Application", "start first");
        new Thread(new Runnable() {
            @Override
            public void run() {
                setUpDbUtil();
            }
        }).start();
    }

    private void setUpDbUtil() {
        Log.d("Application", "setting up dbUtil");
        this.dbUtil = new SQLiteHelper.DeBra(context);
        Log.d("Application", "is null " + Boolean.toString(this.dbUtil == null));
        Log.d("Application", "start get writeable");
        this.db = dbUtil.getWritableDatabase();
        Log.d("Application", "got writeable " + Boolean.toString(this.db != null));
    }

    public SQLiteDatabase getDB() {
        return db;
    }
}
