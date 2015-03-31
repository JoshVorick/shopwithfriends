package allsense.shopwithfriends;


import android.test.ActivityInstrumentationTestCase2;

public class UserDataSourceTest extends ActivityInstrumentationTestCase2<WelcomeActivity> {
    UserDataSource source;
    User user1;
    User user2;
    User user3;

    public UserDataSourceTest() {
        super(WelcomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        source = new UserDataSource(getActivity().getApplicationContext());
        user1 = User.addUser("name1", "email1", "user1", "pass1");
        user2 = User.addUser("name2", "email2", "user2", "pass2");
        user3 = User.addUser("name3", "email3", "user3", "pass3");
    }

    // Arthur Eubanks
    public void testOneAddFriends() {
        assertFalse(user1.friends().contains(user2));
        assertFalse(user2.friends().contains(user1));
        source.addFriends(user1, user2);
        assertTrue(user1.friends().contains(user2));
        assertTrue(user2.friends().contains(user1));
    }

    public void testMultipleAddFriends() {
        User[] users = {user1, user2, user3};

        for (User u1 : users) {
            for (User u2 : users) {
                assertFalse(u1.friends().contains(u2));
            }
        }

        user1.addFriend(user2);
        user2.addFriend(user3);

        assertTrue(user1.friends().contains(user2));
        assertTrue(user2.friends().contains(user3));
        assertFalse(user1.friends().contains(user3));

        user1.addFriend(user3);

        for (User u1 : users) {
            for (User u2 : users) {
                assertEquals(u1 != u2, u1.friends().contains(u2));
            }
        }
    }

    @Override
    protected void tearDown() throws Exception {
        source.deleteUser(user1);
        source.deleteUser(user2);
        source.deleteUser(user3);

        super.tearDown();
    }
}