package wang.wangxinarhat.rxandroidsamples.global;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;
import wang.wangxinarhat.rxandroidsamples.http.volley.VolleyHelper;

/**
 * Created by wang on 2016/4/1.
 */
public class BaseApplication extends Application{

    private static BaseApplication _instance;
    private RefWatcher _refWatcher;

    public static BaseApplication get() {
        return _instance;
    }

    public static RefWatcher getRefWatcher() {
        return BaseApplication.get()._refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = (BaseApplication) getApplicationContext();
        _refWatcher = LeakCanary.install(this);

        // Initialize Volley
        VolleyHelper.init(this);

        Timber.plant(new Timber.DebugTree());
    }
}
