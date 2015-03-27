package allsense.shopwithfriends;

import android.test.ActivityInstrumentationTestCase2;

public class InterestForIDTest extends ActivityInstrumentationTestCase2<WelcomeActivity> {
    private InterestDataSource interestDataSource;

    public InterestForIDTest() {
        super(WelcomeActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        interestDataSource = new InterestDataSource(getActivity().getApplicationContext());
    }

    public void testSimple() {
        for (int i = 0; i < 100; i++) {
            Interest interest = interestDataSource.createInterest(Character.toString(
                    (char) ('A' + (int) (('Z' - 'A') * Math.random())))
                    , (int) (9999 * Math.random()));
            assertNotNull(interestDataSource.interestForID(interest.id()));
            assertEquals(interest, interestDataSource.interestForID(interest.id()));
            interestDataSource.deleteInterest(interest);
        }
    }

    public void testNull() {
        for (int i = 0; i < 100; i++) {
            Interest interest = interestDataSource.createInterest(Character.toString(
                    (char) ('A' + (int) (('Z' - 'A') * Math.random())))
                    , (int) (9999 * Math.random()));
            long temp = interest.id();
            interestDataSource.deleteInterest(interest);
            assertNull(interestDataSource.interestForID(temp));
        }
    }

    @Override
    protected void tearDown() throws Exception {
        interestDataSource.close();
        super.tearDown();
    }
}