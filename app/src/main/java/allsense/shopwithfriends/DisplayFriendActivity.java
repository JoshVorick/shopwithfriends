package allsense.shopwithfriends;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class DisplayFriendActivity extends ActionBarActivity {

    private User friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_friend);

        friend = User.userForID(getIntent().getLongExtra("id", 0));

        setTitle(friend.username());

        setTextViews();
    }

    private void setTextViews() {
        TextView nameTextView = (TextView) findViewById(R.id.display_friend_name);
        nameTextView.setText("Name: " + friend.name());
        TextView emailTextView = (TextView) findViewById(R.id.display_friend_email);
        emailTextView.setText("Email: " + friend.email());
        TextView ratingTextView = (TextView) findViewById(R.id.display_friend_rating);
        ratingTextView.setText("Rating: " + friend.rating());
    }

    public void rate1(View view) {
        User.currentUser.rate(friend, 1);
        setTextViews();
    }

    public void rate2(View view) {
        User.currentUser.rate(friend, 2);
        setTextViews();
    }

    public void rate3(View view) {
        User.currentUser.rate(friend, 3);
        setTextViews();
    }

    public void rate4(View view) {
        User.currentUser.rate(friend, 4);
        setTextViews();
    }

    public void rate5(View view) {
        User.currentUser.rate(friend, 5);
        setTextViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
