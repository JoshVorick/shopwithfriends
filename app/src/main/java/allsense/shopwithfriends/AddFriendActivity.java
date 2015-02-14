package allsense.shopwithfriends;

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


public class AddFriendActivity extends ActionBarActivity {

    private ListView listView;
    private ArrayAdapter<User> adapter;
    private List<User> adapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_friend);

        adapterList = User.currentNotFriends();
        Log.d("SWF", "users current user can add as friend: " + adapterList);

        adapter = new ArrayAdapter<User>(this, R.layout.activity_friends_cell, adapterList);

        listView = (ListView) findViewById(R.id.add_friends_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User friend = adapterList.remove(position);
                User.currentUser.addFriend(friend);
                Log.d("SWF", "added friend " + friend);
                Log.d("SWF", "refresh add friends list");
                refreshList();
            }
        });
    }

    public void refreshList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
