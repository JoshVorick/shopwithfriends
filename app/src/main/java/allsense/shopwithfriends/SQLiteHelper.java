package allsense.shopwithfriends;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class SQLiteHelper extends SQLiteOpenHelper {
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
    public static final String ITEMS_COLUMN_PRICE = "price";
    public static final String ITEMS_COLUMN_LATITUDE = "latitude";
    public static final String ITEMS_COLUMN_LONGITUDE = "longitude";

    public static final String TABLE_REPORTED = "reported";
    public static final String REPORTED_COLUMN_ITEM_ID = "item_id";
    public static final String REPORTED_COLUMN_FRIEND_ID = "user_id"; // The person who reported the sale

    public static final String TABLE_INTERESTS = "interests";
    public static final String INTERESTS_COLUMN_ID = "_id";
    public static final String INTERESTS_COLUMN_NAME = "name";
    public static final String INTERESTS_COLUMN_PRICE = "price";

    public static final String TABLE_REGISTERED = "registered";
    public static final String REGISTERED_COLUMN_USER_ID = "user_id";
    public static final String REGISTERED_COLUMN_INTEREST_ID = "interest_id";

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String[] ALL_TABLES = {
            TABLE_INTERESTS,
            TABLE_FRIENDS,
            TABLE_ITEMS,
            TABLE_REGISTERED,
            TABLE_REPORTED,
            TABLE_USERS
    };

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
                    ITEMS_COLUMN_SELLER + " text not null, " +
                    ITEMS_COLUMN_PRICE + " integer," +
                    ITEMS_COLUMN_LATITUDE + " float," +
                    ITEMS_COLUMN_LONGITUDE + " float" +
                    ");"
            ;

    private static final String DATABASE_CREATE_REPORTED =
            "create table if not exists " +
                    TABLE_REPORTED +
                    "(" +
                    REPORTED_COLUMN_FRIEND_ID + " integer, " +
                    REPORTED_COLUMN_ITEM_ID + " integer" +
                    ");"
            ;

    private static final String DATABASE_CREATE_REGISTERED =
            "create table if not exists " +
                    TABLE_REGISTERED +
                    "(" +
                    REGISTERED_COLUMN_USER_ID + " integer, " +
                    REGISTERED_COLUMN_INTEREST_ID + " integer" +
                    ");"
            ;

    private static final String DATABASE_CREATE_INTERESTS =
            "create table if not exists " +
                    TABLE_INTERESTS +
                    "(" +
                    INTERESTS_COLUMN_ID + " integer primary key autoincrement, " +
                    INTERESTS_COLUMN_NAME + " text not null, " +
                    INTERESTS_COLUMN_PRICE + " integer" +
                    ")"
            ;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String[] creates = {
                DATABASE_CREATE_USERS,
                DATABASE_CREATE_FRIENDS,
                DATABASE_CREATE_ITEMS,
                DATABASE_CREATE_INTERESTS,
                DATABASE_CREATE_REPORTED,
                DATABASE_CREATE_REGISTERED,
        };

        for (String create : creates) {
            database.execSQL(create);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
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
        for (String table : ALL_TABLES) {
            db.delete(table, null, null);
        }
    }

    /**
     * deletes all tables in the database
     * @param db  the database
     */
    public void deleteDatabase(SQLiteDatabase db) {
        for (String table : ALL_TABLES) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
    }

    public static void resetDatabase(final Context context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        helper.deleteDatabase(database);
        helper.onCreate(database);
    }
}
