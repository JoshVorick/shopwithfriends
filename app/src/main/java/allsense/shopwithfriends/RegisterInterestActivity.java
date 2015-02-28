package allsense.shopwithfriends;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


public class RegisterInterestActivity extends ActionBarActivity {

    private ListView listView;
    private ArrayAdapter<Item> adapter;
    private List<Item> adapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_interest);
        adapterList = User.currentNotRegistered();
        Log.d("SWF", "current unregistered items: " + adapterList);

        adapter = new ArrayAdapter<Item>(this, R.layout.list_view_cell, adapterList);

        listView = (ListView) findViewById(R.id.register_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Item registered = adapterList.remove(position);
                Item.registerItem(registered, User.currentUser(), 1000);
                Log.d("SWF", "register interest in " + registered);
                Log.d("SWF", "refresh unregistered items list");
                refreshList();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SWF", "refresh not registered items list");
        refreshList();
    }

    public void refreshList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register_interest, menu);
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
