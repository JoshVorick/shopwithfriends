package allsense.shopwithfriends;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public static final String TABLE_FRIENDS = "friends";
    public static final String COLUMN_FRIEND_1 = "id1";
    public static final String COLUMN_FRIEND_2 = "id2";

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE_USERS =
            "create table " +
                    TABLE_USERS +
                    "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_NAME + " text not null, " +
                    COLUMN_EMAIL + " text not null, " +
                    COLUMN_USERNAME + " text not null, " +
                    COLUMN_PASSWORD + " text not null" +
                    ");"
            ;
    private static final String DATABASE_CREATE_FRIENDS =
            "create table " +
                    TABLE_FRIENDS +
                    "(" +
                    COLUMN_FRIEND_1 + " integer, " +
                    COLUMN_FRIEND_2 + " integer" +
                    ");"
            ;

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_USERS);
        database.execSQL(DATABASE_CREATE_FRIENDS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        deleteAll(db);
    }

    public void deleteAll(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FRIENDS);
        onCreate(db);
    }
}
