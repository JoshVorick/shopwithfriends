package allsense.shopwithfriends;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class RegisterActivity extends ActionBarActivity {

    private EditText mPasswordView;
    private EditText mUsernameView;
    private EditText mNameView;
    private EditText mEmailView;

    private UserRegisterTask mRegisterTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mPasswordView = (EditText) findViewById(R.id.passwordEditText);
        mUsernameView = (EditText) findViewById(R.id.usernameEditText);
        mNameView = (EditText) findViewById(R.id.nameEditText);
        mEmailView = (EditText) findViewById(R.id.emailEditText);

        SWFApplication.AUTO_LOGIN = false;
    }

    /**
     * The method for adding a user when the register button is clicked
     * With valid parameters in the three fields, a new user will be added to the "database"
     * @param view The view given by the button
     */
    public void attemptRegistration(View view) {
        if (mRegisterTask == null) {
            mRegisterTask = new UserRegisterTask();
            mRegisterTask.execute();
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
        View errorFocusView;
        CharSequence passwordError;
        CharSequence usernameError;
        CharSequence emailError;
        CharSequence nameError;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                // Simulate network access.
                Thread.sleep(1000);

                String name = mNameView.getText().toString();
                String email = mEmailView.getText().toString();
                String username = mUsernameView.getText().toString();
                String password = mPasswordView.getText().toString();

                if (!User.isValidPassword(password)) {
                    passwordError = getText(R.string.register_invalid_password);
                    errorFocusView = mPasswordView;
                }

                if (User.usernameExists(username)) {
                    usernameError = getText(R.string.register_username_exists);
                    errorFocusView = mUsernameView;
                } else if (!User.isValidUsername(username)) {
                    usernameError = getText(R.string.register_invalid_username);
                    errorFocusView = mUsernameView;
                }

                if (!User.isValidEmail(email)) {
                    emailError = getText(R.string.register_invalid_email);
                    errorFocusView = mEmailView;
                }

                if (!User.isValidName(name)) {
                    nameError = getText(R.string.register_invalid_name);
                    errorFocusView = mNameView;
                }
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                if (errorFocusView == null) {
                    mNameView.setText("");
                    mEmailView.setText("");
                    mUsernameView.setText("");
                    mPasswordView.setText("");
                    mRegisterTask = null;

                    Intent intent = new Intent(RegisterActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                } else {
                    errorFocusView.requestFocus();
                    if (nameError != null) {
                        mNameView.setError(nameError);
                    }
                    if (emailError != null) {
                        mEmailView.setError(emailError);
                    }
                    if (usernameError != null) {
                        mUsernameView.setError(usernameError);
                    }
                    if (passwordError != null) {
                        mPasswordView.setError(passwordError);
                    }
                }
            }
            mRegisterTask = null;
        }

        @Override
        protected void onCancelled() {
            mRegisterTask = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
