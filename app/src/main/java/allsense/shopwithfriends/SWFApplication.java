package allsense.shopwithfriends;

import android.app.Application;

public class SWFApplication extends Application {
    @Override
    public void onCreate() {
        User.init(getApplicationContext());
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        User.deinit();
    }
}
