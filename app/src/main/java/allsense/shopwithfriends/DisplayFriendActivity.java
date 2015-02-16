package allsense.shopwithfriends;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DisplayFriendActivity extends ActionBarActivity {

    private User friend;
    private int rating;
    private Button[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_friend);

        friend = User.userForID(getIntent().getLongExtra("id", 0));
        buttons = new Button[5];
        buttons[0] = (Button) findViewById(R.id.display_friend_button_1);
        buttons[1] = (Button) findViewById(R.id.display_friend_button_2);
        buttons[2] = (Button) findViewById(R.id.display_friend_button_3);
        buttons[3] = (Button) findViewById(R.id.display_friend_button_4);
        buttons[4] = (Button) findViewById(R.id.display_friend_button_5);

        rating = User.currentUser.ratingForFriend(friend);

        setTitle(friend.username());

        setUI();
    }

    private void setUI() {
        TextView nameTextView = (TextView) findViewById(R.id.display_friend_name);
        nameTextView.setText("Name: " + friend.name());
        TextView emailTextView = (TextView) findViewById(R.id.display_friend_email);
        emailTextView.setText("Email: " + friend.email());
        TextView ratingTextView = (TextView) findViewById(R.id.display_friend_rating);
        ratingTextView.setText("Rating: " + friend.rating());

        for (Button button : buttons) {
            button.setBackgroundColor(
                    getResources().getColor(R.color.background_material_light));
        }
        if (rating >= 1 && rating <= 5) {
            buttons[rating - 1].setBackgroundColor(
                    getResources().getColor(R.color.background_dark));
        }
    }

    public void rate1(View view) {
        rating = 1;
        User.currentUser.rate(friend, rating);
        setUI();
    }

    public void rate2(View view) {
        rating = 2;
        User.currentUser.rate(friend, rating);
        setUI();
    }

    public void rate3(View view) {
        rating = 3;
        User.currentUser.rate(friend, rating);
        setUI();
    }

    public void rate4(View view) {
        rating = 4;
        User.currentUser.rate(friend, rating);
        setUI();
    }

    public void rate5(View view) {
        rating = 5;
        User.currentUser.rate(friend, rating);
        setUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
