package allsense.shopwithfriends;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.TextView;


public class DisplayInterestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_interest);

        Interest interest = Interest.interestForID(getIntent().getLongExtra("id", 0));

        TextView nameTextView = (TextView) findViewById(R.id.display_interest_name);
        nameTextView.setText("Name: " + interest.name());
        TextView priceTextView = (TextView) findViewById(R.id.display_interest_price);
        priceTextView.setText("Price: " + interest.price());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
