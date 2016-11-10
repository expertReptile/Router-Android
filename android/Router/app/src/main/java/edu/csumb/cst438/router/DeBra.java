package edu.csumb.cst438.router;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by pico on 11/3/16.
 */

public class DeBra {

    public static SQLiteDatabase dbwrite;

    public DeBra(SQLiteDatabase db) {

        dbwrite = db;
    }

    private void rawQuery(final String query) {
        dbwrite.rawQuery(query, null);
    }

    public void update(final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbwrite.rawQuery(query, null);
            }
        }).start();
    }
}
