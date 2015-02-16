package allsense.shopwithfriends;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;


public class WelcomeActivity extends ActionBarActivity {

    /**
     * Sends user to the register page
     * @param view The view given by the button
     */
    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Sends user to the login page
     * @param view The view given by the button
     */
    public void logIn(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void deleteUsers(View view) {
        new AlertDialog.Builder(this)
            .setTitle("Delete Users?")
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User.deleteAllUsers();
                            logAllUsers();
                        }
                    }).start();
                }
            })
            .setNegativeButton("No", null)
            .show();
    }

    public void resetDatabase(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Reset Database?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                UserDataSource dataSource = new UserDataSource(getApplicationContext());
                                dataSource.open();
                                dataSource.resetDatabase();
                                dataSource.close();
                            }
                        }).start();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void logAllUsers() {
        Log.d("SWF", "all users: " + User.allUsers());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        logAllUsers();
        if (SWFApplication.AUTO_LOGIN) {
            UserDataSource dataSource = new UserDataSource(getApplicationContext());
            dataSource.open();
            User user = dataSource.userForID(1);
            dataSource.close();
            if (user != null) {
                logIn(null);
            } else {
                register(null);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
