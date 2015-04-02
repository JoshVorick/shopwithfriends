package allsense.shopwithfriends;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by Josh Vorick
 */
public class GetRelevantFriendsTest extends ActivityInstrumentationTestCase2<WelcomeActivity> {
    UserDataSource source;
    User user1; // Has interests
    User user2; // Has interests
    User user3; // Reports sales
    User user4; // Reports sales

    public GetRelevantFriendsTest() {
        super(WelcomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        source = new UserDataSource(getActivity().getApplicationContext());
        user1 = User.addUser("name1", "email1", "user1", "pass1");
        user2 = User.addUser("name2", "email2", "user2", "pass2");
        user3 = User.addUser("name3", "email3", "user3", "pass3");
        user4 = User.addUser("name4", "email4", "user4", "pass4");
    }

    public void testBeforeAndAfterFriends() {
        Interest.registerInterest(user1, "item1", 10);
        Interest.registerInterest(user1, "item2", 2000);
        Interest.registerInterest(user1, "item3", 30);
        Interest.registerInterest(user2, "item1", 1000);
        Interest.registerInterest(user2, "item2", 20);
        Interest.registerInterest(user2, "item3", 30);

        Item.reportSale("item1", "seller1", 100, 1.0f, -1.0f, user3);
        Item.reportSale("asdfa", "asdf", 1, 1.0f, -1.0f, user3); // report cheap gibberish
        Item.reportSale("item2", "seller2", 200, 2.0f, -2.0f, user4);
        Item.reportSale("asdfa", "asdf", 1, 1.0f, -1.0f, user3); // report cheap gibberish

        // Add user1 friends and make sure it behaves correctly
        source.addFriends(user1, user2);
        source.addFriends(user1, user3);
        assertEquals(Item.allRelevantSales(user1).size(), 0);
        source.addFriends(user1, user4);
        // Should change since user4's item was cheap enough
        assertEquals(Item.allRelevantSales(user1).size(), 1);
        assertEquals(Item.allRelevantSales(user1).get(0).name(), "item2");

        // Now test user2
        assertEquals(Item.allRelevantSales(user2).size(), 0);
        source.addFriends(user2, user3);
        // Should now have 1 relevant sale
        assertEquals(Item.allRelevantSales(user2).size(), 1);
        assertEquals(Item.allRelevantSales(user2).get(0).name(), "item1");
        source.addFriends(user2, user4);
        // Should stay the same
        assertEquals(Item.allRelevantSales(user2).size(), 1);
        assertEquals(Item.allRelevantSales(user2).get(0).name(), "item1");

        // Now make sure everyone is friends and double check that things are correct
        source.addFriends(user3, user4);
        assertEquals(Item.allRelevantSales(user1).size(), 1);
        assertEquals(Item.allRelevantSales(user1).get(0).name(), "item2");
        assertEquals(Item.allRelevantSales(user2).size(), 1);
        assertEquals(Item.allRelevantSales(user2).get(0).name(), "item1");
        assertEquals(Item.allRelevantSales(user3).size(), 0);
        assertEquals(Item.allRelevantSales(user4).size(), 0);
    }
}