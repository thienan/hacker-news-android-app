package com.marcelljee.hackernews.injector;

import com.marcelljee.hackernews.api.HackerNewsApi;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = RetrofitModule.class)
public interface ModuleComponent {
    void inject(HackerNewsApi hackerNewsApi);

    class Factory {
        public static ModuleComponent init() {
            return DaggerModuleComponent.builder()
                    .retrofitModule(new RetrofitModule())
                    .build();
        }
    }
}
