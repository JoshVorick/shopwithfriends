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
    private ArrayList<String> regs;

    /**
     * The method for adding a user when the register button is clicked
     * With valid parameters in the three fields, a new user will be added to the "database"
     * @param view The view given by the button
     */
    public void reger(View view) {
        String pw = mPasswordView.getText().toString();
        String user = mUsernameView.getText().toString();
        String name = mNameView.getText().toString();
        try {
            BufferedReader read = new BufferedReader(new InputStreamReader(openFileInput("users")));
            while (read.ready()) {
                regs.add(read.readLine());
            }
        } catch (Exception e) {

        }

        regs.add(user + ":" + pw);

        try {
            PrintStream out = new PrintStream(openFileOutput("users", Context.MODE_PRIVATE));
            for (String a : regs) {
                out.println(a);
            }
        } catch (Exception e) {

        }
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mPasswordView = (EditText) findViewById(R.id.editText2);
        mUsernameView = (EditText) findViewById(R.id.editText);
        mNameView = (EditText) findViewById(R.id.editText3);
        regs = new ArrayList<String>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
