package allsense.shopwithfriends;

import android.app.Application;

/**
 * subclass of Application so we run code only once
 */
public class SWFApplication extends Application {
    public static boolean AUTO_LOGIN = false;

    @Override
    public void onCreate() {
        User.init(getApplicationContext());
        Item.init(getApplicationContext());
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        User.deinit();
        Item.deinit();
    }
}
