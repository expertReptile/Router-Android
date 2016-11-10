package edu.csumb.cst438.router;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by pico on 11/8/16.
 */

public class Services {

    public SQLiteDatabase db;

    public Services(SQLiteDatabase db) {
        this.db = db;
    }
}
