package co.marcelje.hackernews;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;

import co.marcelje.hackernews.injector.ModuleComponent;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class HackerNewsApplication extends Application {

    private static HackerNewsApplication instance;

    private ModuleComponent mModuleComponent;

    public static HackerNewsApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initSelf();
        initStetho();
        initDagger();
        initTimber();
        initLeakCanary();
        initCrashlytics();
    }

    public ModuleComponent getServiceComponent() {
        return mModuleComponent;
    }

    private void initSelf() {
        instance = this;
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

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }

    private void initCrashlytics() {
        // Set up Crashlytics, disabled for debug builds
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();

        // Initialize Fabric with the debug-disabled crashlytics.
        Fabric.with(this, crashlyticsKit);
    }
}
