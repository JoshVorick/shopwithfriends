package allsense.shopwithfriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

// http://www.vogella.com/tutorials/AndroidSQLite/article.html

public class UserDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private static final String[] ALL_COLUMNS_USERS = {
            MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_NAME,
            MySQLiteHelper.COLUMN_EMAIL,
            MySQLiteHelper.COLUMN_USERNAME,
            MySQLiteHelper.COLUMN_PASSWORD,
    };

    private static final String[] ALL_COLUMNS_FRIENDS = {
            MySQLiteHelper.COLUMN_FRIEND_1,
            MySQLiteHelper.COLUMN_FRIEND_2,
    };

    public UserDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public User createUser(final String name, final String email, final String username, final String password) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_NAME, name);
        values.put(MySQLiteHelper.COLUMN_EMAIL, email);
        values.put(MySQLiteHelper.COLUMN_USERNAME, username);
        values.put(MySQLiteHelper.COLUMN_PASSWORD, password);
        long insertID = database.insert(MySQLiteHelper.TABLE_USERS, null, values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, ALL_COLUMNS_USERS, MySQLiteHelper.COLUMN_ID + " = " + insertID, null, null, null, null);
        cursor.moveToFirst();
        User user = userAtCursor(cursor);
        cursor.close();
        return user;
    }

    public void deleteUser(final User user) {
        long id = user.id();
        System.out.println("deleting user " + user);
        database.delete(MySQLiteHelper.TABLE_USERS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    public List<User> friends(final User user) {
        List<User> friends = new ArrayList<User>();

        long id = user.id();
        Cursor cursor = database.query(MySQLiteHelper.TABLE_FRIENDS, ALL_COLUMNS_FRIENDS, MySQLiteHelper.COLUMN_FRIEND_1 + " = " + id + " OR " + MySQLiteHelper.COLUMN_FRIEND_2 + " = " + id, null, null, null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            long id1 = cursor.getLong(0);
            long id2 = cursor.getLong(1);
            long otherID = id1 != id ? id1 : id2;
            User friend = userForID(otherID);
            friends.add(friend);
            cursor.moveToNext();
        }
        cursor.close();

        return friends;
    }

    public List<User> notFriends(final User user) {
        List<User> allUsers = allUsers();
        List<User> friends = friends(user);
        allUsers.remove(user);
        allUsers.removeAll(friends);
        return allUsers;
    }

    public void deleteAllUsers() {
        dbHelper.deleteAll(database);
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<User>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, ALL_COLUMNS_USERS, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = userAtCursor(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        cursor.close();

        return users;
    }

    public User userForID(final long id) {
        Cursor cursor = database.query(MySQLiteHelper.TABLE_USERS, ALL_COLUMNS_USERS, MySQLiteHelper.COLUMN_ID + " = " + id, null, null, null, null);
        cursor.moveToFirst();
        User user = null;
        while (!cursor.isAfterLast()) {
            if (user == null) {
                user = userAtCursor(cursor);
            } else {
                Log.e("SWF", "two users with same id");
                Log.e("SWF", user.toString());
                Log.e("SWF", userAtCursor(cursor).toString());
                Log.e("SWF", "all users: " + allUsers());
                throw new RuntimeException();
            }
            cursor.moveToNext();
        }
        return user;
    }

    private User userAtCursor(Cursor cursor) {
        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        String email = cursor.getString(2);
        String username = cursor.getString(3);
        String password = cursor.getString(4);
        User user = new User(name, email, username, password, id);
        return user;
    }

    public void addFriends(final User user1, final User user2) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_FRIEND_1, user1.id());
        values.put(MySQLiteHelper.COLUMN_FRIEND_2, user2.id());
        database.insert(MySQLiteHelper.TABLE_FRIENDS, null, values);
    }
}
