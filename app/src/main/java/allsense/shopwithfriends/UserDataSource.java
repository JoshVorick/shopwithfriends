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
            MySQLiteHelper.COLUMN_RATING,
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
        Cursor cursor = queryUsers(MySQLiteHelper.COLUMN_ID + " = " + insertID);
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

    public void resetDatabase() {
        Log.d("SWF", "reset database");
        dbHelper.deleteDatabase(database);
        dbHelper.onCreate(database);
    }

    public Cursor queryUsers(final String selection) {
        return database.query(MySQLiteHelper.TABLE_USERS, ALL_COLUMNS_USERS, selection, null, null, null, null);
    }

    public Cursor queryFriends(final String selection) {
        return database.query(MySQLiteHelper.TABLE_FRIENDS, ALL_COLUMNS_FRIENDS, selection, null, null, null, null);
    }

    public List<User> friends(final User user) {
        List<User> friends = new ArrayList<User>();

        Cursor cursor = queryFriends(MySQLiteHelper.COLUMN_FRIEND_1 + " = " + user.id());

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            long friendID = cursor.getLong(1);
            User friend = userForID(friendID);
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
        dbHelper.deleteAllData(database);
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<User>();

        Cursor cursor = queryUsers(null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = userAtCursor(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        cursor.close();

        return users;
    }

    public User userForUsername(final String username) {
        Cursor cursor = queryUsers(MySQLiteHelper.COLUMN_USERNAME + " = " + '\'' + username + '\'');
        cursor.moveToFirst();
        try {
            if (!cursor.isAfterLast()) {
                return userAtCursor(cursor);
            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
    }

    public User userForID(final long id) {
        Cursor cursor = queryUsers(MySQLiteHelper.COLUMN_ID + " = " + id);
        cursor.moveToFirst();
        try {
            if (!cursor.isAfterLast()) {
                return userAtCursor(cursor);
            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
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

    public void rate(final User rater, final User rated, final int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("illegal rating: " + rating);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySQLiteHelper.COLUMN_RATING, rating);
        database.update(MySQLiteHelper.TABLE_FRIENDS, contentValues,
                MySQLiteHelper.COLUMN_FRIEND_1 + " = " + rater.id() +
                        " AND " + MySQLiteHelper.COLUMN_FRIEND_2 + " = " + rated.id()
                , null);
    }

    public int rating(final User user) {
        Cursor cursor = queryFriends(MySQLiteHelper.COLUMN_FRIEND_2 + " = " + user.id());
        cursor.moveToFirst();
        double total = 0.0;
        int count = 0;
        while (!cursor.isAfterLast()) {
            int rating = cursor.getInt(2);
            if (rating != 0) {
                total += rating;
                count++;
            }
            cursor.moveToNext();
        }
        cursor.close();
        if (count == 0) {
            return 0;
        } else {
            double rating = total / count;
            return (int) (rating + 0.5);
        }
    }

    public void addFriends(final User user1, final User user2) {
        Cursor cursor = queryFriends(MySQLiteHelper.COLUMN_FRIEND_1 + " = " + user1.id() +
                " AND " + MySQLiteHelper.COLUMN_FRIEND_2 + " = " + user2.id());

        try {
            if (!cursor.isAfterLast()) {
                Log.e("SWF", user1 + " and " + user2 + " are already friends");
                return;
            }
        } finally {
            cursor.close();
        }

        ContentValues values1 = new ContentValues();
        values1.put(MySQLiteHelper.COLUMN_FRIEND_1, user1.id());
        values1.put(MySQLiteHelper.COLUMN_FRIEND_2, user2.id());
        values1.put(MySQLiteHelper.COLUMN_RATING, 0);

        ContentValues values2 = new ContentValues();
        values2.put(MySQLiteHelper.COLUMN_FRIEND_1, user2.id());
        values2.put(MySQLiteHelper.COLUMN_FRIEND_2, user1.id());
        values2.put(MySQLiteHelper.COLUMN_RATING, 0);

        // both will be executed or else none if error
        database.beginTransaction();
        try {
            database.insert(MySQLiteHelper.TABLE_FRIENDS, null, values1);
            database.insert(MySQLiteHelper.TABLE_FRIENDS, null, values2);
            database.setTransactionSuccessful();
            Log.d("SWF", user1 + " and " + user2 + "are now friends");
        } catch (Exception e) {
            Log.d("SWF", "adding friends failed");
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }
}
