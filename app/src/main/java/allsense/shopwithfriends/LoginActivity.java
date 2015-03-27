package allsense.shopwithfriends;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class LoginActivity extends ActionBarActivity {

    private UserLoginTask mAuthTask = null;

    private EditText mUsernameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameView = (EditText) findViewById(R.id.login_username);
        mPasswordView = (EditText) findViewById(R.id.login_password);

        Button mEmailSignInButton = (Button) findViewById(R.id.login_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        if (SWFApplication.AUTO_LOGIN) {
            SWFApplication.AUTO_LOGIN = false;
            UserDataSource dataSource = new UserDataSource(getApplicationContext());
            User user = dataSource.userForID(1);
            dataSource.close();
            if (user != null) {
                mUsernameView.setText(user.username());
                mPasswordView.setText(user.password());
                attemptLogin();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        UserLoginTask task = new UserLoginTask();
        task.execute();
    }

    private class UserLoginTask extends AsyncTask<Void, Void, User> {

        @Override
        protected User doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(1000);
                return User.userForUsername(mUsernameView.getText().toString());
            } catch (InterruptedException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final User user) {
            String password = mPasswordView.getText().toString();

            View errorFocusView = null;

            if (user != null) {
                if (!user.password().equals(password)) {
                    errorFocusView = mPasswordView;
                    mPasswordView.setError(getString(R.string.login_error_incorrect_password));
                }
            } else {
                mUsernameView.setError(getString(R.string.login_error_invalid_username));
                errorFocusView = mUsernameView;
            }

            if (errorFocusView != null) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                errorFocusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.
                User.setCurrentUser(user);
                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }

            mAuthTask = null;
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}



