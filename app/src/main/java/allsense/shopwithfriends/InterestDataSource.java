package allsense.shopwithfriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class InterestDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    private static final String[] ALL_COLUMNS_INTERESTS = {
            SQLiteHelper.INTERESTS_COLUMN_ID,
            SQLiteHelper.INTERESTS_COLUMN_NAME,
            SQLiteHelper.INTERESTS_COLUMN_PRICE,
    };

    private static final String[] ALL_COLUMNS_REGISTERED = {
            SQLiteHelper.REGISTERED_COLUMN_USER_ID,
            SQLiteHelper.REGISTERED_COLUMN_INTEREST_ID,
    };

    public InterestDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    /**
     * closes the database
     */
    public void close() {
        dbHelper.close();
    }

    public Cursor queryInterests(final String selection) {
        return database.query(SQLiteHelper.TABLE_INTERESTS, ALL_COLUMNS_INTERESTS, selection, null, null, null, null);
    }

    public Cursor queryRegistered(final String selection) {
        return database.query(SQLiteHelper.TABLE_REGISTERED, ALL_COLUMNS_REGISTERED, selection, null, null, null, null);
    }

    private Interest interestAtCursor(Cursor cursor) {
        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        int price = cursor.getInt(2);
        return new Interest(name, price, id);
    }

    public Interest createInterest(final String name, final int price) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.INTERESTS_COLUMN_NAME, name);
        values.put(SQLiteHelper.INTERESTS_COLUMN_PRICE, price);
        long insertID = database.insert(SQLiteHelper.TABLE_INTERESTS, null, values);
        Cursor cursor = queryInterests(SQLiteHelper.INTERESTS_COLUMN_ID + " = " + insertID);
        cursor.moveToFirst();
        Interest interest = interestAtCursor(cursor);
        cursor.close();
        return interest;
    }


    public void registerInterest(final Interest interest, final User user) {
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.REGISTERED_COLUMN_INTEREST_ID, interest.id());
        values.put(SQLiteHelper.REGISTERED_COLUMN_USER_ID, user.id());
        database.insert(SQLiteHelper.TABLE_REGISTERED, null, values);
    }

    public void deleteInterest(final Interest interest) {
        long id = interest.id();
        System.out.println("deleting interest " + interest);
        database.delete(SQLiteHelper.TABLE_INTERESTS, SQLiteHelper.INTERESTS_COLUMN_ID + " = " + id, null);
    }

    public List<Interest> allInterests() {
        Cursor cursor = queryInterests(null);
        return interestsFromCursor(cursor);
    }

    private List<Interest> interestsFromCursor(final Cursor cursor) {
        List<Interest> interests = new ArrayList<Interest>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            long interestID = cursor.getLong(0);
            Interest interest = interestForID(interestID);
            interests.add(interest);
            cursor.moveToNext();
        }

        cursor.close();

        return interests;
    }

    public List<Interest> registered(final User user) {
        Cursor cursor = queryRegistered(SQLiteHelper.REGISTERED_COLUMN_USER_ID + " = " + user.id());
        cursor.moveToFirst();
        List<Interest> interests = new ArrayList<Interest>();
        while (!cursor.isAfterLast()) {
            long interestID = cursor.getLong(1);
            interests.add(interestForID(interestID));
            cursor.moveToNext();
        }
        return interests;
    }

    public List<Interest> notRegistered(final User user) {
        List<Interest> interests = allInterests();
        List<Interest> subset = registered(user);
        interests.removeAll(subset);
        return interests;
    }

    public Interest interestForID(final long id) {
        Cursor cursor = queryInterests(SQLiteHelper.INTERESTS_COLUMN_ID + " = " + id);
        cursor.moveToFirst();
        try {
            if (cursor.isAfterLast()) {
                return null;
            } else {
                return interestAtCursor(cursor);
            }
        } finally {
            cursor.close();
        }
    }
}
