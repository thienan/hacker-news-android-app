package com.marcelje.hackernews;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.marcelje.hackernews.injector.ModuleComponent;

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
}
