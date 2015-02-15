package allsense.shopwithfriends;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
        User.deleteAllUsers();
        logAllUsers();
    }

    private void logAllUsers() {
        Log.d("SWF", "all users: " + User.allUsers());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        logAllUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
