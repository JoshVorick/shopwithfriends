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


public class FriendsActivity extends ActionBarActivity {

    private ArrayAdapter<User> adapter;
    private List<User> adapterList;
    private boolean removing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_friends);

        adapterList = User.currentUser().friends();
        Log.d("SWF", "current user friends: " + adapterList);

        adapter = new ArrayAdapter<>(this, R.layout.list_view_cell, adapterList);

        ListView listView = (ListView) findViewById(R.id.friends_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (removing) {
                    User friend = adapterList.remove(position);
                    User.currentUser().deleteFriend(friend);
                    Log.d("SWF", "deleted friend " + friend);
                    Log.d("SWF", "refresh friends list");
                    refreshList();
                } else {
                    Log.d("SWF", "selected friend " + adapterList.get(position));
                    displayFriend(adapterList.get(position));
                }
            }
        });
    }

    void displayFriend(final User user) {
        Intent intent = new Intent(this, DisplayFriendActivity.class);
        intent.putExtra("id", user.id());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("SWF", "refresh friends list");
        refreshList();
    }

    void refreshList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.friends_menu_add_friend:
                Intent intent = new Intent(this, AddFriendActivity.class);
                startActivity(intent);
                return true;
            case R.id.friends_menu_remove_friend:
                removing = !removing;
                if (removing) {
                    item.setTitle(R.string.friends_menu_display_friends);
                } else {
                    item.setTitle(R.string.friends_menu_remove_friends);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
