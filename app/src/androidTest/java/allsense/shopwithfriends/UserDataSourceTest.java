package allsense.shopwithfriends;


import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class UserDataSourceTest extends ActivityInstrumentationTestCase2<WelcomeActivity> {

    public UserDataSourceTest() {
        super(WelcomeActivity.class);
    }

    // Arthur Eubanks
    public void testAddFriends() {
        Log.e("Test", "UserDataSourceTest.testAddFriends");

        UserDataSource source = new UserDataSource(getActivity().getApplicationContext());
        User user1 = User.addUser("name1", "email1", "user1", "pass1");
        User user2 = User.addUser("name2", "email2", "user2", "pass2");

        assertFalse(user1.friends().contains(user2));
        assertFalse(user2.friends().contains(user1));
        source.addFriends(user1, user2);
        assertTrue(user1.friends().contains(user2));
        assertTrue(user2.friends().contains(user1));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        UserDataSource source = new UserDataSource(getActivity().getApplicationContext());
        for (User u : source.allUsers()) {
            if (u.name().contains("name")) {
                source.deleteUser(u);
            }
        }
    }
}