package allsense.shopwithfriends;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class RegisterInterestActivity extends ActionBarActivity {

    private EditText mNameTextView;
    private EditText mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register_interest);

        mNameTextView = (EditText) findViewById(R.id.register_interest_item_name_edit_text);
        mPriceTextView = (EditText) findViewById(R.id.register_interest_price_edit_text);

        Log.d("SWF", "register interest");
    }

    public void registerItem(View view) {
        View errorView = null;
        CharSequence nameError = null;
        CharSequence priceError = null;

        String priceText = mPriceTextView.getText().toString();
        int price = 0;

        try {
            price = Integer.parseInt(priceText);
        } catch (NumberFormatException e) {
            errorView = mPriceTextView;
            priceError = getString(R.string.register_interest_invalid_price);
        }

        String name = mNameTextView.getText().toString();
        if (name.isEmpty()) {
            errorView = mNameTextView;
            nameError = getString(R.string.register_interest_invalid_item_name);
        }

        if (errorView != null) {
            errorView.requestFocus();
            if (priceError != null) {
                mPriceTextView.setError(priceError);
            }
            if (nameError != null) {
                mNameTextView.setError(nameError);
            }
        } else {
            Interest.registerInterest(User.currentUser(), name, price);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
