package co.marcelje.hackernews.injector;

import co.marcelje.hackernews.api.HackerNewsApi;

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
