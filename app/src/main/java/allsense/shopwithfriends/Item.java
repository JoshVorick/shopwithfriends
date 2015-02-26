package allsense.shopwithfriends;

import android.content.Context;

import java.util.List;

public class Item {
    private static Context appContext;
    private static ItemDataSource itemDataSource;

    /**
     * Called in SWFApplication
     * @param context
     */
    public static void init(final Context context) {
        if (appContext == null) {
            appContext = context;
            itemDataSource = new ItemDataSource(context);
            itemDataSource.open();
        }
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

    public static List<Item> registeredSales(User user) {
        return itemDataSource.registered(user);
    }

    public static Item reportSale(final String name, final String seller, final User friend1, final User friend2) {
        return itemDataSource.reportSale(name, seller, friend1, friend2);
    }

    public static void registerItem(final Item item, final User user, final int maxPrice) {
        itemDataSource.registerInterest(item, user, maxPrice);
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
