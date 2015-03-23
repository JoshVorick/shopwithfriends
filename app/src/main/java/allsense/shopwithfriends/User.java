package allsense.shopwithfriends;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class User {
    private static UserDataSource userDataSource;
    private static ItemDataSource itemDataSource;
    private static InterestDataSource interestDataSource;
    private static User currentUser;

    public static User currentUser() {
        if (currentUser == null) {
            Log.e("SWF", "currentUser is null");
        }
        return currentUser;
    }

    public static void setCurrentUser(final User user) {
        currentUser = user;
        Log.d("SWF", "setting current user to " + user);
    }

    public static void init(final Context context) {
        userDataSource = new UserDataSource(context);
        itemDataSource = new ItemDataSource(context);
        interestDataSource = new InterestDataSource(context);
    }

    /**
     * Called in SWFApplication
     */
    public static void deinit() {
        if (userDataSource != null) {
            userDataSource.close();
        }
        if (itemDataSource != null) {
            itemDataSource.close();
        }
    }

    public static List<User> allUsers() {
        return userDataSource.allUsers();
    }

    public static void deleteAllUsers() {
        Log.d("SWF", "delete all users");
        userDataSource.deleteAllUsers();
    }

    public static User addUser(final String name, final String email, final String username, final String password) {
        User user = userDataSource.createUser(name, email, username, password);
        Log.d("SWF", "add user " + user);
        return user;
    }

    public static boolean usernameExists(final String username) {
        for (User user : allUsers()) {
            if (user.username().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static User userForUsername(final String username) {
        return userDataSource.userForUsername(username);
    }

    public static User userForID(final long id) {
        return userDataSource.userForID(id);
    }

    public static boolean isValidEmail(final String email) {
        return email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9._%+-]+\\.[A-Za-z0-9]+");
    }

    public static boolean isValidUsername(final String username) {
        return username.length() >= 2;
    }

    public static boolean isValidName(final String name) {
        return name.length() >= 2;
    }

    public static boolean isValidPassword(final String password) {
        return password.length() >= 4;
    }

    private final String name;
    private final String email;
    private final String username;
    private final String password;
    private final long id;

    public User(final String name, final String email, final String username, final String password, final long id) {
        if (name == null) {
            throw new IllegalArgumentException("name is null");
        }
        if (email == null) {
            throw new IllegalArgumentException("email is null");
        }
        if (username == null) {
            throw new IllegalArgumentException("username is null");
        }
        if (password == null) {
            throw new IllegalArgumentException("password is null");
        }
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public long id() {
        return id;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public String name() {
        return name;
    }

    public String email() {
        return email;
    }

    /**
     *
     * @return the average rating of this user
     */
    public int rating() {
        return userDataSource.rating(this);
    }

    public int ratingForFriend(final User friend) {
        return userDataSource.rating(this, friend);
    }

    public void rate(final User friend, final int rating) {
        userDataSource.rate(this, friend, rating);
    }

    public void addFriend(final User friend) {
        userDataSource.addFriends(this, friend);
    }

    public void deleteFriend(final User friend) {
        userDataSource.deleteFriends(this, friend);
    }

    public int getNumberSalesReportsFromFriend(final User friend) {
        return itemDataSource.salesReportedBy(friend).size();
    }

    public List<User> friends() {
        return userDataSource.friends(this);
    }

    public List<User> notFriends() {
        return userDataSource.notFriends(this);
    }

    public List<Item> reportedBy() {
        return itemDataSource.salesReportedBy(this);
    }

    public List<Interest> interests() {
        return interestDataSource.registeredInterests(this);
    }

    public List<Interest> notInterests() {
        return interestDataSource.notRegisteredInterests(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User other = (User) o;
            return other.id == this.id;
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
