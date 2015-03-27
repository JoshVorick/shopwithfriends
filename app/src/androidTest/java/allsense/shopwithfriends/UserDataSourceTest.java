package allsense.shopwithfriends;


import android.test.ActivityInstrumentationTestCase2;

public class UserDataSourceTest extends ActivityInstrumentationTestCase2<WelcomeActivity> {
    UserDataSource source;
    User user1;
    User user2;

    public UserDataSourceTest() {
        super(WelcomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        source = new UserDataSource(getActivity().getApplicationContext());
        user1 = User.addUser("name1", "email1", "user1", "pass1");
        user2 = User.addUser("name2", "email2", "user2", "pass2");
    }

    // Arthur Eubanks
    public void testAddFriends() {


        assertFalse(user1.friends().contains(user2));
        assertFalse(user2.friends().contains(user1));
        source.addFriends(user1, user2);
        assertTrue(user1.friends().contains(user2));
        assertTrue(user2.friends().contains(user1));
    }

    @Override
    protected void tearDown() throws Exception {
        source.deleteUser(user1);
        source.deleteUser(user2);

        super.tearDown();
    }
}