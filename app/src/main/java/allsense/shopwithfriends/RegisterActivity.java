package allsense.shopwithfriends;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;


public class RegisterActivity extends ActionBarActivity {

    private EditText mPasswordView;
    private EditText mUsernameView;
    private EditText mNameView;
    private EditText mEmailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mPasswordView = (EditText) findViewById(R.id.passwordEditText);
        mUsernameView = (EditText) findViewById(R.id.usernameEditText);
        mNameView = (EditText) findViewById(R.id.nameEditText);
        mEmailView = (EditText) findViewById(R.id.emailEditText);
    }

    /**
     * The method for adding a user when the register button is clicked
     * With valid parameters in the three fields, a new user will be added to the "database"
     * @param view The view given by the button
     */
    public void attemptRegistration(View view) {
        String name = mNameView.getText().toString();
        String email = mEmailView.getText().toString();
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        View errorFocusView = null;

        if (!User.isValidPassword(password)) {
            mUsernameView.setError(getText(R.string.register_invalid_password));
            errorFocusView = mPasswordView;
        }

        if (User.usernameExists(username)) {
            mUsernameView.setError(getText(R.string.register_username_exists));
            errorFocusView = mUsernameView;
        } else if (!User.isValidUsername(username)) {
            mUsernameView.setError(getText(R.string.register_invalid_username));
            errorFocusView = mUsernameView;
        }

        if (!User.isValidEmail(email)) {
            mEmailView.setError(getText(R.string.register_invalid_email));
            errorFocusView = mEmailView;
        }

        if (!User.isValidName(name)) {
            mNameView.setError(getText(R.string.register_invalid_name));
            errorFocusView = mNameView;
        }

        if (errorFocusView != null) {
            errorFocusView.requestFocus();
        } else {
            User user = User.addUser(name, email, username, password);
            User.currentUser = user;

            mNameView.setText("");
            mEmailView.setText("");
            mUsernameView.setText("");
            mPasswordView.setText("");

            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
