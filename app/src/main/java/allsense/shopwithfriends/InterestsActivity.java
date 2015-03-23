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


public class InterestsActivity extends ActionBarActivity {

    private ArrayAdapter<Interest> adapter;
    private List<Interest> adapterList;
    private boolean removing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interests);

        adapterList = User.currentUser().interests();
        Log.d("SWF", "current user interests: " + adapterList);

        adapter = new ArrayAdapter<>(this, R.layout.list_view_cell, adapterList);

        ListView listView = (ListView) findViewById(R.id.interests_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (removing) {
                    Interest interest = adapterList.remove(position);
                    interest.delete();
                    Log.d("SWF", "deleted interest " + interest);
                    refreshList();
                } else {
                    Log.d("SWF", "selected interest " + adapterList.get(position));
                    displayInterest(adapterList.get(position));
                }
            }
        });
    }
    public void displayInterest(final Interest interest) {
        Intent intent = new Intent(this, DisplayInterestActivity.class);
        intent.putExtra("id", interest.id());
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        refreshList();
        Log.d("SWF", "onRestart");
    }

    public void refreshList() {
        adapterList.clear();
        adapterList.addAll(User.currentUser().interests());
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_interests, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.interests_menu_add_interest:
                Intent intent = new Intent(this, RegisterInterestActivity.class);
                startActivity(intent);
                break;
            case R.id.interests_menu_remove_interests:
                removing = !removing;
                if (removing) {
                    item.setTitle(R.string.interests_menu_display_interests);
                } else {
                    item.setTitle(R.string.interests_menu_remove_interests);
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
