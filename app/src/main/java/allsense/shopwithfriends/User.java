package allsense.shopwithfriends;

import android.content.Context;
import android.util.Log;

import java.util.List;

public class User {
    private static Context appContext;
    private static UserDataSource dataSource;

    public static User currentUser() {
        return currentUser;
    }

    public static void setCurrentUser(final User user) {
        currentUser = user;
        Log.d("SWF", "setting current user to " + user);
    }

    private static User currentUser;

    public static void init(final Context context) {
        if (appContext == null) {
            appContext = context;
            dataSource = new UserDataSource(context);
            dataSource.open();
        }
    }

    public static void deinit() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    public static List<User> allUsers() {
        return dataSource.allUsers();
    }

    public static void deleteAllUsers() {
        Log.d("SWF", "delete all users");
        dataSource.deleteAllUsers();
    }

    public static User addUser(final String name, final String email, final String username, final String password) {
        User user = dataSource.createUser(name, email, username, password);
        Log.d("SWF", "add user " + user);
        return user;
    }

    public static List<User> currentFriends() {
        return dataSource.friends(currentUser());
    }

    public static List<User> currentNotFriends() {
        return dataSource.notFriends(currentUser());
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
        return dataSource.userForUsername(username);
    }

    public static User userForID(final long id) {
        return dataSource.userForID(id);
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

    public long id() {
        return id;
    }

    /**
     *
     * @return the average rating of this user
     */
    public int rating() {
        return dataSource.rating(this);
    }

    /**
     *
     * @param friend
     * @return what this user rated the friend
     */
    public int ratingForFriend(final User friend) {
        return dataSource.rating(this, friend);
    }

    public void rate(final User friend, final int rating) {
        dataSource.rate(this, friend, rating);
    }

    public void addFriend(final User friend) {
        dataSource.addFriends(this, friend);
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
//        return username + ": " + name;
        return "(" + name + ", " + email + ", " + username + ", " + password + ", " + id + ")";
    }
}
