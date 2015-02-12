package allsense.shopwithfriends;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class User {
    public static List<User> allUsers;
    private static final String fileName = "USERS";
    private static Context appContext;

    public static User currentUser;

    public static int numUsers() {
        return allUsers.size();
    }

    public static void init(final Context context) {
        if (appContext == null) {
            appContext = context;
            allUsers = new ArrayList<User>();
            FileInputStream in;
            try {
                in = appContext.openFileInput(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                while (true) {
                    String name = br.readLine();
                    if (name == null || name.length() == 0) {
                        break;
                    }
                    String email = br.readLine();
                    String username = br.readLine();
                    String password = br.readLine();
                    User user = new User(name, email, username, password);
                    addUser(user);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearUsers() {
        Log.d("SWF", "clear all users");
        FileOutputStream out;
        try {
            out = appContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        allUsers.clear();
    }

    public static void addUser(final User user) {
        allUsers.add(user);

        FileOutputStream out;

        try {
            out = appContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            out.write(user.name.getBytes());
            out.write('\n');
            out.write(user.email.getBytes());
            out.write('\n');
            out.write(user.username.getBytes());
            out.write('\n');
            out.write(user.password.getBytes());
            out.write('\n');
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("SWF", "add user " + user);
    }

    public static boolean usernameExists(final String username) {
        for (User user : allUsers) {
            if (user.username().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static boolean usernamePasswordMatch(final String username, final String password) {
        for (User user : allUsers) {
            if (user.username().equals(username)) {
                return user.password().equals(password);
            }
        }
        return false;
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

    private String name;
    private String email;
    private String username;
    private String password;

    public User(final String name, final String email, final String username, final String password) {
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

    @Override
    public String toString() {
        return username;
    }
}
