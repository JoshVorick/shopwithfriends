package allsense.shopwithfriends;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;


public class DisplaySaleActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sale);

        Item item = Item.itemForID(getIntent().getLongExtra("id", 0));
        Log.d("SWF", "Item to be displayed: " + item.name());

        if (item != null) {
            TextView nameTextView = (TextView) findViewById(R.id.display_sale_name);
            nameTextView.setText("Name: " + item.name());
            TextView sellerTextView = (TextView) findViewById(R.id.display_sale_seller);
            sellerTextView.setText("Seller: " + item.seller());
            TextView priceTextView = (TextView) findViewById(R.id.display_sale_price);
            priceTextView.setText("Price: " + item.price());
        } else {
            Log.d("SWF", "Cannot display sale of null item");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
