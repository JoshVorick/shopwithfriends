package allsense.shopwithfriends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;


public class MainMenuActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    public void toFriends(View view) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    public void toSales(View view) {
        Intent intent = new Intent(this, SalesActivity.class);
        startActivity(intent);
    }

    public void toInterests(View view) {
        Intent intent = new Intent(this, InterestsActivity.class);
        startActivity(intent);
    }
}
