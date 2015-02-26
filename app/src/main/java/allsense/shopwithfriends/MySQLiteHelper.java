package allsense.shopwithfriends;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_USERS = "users";
    public static final String USERS_COLUMN_ID = "_id";
    public static final String USERS_COLUMN_NAME = "name";
    public static final String USERS_COLUMN_EMAIL = "email";
    public static final String USERS_COLUMN_USERNAME = "username";
    public static final String USERS_COLUMN_PASSWORD = "password";

    public static final String TABLE_FRIENDS = "friends";
    public static final String FRIENDS_COLUMN_FRIEND_ID_1 = "id1"; // the rater
    public static final String FRIENDS_COLUMN_FRIEND_ID_2 = "id2"; // the rated
    public static final String FRIENDS_COLUMN_RATING = "rating";

    public static final String TABLE_ITEMS = "items";
    public static final String ITEMS_COLUMN_ID = "_id";
    public static final String ITEMS_COLUMN_NAME = "name";
    public static final String ITEMS_COLUMN_SELLER = "seller";

    public static final String TABLE_REPORTED = "reported";
    public static final String REPORTED_COLUMN_ITEM_ID = "item_id";
    //from user 1 to user 2
    public static final String REPORTED_COLUMN_FRIEND_ID_1 = "user_id1";
    public static final String REPORTED_COLUMN_FRIEND_ID_2 = "user_id2";

    public static final String TABLE_REGISTERED = "registered";
    public static final String REGISTERED_COLUMN_ITEM_ID = "item_id";
    public static final String REGISTERED_COLUMN_USER_ID = "user_id";
    public static final String REGISTERED_COLUMN_MAX_PRICE = "max_price";

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    // statements to create the tables
    private static final String DATABASE_CREATE_USERS =
            "create table if not exists " +
                    TABLE_USERS +
                    "(" +
                    USERS_COLUMN_ID + " integer primary key autoincrement, " +
                    USERS_COLUMN_NAME + " text not null, " +
                    USERS_COLUMN_EMAIL + " text not null, " +
                    USERS_COLUMN_USERNAME + " text not null, " +
                    USERS_COLUMN_PASSWORD + " text not null" +
                    ");"
            ;
    private static final String DATABASE_CREATE_FRIENDS =
            "create table if not exists " +
                    TABLE_FRIENDS +
                    "(" +
                    FRIENDS_COLUMN_FRIEND_ID_1 + " integer, " +
                    FRIENDS_COLUMN_FRIEND_ID_2 + " integer, " +
                    FRIENDS_COLUMN_RATING + " integer" +
                    ");"
            ;
    private static final String DATABASE_CREATE_ITEMS =
            "create table if not exists " +
                    TABLE_ITEMS +
                    "(" +
                    ITEMS_COLUMN_ID + " integer primary key autoincrement, " +
                    ITEMS_COLUMN_NAME + " text not null, " +
                    ITEMS_COLUMN_SELLER + " text not null" +
                    ");"
            ;

    private static final String DATABASE_CREATE_REPORTED =
            "create table if not exists " +
                    TABLE_REPORTED +
                    "(" +
                    REPORTED_COLUMN_ITEM_ID + " integer, " +
                    REPORTED_COLUMN_FRIEND_ID_1 + " integer, " +
                    REPORTED_COLUMN_FRIEND_ID_2 + " integer" +
                    ");"
            ;

    private static final String DATABASE_CREATE_REGISTERED =
            "create table if not exists " +
                    TABLE_REGISTERED +
                    "(" +
                    REGISTERED_COLUMN_ITEM_ID + " integer, " +
                    REGISTERED_COLUMN_USER_ID + " integer, " +
                    REGISTERED_COLUMN_MAX_PRICE + " integer" +
                    ");"
            ;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_USERS);
        database.execSQL(DATABASE_CREATE_FRIENDS);
        database.execSQL(DATABASE_CREATE_ITEMS);
        database.execSQL(DATABASE_CREATE_REPORTED);
        database.execSQL(DATABASE_CREATE_REGISTERED);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        deleteDatabase(db);
        onCreate(db);
    }

    /**
     * deletes all rows in all tables, but not the tables themselves
     * @param db  the database
     */
    public void deleteAllData(SQLiteDatabase db) {
        db.delete(TABLE_USERS, null, null);
        db.delete(TABLE_FRIENDS, null, null);
        db.delete(TABLE_ITEMS, null, null);
        db.delete(TABLE_REPORTED, null, null);
        db.delete(TABLE_REGISTERED, null, null);
    }

    /**
     * deletes all tables in the database
     * @param db  the database
     */
    public void deleteDatabase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORTED);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTERED);
    }
}
