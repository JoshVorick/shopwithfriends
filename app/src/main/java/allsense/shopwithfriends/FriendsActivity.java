package allsense.shopwithfriends;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class FriendsActivity extends ActionBarActivity {

    private ListView listView;
    private ArrayAdapter<User> adapter;
    private List<User> adapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // TODO: right now this simply displays all the users that aren't the current user
        adapterList = new ArrayList<User>(User.allUsers);
        // remove current user
        for (int i = 0; i < adapterList.size(); i++) {
            User user = adapterList.get(i);
            if (user == User.currentUser) {
                adapterList.remove(i);
                i--;
            }
        }

        adapter = new ArrayAdapter<User>(this, R.layout.activity_friends_cell, adapterList);

        listView = (ListView) findViewById(R.id.friends_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void friendsChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
