package allsense.shopwithfriends;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class Interest {
    private static InterestDataSource interestDataSource;

    public static void init(final Context context) {
        interestDataSource = new InterestDataSource(context);
    }

    /**
     * Called in SWFApplication
     */
    public static void deinit() {
        if (interestDataSource != null) {
            interestDataSource.close();
        }
    }

//    public static List<Interest> allInterests() {
//        return interestDataSource.allInterests();
//    }

    public static void registerInterest(final User user, final String name, final int price) {
        Interest interest = addInterest(name, price);
        interestDataSource.registerInterest(interest, user);
        Log.d("SWF", "registered " + interest + " to user " + user);
    }

    private static Interest addInterest(final String name, final int price) {
        return interestDataSource.createInterest(name, price);
    }

    public static Interest interestForID(final long id) {
        return interestDataSource.interestForID(id);
    }

    private final String name;
    private final int price;
    private final long id;

    public Interest(final String name, final int price, final long id) {
        if (name == null) {
            throw new IllegalArgumentException("item name is null");
        }
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public int price() {
        return price;
    }

    public void delete() {
        interestDataSource.deleteInterest(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Interest) {
            Interest other = (Interest) o;
            return other.id == this.id;
        }
        return false;
    }

    @Override
    public String toString() {
        return name + ": " + price;
    }
}
