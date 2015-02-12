package allsense.shopwithfriends;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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
        User.clearUsers();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        User.init(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
