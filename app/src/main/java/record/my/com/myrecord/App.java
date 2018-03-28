package record.my.com.myrecord;

import android.app.Application;

/**
 * Created by Cody on 2016/12/30.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
