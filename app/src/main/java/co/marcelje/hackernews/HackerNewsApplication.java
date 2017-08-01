package co.marcelje.hackernews;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import co.marcelje.hackernews.injector.ModuleComponent;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class HackerNewsApplication extends Application {

    private ModuleComponent mModuleComponent;

    public static HackerNewsApplication getApplication(Application application) {
        return (HackerNewsApplication) application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initStetho();
        initDagger();
        initTimber();
        initCrashlytics();
    }

    public ModuleComponent getServiceComponent() {
        return mModuleComponent;
    }

    private void initStetho() {
        Stetho.initializeWithDefaults(this);
    }

    private void initDagger() {
        mModuleComponent = ModuleComponent.Factory.init();
    }

    private void initTimber() {
        Timber.plant(new Timber.DebugTree());
    }

    private void initCrashlytics() {
        Fabric.with(this, new Crashlytics());
    }
}
