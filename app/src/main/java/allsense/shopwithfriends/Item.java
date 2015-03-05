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

    public static void reportSale(final String name, final String seller, final User friend1, final User friend2) {
        Item item = addItem(name, seller);
        itemDataSource.reportSale(item, friend1, friend2);
        Log.d("SWF", friend1 + " reported sale to " + friend2 + ": " + item);
    }

    public static Item addItem(final String name, final String seller) {
        return itemDataSource.createItem(name, seller);
    }

    private final String name;
    private final String seller;
    private final long id;

    public Item(final String name, final String seller, final long id) {
        if (name == null) {
            throw new IllegalArgumentException("item name is null");
        }
        if (seller == null) {
            throw new IllegalArgumentException("seller name is null");
        }
        this.name = name;
        this.seller = seller;
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
        return "(" + name + ", " + seller + ", " + id + ")";
    }
}
