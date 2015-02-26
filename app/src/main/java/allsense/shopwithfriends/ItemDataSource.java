package allsense.shopwithfriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ItemDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    private static final String[] ALL_COLUMNS_ITEMS = {
            MySQLiteHelper.ITEMS_COLUMN_ID,
            MySQLiteHelper.ITEMS_COLUMN_NAME,
            MySQLiteHelper.ITEMS_COLUMN_SELLER,
    };

    private static final String[] ALL_COLUMNS_REPORTED = {
            MySQLiteHelper.REPORTED_COLUMN_ITEM_ID,
            MySQLiteHelper.REPORTED_COLUMN_FRIEND_ID_1,
            MySQLiteHelper.REPORTED_COLUMN_FRIEND_ID_2,
    };

    private static final String[] ALL_COLUMNS_REGISTERED = {
            MySQLiteHelper.REGISTERED_COLUMN_ITEM_ID,
            MySQLiteHelper.REGISTERED_COLUMN_USER_ID,
            MySQLiteHelper.REGISTERED_COLUMN_MAX_PRICE,
    };

    public ItemDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    /**
     * allows the database to be used
     * @throws SQLiteException
     */
    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * closes the database
     */
    public void close() {
        dbHelper.close();
    }


    /**
     * returns all columns in the table ITEMS that match the selection
     * @param selection  the condition to match against, null for all rows
     * @return  all rows that match selection
     */
    public Cursor queryItems(final String selection) {
        return database.query(MySQLiteHelper.TABLE_ITEMS, ALL_COLUMNS_ITEMS, selection, null, null, null, null);
    }

    public Cursor queryReported(final String selection) {
        return database.query(MySQLiteHelper.TABLE_REPORTED, ALL_COLUMNS_REPORTED, selection, null, null, null, null);
    }

    public Cursor queryRegistered(final String selection) {
        return database.query(MySQLiteHelper.TABLE_REGISTERED, ALL_COLUMNS_REGISTERED, selection, null, null, null, null);
    }

    /**
     * returns the item at the cursor position
     * @param cursor
     * @return  the item at the current position of the cursor
     */
    private Item itemAtCursor(Cursor cursor) {
        long id = cursor.getLong(0);
        String name = cursor.getString(1);
        String seller = cursor.getString(2);
        Item item = new Item(name, seller, id);
        return item;
    }

    /**
     * creates item to be put into database
     * @param name
     * @return the created item
     */
    public Item createItem(final String name, final String seller) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.ITEMS_COLUMN_NAME, name);
        values.put(MySQLiteHelper.ITEMS_COLUMN_SELLER, seller);
        long insertID = database.insert(MySQLiteHelper.TABLE_ITEMS, null, values);
        Cursor cursor = queryItems(MySQLiteHelper.ITEMS_COLUMN_ID + " = " + insertID);
        cursor.moveToFirst();
        Item item = itemAtCursor(cursor);
        cursor.close();
        return item;
    }

    /**
     * creates item to be put into both ITEMS database and REPORTED database
     * @param name
     * @return the created item
     */
    public Item reportSale(final String name, final String seller, final User friend1, final User friend2) {
        Item item = createItem(name, seller);
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.REPORTED_COLUMN_ITEM_ID, item.id());
        values.put(MySQLiteHelper.REPORTED_COLUMN_FRIEND_ID_1, friend1.id());
        values.put(MySQLiteHelper.REPORTED_COLUMN_FRIEND_ID_2, friend2.id());
        database.insert(MySQLiteHelper.TABLE_REPORTED, null, values);
        return item;
    }

    /**
     * puts existing item with user into REGISTERED database
     * @param item
     * @param user
     * @return the registered item
     */
    public void registerInterest(final Item item, final User user, final int maxPrice) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.REGISTERED_COLUMN_ITEM_ID, item.id());
        values.put(MySQLiteHelper.REGISTERED_COLUMN_USER_ID, user.id());
        values.put(MySQLiteHelper.REGISTERED_COLUMN_USER_ID, maxPrice);
        database.insert(MySQLiteHelper.TABLE_REGISTERED, null, values);
    }

    /**
     * deletes the specified item from the database
     * @param item
     */
    public void deleteItem(final Item item) {
        long id = item.id();
        System.out.println("deleting item " + item);
        database.delete(MySQLiteHelper.TABLE_ITEMS, MySQLiteHelper.ITEMS_COLUMN_ID + " = " + id, null);
    }

    /**
     * remakes the whole database, deleting rows in the process
     */
    public void resetDatabase() {
        Log.d("SWF", "reset item database");
        dbHelper.deleteDatabase(database);
        dbHelper.onCreate(database);
    }

    /**
     *
     * @return  a list of all users in the database USERS
     */
    public List<Item> allItems() {
        List<Item> items = new ArrayList<Item>();

        Cursor cursor = queryItems(null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = itemAtCursor(cursor);
            items.add(item);
            cursor.moveToNext();
        }
        cursor.close();

        return items;
    }

    /**
     * returns a list of reported items a user has received
     * @param user
     * @return  a list of reported items a user has received
     */
    public List<Item> reportedTo(final User user) {
        List<Item> items = new ArrayList<Item>();

        Cursor cursor = queryReported(MySQLiteHelper.REPORTED_COLUMN_FRIEND_ID_2 + " = " + user.id());

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            long itemID = cursor.getLong(1);
            Item item = itemForID(itemID);
            items.add(item);
            cursor.moveToNext();
        }

        cursor.close();

        return items;
    }

    /**
     * returns a list of reported items friend 1 has given to friend 2
     * @param friend1
     * @param friend2
     * @return  a list of reported items friend 1 has given to friend 2
     */
    public List<Item> reportedFromTo(final User friend1, final User friend2) {
        List<Item> items = new ArrayList<Item>();

        Cursor cursor = queryReported(MySQLiteHelper.REPORTED_COLUMN_FRIEND_ID_1 + " = " + friend1.id()
                + " AND " + MySQLiteHelper.REPORTED_COLUMN_FRIEND_ID_2 + " = " + friend2.id());

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            long itemID = cursor.getLong(1);
            Item item = itemForID(itemID);
            items.add(item);
            cursor.moveToNext();
        }

        cursor.close();

        return items;
    }

    /**
     * returns all registered items of the user according to the table REGISTERED
     * @param user  the user to find registered items
     * @return  a list of the user's registered items
     */
    public List<Item> registered(final User user) {
        List<Item> items = new ArrayList<Item>();

        Cursor cursor = queryRegistered(MySQLiteHelper.REGISTERED_COLUMN_USER_ID + " = " + user.id());

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            long itemID = cursor.getLong(1);
            Item item = itemForID(itemID);
            items.add(item);
            cursor.moveToNext();
        }

        cursor.close();

        return items;
    }

    /**
     * returns all items not in the user's registered list
     * @param user the user
     * @return  a list of any items not in the user's registered list
     */
    public List<Item> notRegistered(final User user) {
        List<Item> items = allItems();
        List<Item> subset = registered(user);
        items.removeAll(subset);
        return items;
    }

    /**
     * deletes all rows from the database
     */
    public void deleteAllItems() {
        dbHelper.deleteAllData(database);
    }

    /**
     * finds the item with the id in the table
     * @param id  the id to find
     * @return  the item with the id passed in, null if not found
     */
    public Item itemForID(final long id) {
        Cursor cursor = queryItems(MySQLiteHelper.ITEMS_COLUMN_ID + " = " + id);
        cursor.moveToFirst();
        try {
            if (cursor.isAfterLast()) {
                return null;
            } else {
                return itemAtCursor(cursor);
            }
        } finally {
            cursor.close();
        }
    }
}
