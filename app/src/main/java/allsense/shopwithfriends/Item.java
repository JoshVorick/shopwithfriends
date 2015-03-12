package allsense.shopwithfriends;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class Item {
    private static ItemDataSource itemDataSource;

    /**
     * Called in SWFApplication
     * @param context
     */
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

    public static void reportSale(final String name, final String seller, final int price, final User friend) {
        Item item = addItem(name, seller, price);
        itemDataSource.reportSale(item, friend);
        Log.d("SWF", friend + " reported sale of: " + item);
    }

    public static Item addItem(final String name, final String seller, final int price) {
        return itemDataSource.createItem(name, seller, price);
    }

    private final String name;
    private final String seller;
    private final int price;
    private final long id;

    public Item(final String name, final String seller, final int price, final long id) {
        if (name == null) {
            throw new IllegalArgumentException("item name is null");
        }
        if (seller == null) {
            throw new IllegalArgumentException("seller name is null");
        }
        this.name = name;
        this.seller = seller;
        this.price = price;
        this.id = id;
    }

    public long id() {
        return id;
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
