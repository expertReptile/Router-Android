package edu.csumb.cst438.router;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by pico on 10/15/16.
 */

public class SQLiteHelper {

    public SQLiteHelper() {}

    public static abstract class UserSettings implements BaseColumns {
        public static final String TABLE_NAME = "UserSettings";
        public static final String COLLUMN_NAME_USERNAME = "Username";
        public static final String COLLUMN_NAME_BIO = "Bio";
        public static final String COLLUM_NAME_PRIVACY = "Privacy";
        public static final String COLLUMN_NAME_EMAIL = "Email";
        public static final String COLLUMN_NAME_USERID = "UserId";
    }

    public static abstract class Routes implements BaseColumns {
        public static final String TABLE_NAME = "Routes";
        public static final String COLLUMN_NAME_ROUTEID = "RouteId";
        public static final String COLLUMN_NAME_ROUTE = "Route";
        public static final String COLLUMN_NAME_START_POINT_LAT = "StartPointLat";
        public static final String COLLUMN_NAME_START_POINT_LON = "StartPointLon";
        public static final String COLLUMN_NAME_ROUTE_NAME = "RouteName";
        public static final String COLLUMN_NAME_USER_ID = "UserId";
    }

    private static final String TEXT_STYPE = " TEXT";
    private static final String COMMA_SEP = " ,";

    private static final String CREATE_USER_SETTINGS =
            "CREATE TABLE " + UserSettings.TABLE_NAME + " (" +
                UserSettings._ID + " INTEGER PRIMARY KEY, " +
                UserSettings.COLLUMN_NAME_USERNAME + TEXT_STYPE + COMMA_SEP +
                UserSettings.COLLUMN_NAME_BIO + TEXT_STYPE + COMMA_SEP +
                UserSettings.COLLUM_NAME_PRIVACY + TEXT_STYPE + COMMA_SEP +
                UserSettings.COLLUMN_NAME_EMAIL + TEXT_STYPE + COMMA_SEP +
                    UserSettings.COLLUMN_NAME_USERID + ")";

    private static final String DELETE_USER_SETTINGS =
            "DROP TABLE IF EXISTS " + UserSettings.TABLE_NAME;

    private static final String CREATE_ROUTES =
            "CREATE TABLE " + Routes.TABLE_NAME + " (" +
                Routes._ID + " INTEGER PRIMARY KEY, " +
                Routes.COLLUMN_NAME_ROUTEID + TEXT_STYPE + COMMA_SEP +
                Routes.COLLUMN_NAME_ROUTE + TEXT_STYPE + COMMA_SEP +
                Routes.COLLUMN_NAME_START_POINT_LAT + TEXT_STYPE + COMMA_SEP +
                Routes.COLLUMN_NAME_START_POINT_LON + TEXT_STYPE + COMMA_SEP +
                Routes.COLLUMN_NAME_ROUTE_NAME + TEXT_STYPE + COMMA_SEP +
                Routes.COLLUMN_NAME_USER_ID + TEXT_STYPE +" )";

    private static final String DELETE_ROUTES =
            "DROP TABLE IF EXISTS " + Routes.TABLE_NAME;

    private static final String INSERT_INTO_ROUTES = "INSERT INTO " + Routes.TABLE_NAME +
            " (" + Routes.COLLUMN_NAME_ROUTEID + COMMA_SEP + Routes.COLLUMN_NAME_ROUTE +
            COMMA_SEP + Routes.COLLUMN_NAME_ROUTE_NAME + COMMA_SEP + Routes.COLLUMN_NAME_START_POINT_LAT +
            COMMA_SEP + Routes.COLLUMN_NAME_START_POINT_LON + COMMA_SEP + Routes.COLLUMN_NAME_USER_ID + ") " +
            "VALUES ( %s, %s, %s, %s, %s, %s)";

    private static final String UPDATE_USER_SETTINGS = "UPDATE " + UserSettings.TABLE_NAME +
            " SET %s = %s WHERE 1";

    private static final String INIT_USER_SETTINGS = "INSERT INTO " + UserSettings.TABLE_NAME +
            " (" + UserSettings.COLLUMN_NAME_USERNAME + COMMA_SEP + UserSettings.COLLUM_NAME_PRIVACY +
            COMMA_SEP + UserSettings.COLLUMN_NAME_BIO + COMMA_SEP + UserSettings.COLLUMN_NAME_EMAIL + COMMA_SEP +
            UserSettings.COLLUM_NAME_PRIVACY + ") " + "VALUES (%s, %s, %s, %s)";

    private static final String SELECT_FROM_ROUTES = "SELECT * FROM " + Routes.TABLE_NAME + " WHERE %s = %s";

    public static class DeBra extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "DeBra.db";

        public DeBra(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_USER_SETTINGS);
            db.execSQL(CREATE_ROUTES);

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DELETE_USER_SETTINGS);
            db.execSQL(DELETE_ROUTES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}

