package allsense.shopwithfriends;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

public class JUnitTestYK extends ActivityInstrumentationTestCase2<WelcomeActivity> {
    private UserDataSource test;
    public JUnitTestYK() {
        super(WelcomeActivity.class);
    }
    protected void setUp() throws Exception {
        test = new UserDataSource(getActivity().getApplicationContext());
    }

    protected void tearDown() throws Exception {
        test.close();
        super.tearDown();
    }
    //General Test: 10 users
    public void test1() {
        User[] temp = new User[10];
        for (int x = 0; x < 10; x++) {
            temp[x] = test.createUser(x+"", x+"", x+"", x+"");
        }
        for (int x = 0; x < 10; x++) {
            for (int y = x; y < 10; y++) {
                if (x != y) {
                    test.addFriends(temp[x], temp[y]);
                }
            }
        }
        int tot = 0;
        for (int x = 0; x < 9; x++) {
            int tempa = (int)(Math.random()*5)+1;
            tot += tempa;
            test.rate(temp[x], temp[9], tempa);
        }
        tot = (int)(((double)tot)/9+0.5);
        assertEquals(test.rating(temp[9]), tot);

    }

    //General Test: 1 user
    public void test2() {
        User[] temp = new User[10];
        for (int x = 0; x < 10; x++) {
            temp[x] = test.createUser(x+"", x+"", x+"", x+"");
        }
        for (int x = 0; x < 10; x++) {
            for (int y = x; y < 10; y++) {
                if (x != y) {
                    test.addFriends(temp[x], temp[y]);
                }
            }
        }
        int tot = 0;
        for (int x = 0; x < 1; x++) {
            int tempa = (int)(Math.random()*5)+1;
            tot += tempa;
            test.rate(temp[x], temp[1], tempa);
        }
        assertEquals(test.rating(temp[1]), tot);

    }
    //General Test: 25 users
    public void test3() {
        User[] temp = new User[25];
        for (int x = 0; x < 25; x++) {
            temp[x] = test.createUser(x+"", x+"", x+"", x+"");
        }
        for (int x = 0; x < 25; x++) {
            for (int y = x; y < 25; y++) {
                if (x != y) {
                    test.addFriends(temp[x], temp[y]);
                }
            }
        }
        int tot = 0;
        for (int x = 0; x < 24; x++) {
            int tempa = (int)(Math.random()*5)+1;
            tot += tempa;
            test.rate(temp[x], temp[temp.length-1], tempa);
        }
        tot = (int)(((double)tot)/(temp.length-1)+0.5);
        assertEquals(test.rating(temp[temp.length-1]), tot);

    }
    //All the same rating: 5
    public void test4() {
        User[] temp = new User[25];
        for (int x = 0; x < 25; x++) {
            temp[x] = test.createUser(x+"", x+"", x+"", x+"");
        }
        for (int x = 0; x < 25; x++) {
            for (int y = x; y < 25; y++) {
                if (x != y) {
                    test.addFriends(temp[x], temp[y]);
                }
            }
        }
        int tot = 0;
        for (int x = 0; x < 24; x++) {
            int tempa = (int)(Math.random()*5)+1;
            tot += tempa;
            test.rate(temp[x], temp[temp.length-1], 5);
        }
        assertEquals(test.rating(temp[temp.length-1]), 5);
    }
    //Rounding Test: Round up
    public void test5() {
        User[] temp = new User[25];
        for (int x = 0; x < 25; x++) {
            temp[x] = test.createUser(x+"", x+"", x+"", x+"");
        }
        for (int x = 0; x < 25; x++) {
            for (int y = x; y < 25; y++) {
                if (x != y) {
                    test.addFriends(temp[x], temp[y]);
                }
            }
        }
        test.rate(temp[0], temp[2], 3);
        test.rate(temp[1], temp[2], 4);
        Log.e("SWF", temp[0].id()+" "+test.rating(temp[0]));
        Log.e("SWF", temp[1].id()+" "+test.rating(temp[1]));
        Log.e("SWF", temp[2].id() + " " + test.rating(temp[2]));
        assertEquals(test.rating(temp[0]), 0);
        assertEquals(test.rating(temp[1]), 0);
        assertEquals(test.rating(temp[2]), 4);
    }
    //Rounding Test: Round down
    public void test6() {
        User[] temp = new User[25];
        for (int x = 0; x < 25; x++) {
            temp[x] = test.createUser(x+"", x+"", x+"", x+"");
        }
        for (int x = 0; x < 25; x++) {
            for (int y = x; y < 25; y++) {
                if (x != y) {
                    test.addFriends(temp[x], temp[y]);
                }
            }
        }
        test.rate(temp[0], temp[3], 3);
        test.rate(temp[1], temp[3], 4);
        test.rate(temp[2], temp[3], 3);
        assertEquals(test.rating(temp[3]), 3);
    }
    //Single 0 Case
    public void test7() {
        User[] temp = new User[25];
        for (int x = 0; x < 25; x++) {
            temp[x] = test.createUser(x+"", x+"", x+"", x+"");
        }
        for (int x = 0; x < 25; x++) {
            for (int y = x; y < 25; y++) {
                if (x != y) {
                    test.addFriends(temp[x], temp[y]);
                }
            }
        }
        try {
            test.rate(temp[0], temp[1], 0);
        } catch(Exception e) {
            assertEquals("illegal rating: 0", e.getMessage());
        }
        assertEquals(test.rating(temp[1]), 0);
    }
    //
}