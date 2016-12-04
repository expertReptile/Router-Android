package edu.csumb.cst438.router;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by pico on 11/8/16.
 */

public class Services {

    public static SQLiteDatabase db;

    public Services(SQLiteDatabase db) {
        Log.d("Services", "Services constructor completed");
        this.db = db;
    }
}
