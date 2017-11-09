package sticker.stdiohue.com.shsticker;

import android.app.Application;

public class App extends Application {

    private static App instance;

    public static App get() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("Application is null");
        }
        return instance;
    }
}
