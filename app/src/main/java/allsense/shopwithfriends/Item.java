package allsense.shopwithfriends;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class Item {
    private static ItemDataSource itemDataSource;

    public static void init(final Context context) {
        itemDataSource = new ItemDataSource(context);
    }

    /**
     * Called in SWFApplication
     */
    public static void deinit() {
        if (itemDataSource != null) {
            itemDataSource.close();
        }
    }

    public static List<Item> allSales() {
        return itemDataSource.allItems();
    }

    public static List<Item> allRelevantSales(User user) {
        return itemDataSource.allRelevantItems(user);
    }

    public static Item itemForID(final long id) {
        return itemDataSource.itemForID(id);
    }

    public static void reportSale(final String name, final String seller, final int price, final float latitude, final float longitude, final User friend) {
        Item item = addItem(name, seller, price, latitude, longitude);
        itemDataSource.reportSale(item, friend);
        Log.d("SWF", friend + " reported sale of: " + item);
    }

    public static Item addItem(final String name, final String seller, final int price, final float latitude, final float longitude) {
        return itemDataSource.createItem(name, seller, price, latitude, longitude);
    }

    private final String name;
    private final String seller;
    private final float latitude;
    private final float longitude;
    private final int price;
    private final long id;

    public Item(final String name, final String seller, final int price, final float latitude, final float longitude, final long id) {
        if (name == null) {
            throw new IllegalArgumentException("item name is null");
        }
        if (seller == null) {
            throw new IllegalArgumentException("seller name is null");
        }
        this.name = name;
        this.seller = seller;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
    }

    public String name() {
        return name;
    }

    public String seller() {
        return seller;
    }

    public int price() {
        return price;
    }

    public float latitude() {
        return latitude;
    }

    public float longitude() {
        return longitude;
    }

    public long id() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Item) {
            Item other = (Item) o;
            return other.id == this.id;
        }
        return false;
    }

    @Override
    public String toString() {
        return name + ": " + price;
    }
}
