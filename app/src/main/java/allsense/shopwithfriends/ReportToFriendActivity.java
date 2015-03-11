package allsense.shopwithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class ReportToFriendActivity extends ActionBarActivity {

    private ListView listView;
    private ArrayAdapter<User> adapter;
    private List<User> adapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_to_friend);

        adapterList = User.currentUser().friends();
        Log.d("SWF", "current user friends: " + adapterList);

        adapter = new ArrayAdapter<User>(this, R.layout.list_view_cell, adapterList);

        listView = (ListView) findViewById(R.id.friends_report_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get itemName and seller from ReportSaleActivity
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    User friend = adapterList.get(position);
                    String itemName = extras.getString("itemName");
                    String seller = extras.getString("seller");
                    String price = extras.getString("price");
                    Item.reportSale(itemName, seller, price, User.currentUser(), friend);
                }
                Intent intent = new Intent(ReportToFriendActivity.this, SalesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SWF", "refresh friends list");
        refreshList();
    }

    public void refreshList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_report_to_friend, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
