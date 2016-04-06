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

    private static BaseApplication application;
    private RefWatcher refWatcher;

    public static BaseApplication getApplication() {
        return application;
    }

    public static RefWatcher getRefWatcher() {
        return BaseApplication.getApplication().refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = (BaseApplication) getApplicationContext();
        refWatcher = LeakCanary.install(this);

        // Initialize Volley
        VolleyHelper.init(this);

        Timber.plant(new Timber.DebugTree());
    }
}
