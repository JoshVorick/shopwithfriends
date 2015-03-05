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


public class SalesActivity extends ActionBarActivity {

    private ListView listView;
    private ArrayAdapter<Item> adapter;
    private List<Item> adapterList;
    private boolean viewingRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        adapterList = Item.allSales();
        Log.d("SWF", "current sales: " + adapterList);

        adapter = new ArrayAdapter<Item>(this, R.layout.list_view_cell, adapterList);

        listView = (ListView) findViewById(R.id.sales_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("SWF", "selected item " + adapterList.get(position));
                displaySale(adapterList.get(position));
            }
        });
    }

    public void displaySale(final Item item) {
        Intent intent = new Intent(this, DisplaySaleActivity.class);
        intent.putExtra("id", item.id());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SWF", "refresh sales list");
        refreshList();
    }

    public void refreshList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sales, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sales_menu_report_sale:
                startActivity(new Intent(this, ReportSaleActivity.class));
                return true;
            case R.id.sales_menu_register_interest:
                startActivity(new Intent(this, RegisterInterestActivity.class));
                return true;
            case R.id.sales_menu_registered:
                viewingRegistered = !viewingRegistered;
                if (viewingRegistered) {
                    adapterList = User.currentUser().reportedTo();
                    Log.d("SWF", "Registered list: " + adapterList);
                    item.setTitle(R.string.sales_menu_all);
                } else {
                    adapterList = Item.allSales();
                    Log.d("SWF", "Full list: " + adapterList);
                    item.setTitle(R.string.sales_menu_registered);
                }
                refreshList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
