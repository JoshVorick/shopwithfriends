package allsense.shopwithfriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

// http://www.vogella.com/tutorials/AndroidSQLite/article.html

class UserDataSource {
    private final SQLiteDatabase database;
    private final SQLiteHelper dbHelper;

    private static final String[] ALL_COLUMNS_USERS = {
            SQLiteHelper.USERS_COLUMN_ID,
            SQLiteHelper.USERS_COLUMN_NAME,
            SQLiteHelper.USERS_COLUMN_EMAIL,
            SQLiteHelper.USERS_COLUMN_USERNAME,
            SQLiteHelper.USERS_COLUMN_PASSWORD,
    };

    private static final String[] ALL_COLUMNS_FRIENDS = {
            SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_1,
            SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_2,
            SQLiteHelper.FRIENDS_COLUMN_RATING,
    };

    public UserDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    /**
     * closes the database
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * creates a user with the parameters, stores it in the database, and returns it
     * @param name  the name
     * @param email  the email
     * @param username  the username
     * @param password  the password
     * @return  the new user
     */
    public User createUser(final String name, final String email, final String username, final String password) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.USERS_COLUMN_NAME, name);
        values.put(SQLiteHelper.USERS_COLUMN_EMAIL, email);
        values.put(SQLiteHelper.USERS_COLUMN_USERNAME, username);
        values.put(SQLiteHelper.USERS_COLUMN_PASSWORD, password);
        long insertID = database.insert(SQLiteHelper.TABLE_USERS, null, values);
        Cursor cursor = queryUsers(SQLiteHelper.USERS_COLUMN_ID + " = " + insertID);
        cursor.moveToFirst();
        User user = userAtCursor(cursor);
        cursor.close();
        return user;
    }

    /**
     * deletes the specified user from the database
     * @param user  the user
     */
    public void deleteUser(final User user) {
        long id = user.id();
        System.out.println("deleting user " + user);
        database.delete(SQLiteHelper.TABLE_USERS, SQLiteHelper.USERS_COLUMN_ID + " = " + id, null);
    }

    /**
     * returns all columns in the table USERS that match the selection
     * @param selection  the condition to match against, null for all rows
     * @return  all rows that match selection
     */
    Cursor queryUsers(final String selection) {
        return database.query(SQLiteHelper.TABLE_USERS, ALL_COLUMNS_USERS, selection, null, null, null, null);
    }

    /**
     * returns all columns in the table FRIENDS that match the selection
     * @param selection  the condition to match against, null for all rows
     * @return  all rows that match selection
     */
    Cursor queryFriends(final String selection) {
        return database.query(SQLiteHelper.TABLE_FRIENDS, ALL_COLUMNS_FRIENDS, selection, null, null, null, null);
    }

    /**
     *
     * @return  a list of all users in the database USERS
     */
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

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

    /**
     * returns all friends of the user according to the table FRIENDS
     * @param user  the user to find friends
     * @return  a list of friends of the user
     */
    public List<User> friends(final User user) {
        List<User> friends = new ArrayList<>();

        Cursor cursor = queryFriends(SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_1 + " = " + user.id());

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

    /**
     * returns all people that are not the user and not in the user's friends
     * @param user  the user to find not friends
     * @return  a list of anyone who is not the user's friend
     */
    public List<User> notFriends(final User user) {
        List<User> allUsers = allUsers();
        List<User> friends = friends(user);
        allUsers.remove(user);
        allUsers.removeAll(friends);
        return allUsers;
    }

    /**
     * deletes all rows from the database
     */
    public void deleteAllUsers() {
        dbHelper.deleteAllData(database);
    }

    /**
     * finds the user with the id in the table USERS
     * @param id  the id to find
     * @return  the user with the id passed in, null if not found
     */
    public User userForID(final long id) {
        Cursor cursor = queryUsers(SQLiteHelper.USERS_COLUMN_ID + " = " + id);
        cursor.moveToFirst();
        try {
            if (cursor.isAfterLast()) {
                return null;
            } else {
                return userAtCursor(cursor);
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * finds the user with the username in the table USERS
     * @param username  the username to find
     * @return  the user with the username passed in, null if not found
     */
    public User userForUsername(final String username) {
        Cursor cursor = queryUsers(SQLiteHelper.USERS_COLUMN_USERNAME + " = " + '\'' + username + '\'');
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

    /**
     * returns the user at the current position of the inputted cursor
     * @param cursor  a cursor from the table USERS
     * @return  the user at the current position of the cursor
     */
    private User userAtCursor(Cursor cursor) {
        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        String email = cursor.getString(2);
        String username = cursor.getString(3);
        String password = cursor.getString(4);
        return new User(name, email, username, password, id);
    }

    /**
     * write the rating that one user gives another user
     * @param rater  the user rating
     * @param rated  the user being rated
     * @param rating  the rating
     */
    public void rate(final User rater, final User rated, final int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("illegal rating: " + rating);
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.FRIENDS_COLUMN_RATING, rating);
        database.update(SQLiteHelper.TABLE_FRIENDS, contentValues,
                SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_1 + " = " + rater.id() +
                        " AND " + SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_2 + " = " + rated.id()
                , null);
    }

    /**
     * finds the average rating of the user from all ratings not including 0
     * @param user  the user to find the rating
     * @return  the rating
     */
    public int rating(final User user) {
        Cursor cursor = queryFriends(SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_2 + " = " + user.id());
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

            Log.d("SWF", "zero ratings");
            return 0;
        } else {
            double rating = total / count;

            Log.d("SWF", "rating: " + rating);
            return (int) (rating + 0.5);
        }
    }

    /**
     * returns the rating that one user rated another user
     * @param rater  the user rating
     * @param rated  the user being rated
     * @return  the rating
     */
    public int rating(final User rater, final User rated) {
        Cursor cursor = queryFriends(
                SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_1 + " = " + rater.id() + " AND " +
                SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_2 + " = " + rated.id()
        );
        cursor.moveToFirst();
        try {
            if (cursor.isAfterLast()) {
                return 0;
            } else {
                return cursor.getInt(2);
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * makes the two users friends if they are not already friends
     * @param user1  first user
     * @param user2  second user
     */
    public void addFriends(final User user1, final User user2) {
        if (user1.id() == user2.id()) {
            Log.e("SWF", "adding self as friend");
            return;
        }

        Cursor cursor = queryFriends(SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_1 + " = " + user1.id() +
                " AND " + SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_2 + " = " + user2.id());

        //noinspection TryFinallyCanBeTryWithResources
        try {
            if (!cursor.isAfterLast()) {
                Log.e("SWF", user1 + " and " + user2 + " are already friends");
                return;
            }
        } finally {
            cursor.close();
        }

        ContentValues values1 = new ContentValues();
        values1.put(SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_1, user1.id());
        values1.put(SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_2, user2.id());
        values1.put(SQLiteHelper.FRIENDS_COLUMN_RATING, 0);

        ContentValues values2 = new ContentValues();
        values2.put(SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_1, user2.id());
        values2.put(SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_2, user1.id());
        values2.put(SQLiteHelper.FRIENDS_COLUMN_RATING, 0);

        // both will be executed or else none if error
        database.beginTransaction();
        try {
            database.insert(SQLiteHelper.TABLE_FRIENDS, null, values1);
            database.insert(SQLiteHelper.TABLE_FRIENDS, null, values2);
            database.setTransactionSuccessful();
            Log.d("SWF", user1 + " and " + user2 + "are now friends");
        } catch (Exception e) {
            Log.d("SWF", "adding friends failed");
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    /**
     * removes a user from friends
     * @param user1  first user
     * @param user2  second user
     */
    public void deleteFriends(final User user1, final User user2) {

        Cursor cursor = queryFriends(SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_1 + " = " + user1.id() +
                " AND " + SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_2 + " = " + user2.id());
        if (cursor.isAfterLast()) {
            Log.e("SWF", user1 + " and " + user2 + " have not been added to friends");
            return;
        }

        // both will be executed or else none if error
        database.beginTransaction();
        try {
            database.delete(SQLiteHelper.TABLE_FRIENDS, SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_1 + " = " + user1.id()
                    + " AND " + SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_2 + " = " + user2.id(), null);
            database.delete(SQLiteHelper.TABLE_FRIENDS, SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_1 + " = " + user2.id()
                    + " AND " + SQLiteHelper.FRIENDS_COLUMN_FRIEND_ID_2 + " = " + user1.id(), null);
            database.setTransactionSuccessful();
            Log.d("SWF", user1 + " and " + user2 + "have been removed from friends");
        } catch (Exception e) {
            Log.d("SWF", "deleting friends failed");
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }
}
